<template>
  <div class="funds">
    <h2>
      당신의 투자 성향은 안정추구형입니다. 채권형 펀드 상품을 추천해드릴게요.
    </h2>
  </div>
  <section class="fund-tracker">
    <div class="controls">
      <div class="search">
        <input
          type="text"
          v-model="searchQuery"
          placeholder="검색하고 싶은 펀드명이 있나요?"
        />
      </div>
    </div>
    <div class="result">
      <div v-if="loading">데이터를 불러오는 중...</div>
      <div v-else-if="error">{{ error }}</div>
      <template v-else>
        <table v-if="filteredFunds.length">
          <thead>
            <tr>
              <th>국가</th>
              <th>펀드명</th>
              <th>최근가</th>
              <th>변동률</th>
              <th>총 자산</th>
              <!-- <th>갱신 시간</th> -->
            </tr>
          </thead>
          <tbody>
            <tr v-for="fund in paginatedFunds" :key="fund.url">
              <td>{{ fund.country }}</td>
              <td>
                <a
                  :href="`/luckyrich/recommend/funds/${encodeURIComponent(
                    fund.url
                  )}`"
                  >{{ fund.name }}</a
                >
              </td>
              <td>{{ formatNumber(fund.lastPrice) }}</td>
              <td
                :class="{
                  'text-green-500': parseFloat(fund.changePercent) >= 0,
                  'text-red-500': parseFloat(fund.changePercent) < 0,
                }"
              >
                <span
                  v-if="parseFloat(fund.changePercent) >= 0"
                  style="font-size: 15px"
                >
                  ▲ {{ fund.changePercent }}
                </span>
                <span v-else style="font-size: 15px">
                  ▼ {{ fund.changePercent }}
                </span>
              </td>

              <td>{{ formatNumber(fund.totalAssets) }}</td>
              <!-- <td>{{ fund.lastUpdate }}</td> -->
            </tr>
          </tbody>
        </table>

        <div v-if="filteredFunds.length && totalPages > 1" class="pagination">
          <button @click="currentPage--" :disabled="currentPage === 1">
            이전
          </button>
          <button
            v-for="page in pageNumbers"
            :key="page"
            @click="currentPage = page"
            :class="{ active: currentPage === page }"
          >
            {{ page }}
          </button>
          <button @click="currentPage++" :disabled="currentPage === totalPages">
            다음
          </button>
        </div>
        <center>
          <button @click="resetTest" class="test-reset-button">
            테스트 다시하기
          </button>
        </center>

        <div v-if="funds.length && !filteredFunds.length">
          검색 결과가 없습니다.
        </div>
        <div v-if="!funds.length">표시할 펀드 데이터가 없습니다.</div>
      </template>
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const funds = ref([]);
const loading = ref(true);
const error = ref(null);
const searchQuery = ref('');
const currentPage = ref(1);
const itemsPerPage = 10;
const pageRange = 10;

const fetchFunds = async () => {
  try {
    loading.value = true;
    const response = await axios.get(
      'http://localhost:8080/api/funds?riskRating=2'
    );
    funds.value = response.data;
  } catch (err) {
    console.error('펀드 데이터를 불러오는 데 실패했습니다.', err);
    error.value =
      '펀드 데이터를 불러오는 데 실패했습니다. 잠시 후 다시 시도해주세요.';
  } finally {
    loading.value = false;
  }
};

const formatNumber = (value) => {
  if (!value) return '-';
  return new Intl.NumberFormat('ko-KR').format(
    parseFloat(value.replace(/,/g, ''))
  );
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

const filteredFunds = computed(() => {
  return funds.value.filter(
    (fund) =>
      fund.name.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      fund.symbol.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

const totalPages = computed(() =>
  Math.ceil(filteredFunds.value.length / itemsPerPage)
);

const paginatedFunds = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return filteredFunds.value.slice(start, end);
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

const formatChangePercent = (value) => {
  const numValue = parseFloat(value);
  if (numValue > 0) {
    return `▲ ${value}`;
  } else if (numValue < 0) {
    return `▼ ${value}`;
  } else return value;
};

onMounted(fetchFunds);

watch(searchQuery, () => {
  currentPage.value = 1;
});
</script>

<style scoped>
.funds {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 20px;
}

.fund-tracker {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
}

.controls {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.search {
  flex-grow: 1;
}

input[type='text'] {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
}

.test-reset-button {
  margin-top: 20px;
  /* margin-left: 850px; */
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

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  border: 1px solid #ddd;
  padding: 12px 8px;
  text-align: left;
}

th {
  background-color: #f2f2f2;
}

tr:hover {
  background-color: #f5f5f5;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.pagination button {
  margin: 0 5px;
  padding: 5px 10px;
  border: 1px solid #ddd;
  background-color: #f8f8f8;
  cursor: pointer;
  transition: background-color 0.3s;
}

.pagination button:hover {
  background-color: #e8e8e8;
}

.pagination button.active {
  font-weight: bold;
  background-color: #4caf50;
  color: white;
  border-color: #4caf50;
}

.pagination button:disabled {
  color: #ccc;
  cursor: not-allowed;
}

.text-green-500 {
  color: #10b981;
}

.text-red-500 {
  color: #ef4444;
}
</style>
