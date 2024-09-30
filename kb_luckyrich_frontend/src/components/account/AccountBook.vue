<template>
  <div id="accountbook">
    <div class="cards-container">
      <button @click="prevCard">←</button>
      <div class="accounts">
        <div class="card"
          v-if="currentCard"
          :class="{ active: selectedAccount === currentCard.accountNumber }"
          :style="getCardStyle(currentCard.bankName)">
          <div class="card-header">
            <h3>{{ currentCard.bankName }}</h3>
          </div>
          <div class="card-body">
            <p>{{ currentCard.accountNumber }}</p>
          </div>
          <div class="circle" :style="getCircleStyle(currentCard.bankName)"></div>
        </div>
      </div>
      <button @click="nextCard">→</button>
    </div>

    <div class="currentmonth">
      <button @click="prevMonth">Previous Month</button>
      <span>{{ currentMonth }}, {{ currentYear }}</span>
      <button @click="nextMonth">Next Month</button>
    </div>

    <div class="filters">
      <select v-model="selectedType">
        <option value="">모든 유형</option>
        <option value="입금">입금</option>
        <option value="출금">출금</option>
      </select>
      <input v-model.number="minAmount" type="number" placeholder="최소 금액" />
      <input v-model.number="maxAmount" type="number" placeholder="최대 금액" />
    </div>

    <div class="transactions" ref="transactionList">
      <div
        v-for="transaction in filteredTransactions"
        :key="transaction.transactionDate.getTime()"
        class="transaction-item"
      >
        <div class="transaction-date">
          {{ formatDate(transaction.transactionDate) }}
        </div>
        <div class="transaction-details">
          <span
            :class="{
              income: transaction.transactionType === '입금',
              expense: transaction.transactionType === '출금',
            }"
          >
            {{ transaction.transactionType === '입금' ? '+' : '-' }}
            {{ formatCurrency(transaction.amount) }}원
          </span>
          <span class="category">{{ transaction.category }}</span>
          <span class="description">{{ transaction.description }}</span>
        </div>
      </div>
      <div v-if="isLoading" class="loading">데이터 로딩 중...</div>
      <div v-if="!hasMore && filteredTransactions.length === 0" class="no-data">
        거래 내역이 없습니다.
      </div>

      <button @click="nextSlide" class="next"></button>
    </div>
  </div>
</template>


<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import axios from 'axios';
import { useInfiniteScroll } from '@vueuse/core';
import { convertTimestampToDate, isSameYearAndMonth } from './transaction/dateUtils';
const transactions = ref([]);
const selectedAccount = ref(null);

const currentMonth = ref(new Date().getMonth() + 1);
const currentYear = ref(new Date().getFullYear());

const selectedType = ref('');
const minAmount = ref('');
const maxAmount = ref('');
const page = ref(1);
const transactionList = ref(null);
const hasMore = ref(true);
const isLoading = ref(false);



const bankNames = {
  1: '국민은행',
  2: '카카오뱅크',
  3: '신한은행',
};

const uniqueAccounts = computed(() => {
  const accountMap = new Map();
  transactions.value.forEach((transaction) => {
    if (!accountMap.has(transaction.accountNumber)) {
      accountMap.set(transaction.accountNumber, {
        accountNumber: transaction.accountNumber,
        bankName: bankNames[transaction.bankId] || '알 수 없는 은행',
      });
    }
  });
  return Array.from(accountMap.values());
});

const currentIndex = ref(0);
const currentCard = computed(() => uniqueAccounts.value[currentIndex.value] || null);

const nextCard = () => {
  currentIndex.value = (currentIndex.value + 1) % uniqueAccounts.value.length;
  selectAccount(currentCard.value.accountNumber); 
};

const prevCard = () => {
  currentIndex.value = (currentIndex.value - 1 + uniqueAccounts.value.length) % uniqueAccounts.value.length;
  selectAccount(currentCard.value.accountNumber); 
};

const selectAccount = (accountNumber) => {
  selectedAccount.value = accountNumber;
  resetAndFetchTransactions();
};

const resetAndFetchTransactions = () => {
  transactions.value = [];
  page.value = 1;
  // hasMore.value = true; 거래내역 없음 글씨가 안나와서 이 부분 false로 수정
  hasMore.value = false;
  getMyBankTransaction();
};

const filteredTransactions = computed(() => {
  return transactions.value.filter((t) => {
    const typeMatch =
      !selectedType.value || t.transactionType === selectedType.value;
    const amountMatch =
      (!minAmount.value || t.amount >= minAmount.value) &&
      (!maxAmount.value || t.amount <= maxAmount.value);
    const accountMatch =
      !selectedAccount.value || t.accountNumber === selectedAccount.value;
    const dateMatch = isSameYearAndMonth(
      t.transactionDate,
      currentYear.value,
      currentMonth.value
    );
    return typeMatch && amountMatch && accountMatch && dateMatch;
  });
});

const prevMonth = () => {
  currentMonth.value--;
  if (currentMonth.value < 1) {
    currentMonth.value = 12;
    currentYear.value--;
  }
  resetAndFetchTransactions();
};

const nextMonth = () => {
  currentMonth.value++;
  if (currentMonth.value > 12) {
    currentMonth.value = 1;
    currentYear.value++;
  }
  resetAndFetchTransactions();
};

function formatCurrency(amount) {
  return new Intl.NumberFormat('ko-KR').format(amount);
}

const { isScrolling } = useInfiniteScroll(
  transactionList,
  () => {
    if (hasMore.value && !isLoading.value) {
      getMyBankTransaction();
    }
  },
  { distance: 10 }
);

watch([selectedType, minAmount, maxAmount], () => {
  if (transactionList.value) {
    transactionList.value.scrollTop = 0;
  }
});

async function getMyBankTransaction() {
  isLoading.value = true;
  const token = localStorage.getItem('access_token');

  try {
    const response = await axios.get(
      'http://localhost:8080/myasset/getbanktransaction',
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
        params: {
          year: currentYear.value,
          month: currentMonth.value,
          accountNumber: selectedAccount.value,
        },
      }
    );

    console.log('API Response:', response.data);

    if (response.data && Array.isArray(response.data)) {
      transactions.value = response.data.map((transaction) => ({
        ...transaction,
        transactionDate: convertTimestampToDate(transaction.transactionDate),
      }));
    } else {
      console.error('Unexpected API response format:', response.data);
      transactions.value = [];
    }
  } catch (error) {
    console.error('거래 내역을 가져오는 중 오류 발생:', error);
    if (error.response) {
      console.error('Error response:', error.response.data);
      console.error('Error status:', error.response.status);
      if (error.response.status === 401) {
        console.error('인증 오류: 토큰이 만료되었거나 유효하지 않습니다.');
      }
    }
    transactions.value = [];
  } finally {
    isLoading.value = false;
  }
}

onMounted(() => {
  getMyBankTransaction();
});

function formatDate(date) {
  if (!(date instanceof Date)) {
    date = new Date(date);
  }
  return date.toLocaleDateString('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  });
}

const getCardStyle = (bankName) => {
  switch (bankName) {
    case '국민은행':
      return {
        backgroundColor: 'orange',
        backgroundSize: 'cover',
        backgroundPosition: 'center',
      };
    case '카카오뱅크':
      return {
        backgroundColor: 'yellow',
        backgroundSize: 'cover',
        backgroundPosition: 'center',
      };
    case '신한은행':
      return {
        backgroundColor: 'skyblue',
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        color: 'black',
      };
    default:
      return {};
  }
};

const getCircleStyle = (bankName) => {
  switch (bankName) {
    case '국민은행':
      return {
        backgroundImage: 'url(/src/assets/images/국민은행.png)',
        backgroundSize: 'cover',
        backgroundPosition: 'center',
      };
    case '카카오뱅크':
      return {
        backgroundImage: 'url(/src/assets/images/카카오뱅크.jpg)',
        backgroundSize: 'cover',
        backgroundPosition: 'center',
      };
    case '신한은행':
      return {
        backgroundImage: 'url(/src/assets/images/신한은행로고.png)',
        backgroundSize: 'cover',
        backgroundPosition: 'center',
      };
    default:
      return {
        backgroundColor: '#007bff',
      };
  }
};
</script>


<style>
.cards-container {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 20px;
}

.accounts {
  display: flex;
  justify-content: center; 
  align-items: center;
}

.card {
  width: 200px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 8px;
  cursor: pointer;
  text-align: center;
  transition: transform 0.2s;
  position: relative;
  margin-top: 10%;
}

.card.active {
  border-color: #007bff;
  transform: scale(1.05);
}

.card-header {
  font-size: 1.2rem;
  font-weight: bold;
}

.circle {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  position: absolute;
  bottom: 10px;
  right: 10px;
}

.transactions {
  margin-top: 1rem;
}

.transactions table {
  width: 100%;
  border-collapse: collapse;
}

.transactions th,
.transactions td {
  padding: 8px;
  border: 1px solid #ddd;
  text-align: left;
}
.currentmonth {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
.filters {
  margin-bottom: 20px;
}

.filters select,
.filters input {
  margin-right: 10px;
  padding: 5px;
}

.transactions {
  height: 500px;
  overflow-y: auto;
}

.transaction-item {
  border-bottom: 1px solid #eee;
  padding: 10px 0;
}

.transaction-date {
  font-size: 0.9em;
  color: #888;
}

.transaction-details {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 5px;
}

.income {
  color: #4caf50;
}

.expense {
  color: #f44336;
}

.category {
  background-color: #f0f0f0;
  padding: 2px 5px;
  border-radius: 3px;
  font-size: 0.8em;
}

.description {
  flex-grow: 1;
  margin-left: 10px;
  font-size: 0.9em;
}

.loading,
.no-data {
  text-align: center;
  padding: 20px;
  color: #888;
}
</style>
