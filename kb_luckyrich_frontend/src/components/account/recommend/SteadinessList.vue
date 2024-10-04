<template>
  <div class="deposit-container">
    <button @click="resetTest" class="test-reset-button">
      테스트 다시하기
    </button>
    <h2 class="title">
      당신의 투자 성향은 안정형입니다. 예적금 상품을 추천해드릴게요.
    </h2>
    <div class="search-container">
      <input type="text" v-model="searchQuery" placeholder="검색하고 싶은 상품명이 있나요?" class="search-input" />
    </div>

    <div v-if="loading" class="loading">데이터를 불러오는 중...</div>
    <template v-else>
      <table v-if="filteredDeposits.length" class="deposit-table">
        <thead>
          <tr>
            <th>금융회사</th>
            <th>상품명</th>
            <th>최고 우대 금리</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(deposit, index) in paginatedDeposits" :key="index">
            <td>{{ deposit.company }}</td>
            <td>
              <a :href="`/luckyrich/recommend/steadiness/${deposit.prodname}`" class="deposit-link">{{ deposit.prodname
                }}</a>
            </td>
            <td>{{ deposit.bestinterest ?? 'N/A' }}</td>
          </tr>
        </tbody>
      </table>

      <div v-if="filteredDeposits.length && totalPages > 1" class="pagination">
        <button @click="currentPage--" :disabled="currentPage === 1" class="pagination-button">
          이전
        </button>
        <button v-for="page in pageNumbers" :key="page" @click="currentPage = page"
          :class="{ active: currentPage === page }" class="pagination-button">
          {{ page }}
        </button>
        <button @click="currentPage++" :disabled="currentPage === totalPages" class="pagination-button">
          다음
        </button>
      </div>

      <div v-if="deposits.length && !filteredDeposits.length" class="no-results">
        검색 결과가 없습니다.
      </div>
      <div v-if="!deposits.length" class="no-data">
        표시할 예적금 상품이 없습니다.
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const deposits = ref([]);
const loading = ref(true);
const searchQuery = ref('');
const currentPage = ref(1);
const itemsPerPage = 10;
const pageRange = 10;

const fetchDepositData = async () => {
  const token = localStorage.getItem('access_token');
  try {
    loading.value = true;
    const response = await axios.get(
      'http://localhost:8080/recommend/steadiness',
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    deposits.value = response.data;
  } catch (error) {
    console.error('There was an error fetching the deposits:', error);
  } finally {
    loading.value = false;
  }
};

const resetTest = async () => {
  try {
    const token = localStorage.getItem('access_token');
    if (!token) {
      console.error('토큰이 없습니다. 로그인 페이지로 이동합니다.');
      router.push('/luckyrich/login');
      return;
    }

    const response = await axios.delete(
      'http://localhost:8080/investment/resetResult',
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    console.log('서버 응답:', response.data);
    localStorage.removeItem('investmentType');
    router.push('/luckyrich/recommend');
  } catch (error) {
    console.error('테스트 리셋 중 오류 발생:', error);
    if (error.response) {
      console.error('서버 응답:', error.response.data);
      alert(`테스트 리셋에 실패했습니다: ${error.response.data}`);
    } else {
      alert('테스트 리셋에 실패했습니다. 다시 시도해 주세요.');
    }
  }
};

const filteredDeposits = computed(() => {
  return deposits.value.filter(
    (deposit) =>
      deposit.prodname
        .toLowerCase()
        .includes(searchQuery.value.toLowerCase()) ||
      deposit.company.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

const totalPages = computed(() =>
  Math.ceil(filteredDeposits.value.length / itemsPerPage)
);

const paginatedDeposits = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return filteredDeposits.value.slice(start, end);
});

const pageNumbers = computed(() => {
  const rangeStart =
    Math.floor((currentPage.value - 1) / pageRange) * pageRange + 1;
  const rangeEnd = Math.min(rangeStart + pageRange - 1, totalPages.value);
  return Array.from(
    { length: rangeEnd - rangeStart + 1 },
    (_, i) => rangeStart + i
  );
});

onMounted(fetchDepositData);

watch(searchQuery, () => {
  currentPage.value = 1;
});
</script>

<style scoped>
.deposit-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

.title {
  text-align: center;
  color: #333;
  margin-bottom: 20px;
}

.test-reset-button {
  display: block;
  margin: 0 auto 20px;
  padding: 10px 20px;
  font-size: 16px;
  color: white;
  background-color: #d32f2f;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.test-reset-button:hover {
  background-color: #b71c1c;
}

.search-container {
  margin-bottom: 20px;
}

.search-input {
  width: 100%;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.deposit-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

.deposit-table th,
.deposit-table td {
  padding: 12px;
  text-align: left;
}

.deposit-table th {
  font-weight: bold;
}

.deposit-table tr:nth-child(even) {
  background-color: #f8f8f8;
}

.deposit-link {
  color: #1a73e8;
  text-decoration: none;
}

.deposit-link:hover {
  text-decoration: underline;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.pagination-button {
  margin: 0 5px;
  padding: 5px 10px;
  border: 1px solid #ddd;
  background-color: #f8f8f8;
  cursor: pointer;
  transition: background-color 0.3s;
}

.pagination-button:hover:not(:disabled) {
  background-color: #e8e8e8;
}

.pagination-button.active {
  font-weight: bold;
  background-color: #4caf50;
  color: white;
  border-color: #4caf50;
}

.pagination-button:disabled {
  color: #ccc;
  cursor: not-allowed;
}

.loading,
.no-results,
.no-data {
  text-align: center;
  margin-top: 20px;
  font-size: 18px;
  color: #666;
}
</style>
