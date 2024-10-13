<template>
  <div class="fund-container">
    <h2 class="title">
      당신의 투자 성향은 위험중립형입니다. 재간접 펀드 상품을 추천해드릴게요.
    </h2>
    <div class="recommendation-explanation">
      <li>위험 중립형 투자자는 자산의 안정성과, 수익을 동시에 추구하는 경향이 있습니다.
        큰 손실을 감수하기보다는, 안정적인 자산 관리를 통해 꾸준한 성과를 내는 것을 목표로 합니다.</li> 
      <li>재간접 펀드는 여러 자산에 분산 투자할 수 있는 구조로, 전문 운용사가 자산 배분을 담당합니다. 리스크를 분산시키고 안정적인 수익을 제공하므로, 적합한 선택입니다.</li> 
      <li>시장 상황에 따라 운용사가 적극적으로 자산 배분 전략을 수정해, 투자자들이 일일이 자산을 관리하지 않아도 되는 장점이 있습니다.</li>
    </div>
    <div class="search-container">
      <input type="text" v-model="searchQuery" placeholder="검색하고 싶은 펀드명이 있나요?" class="search-input" />
    </div>
    <div v-if="loading" class="loading">데이터를 불러오는 중...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <template v-else>
      <table v-if="filteredFunds.length" class="fund-table">
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
              <a :href="`/luckyrich/recommend/funds/${encodeURIComponent(
                fund.url
              )}`" class="fund-link">{{ fund.name }}</a>
            </td>
            <td>{{ formatNumber(fund.lastPrice) }}</td>
            <td :class="{
              'text-green-500': parseFloat(fund.changePercent) > 0,
              'text-red-500': parseFloat(fund.changePercent) < 0,
            }">
              <span v-if="parseFloat(fund.changePercent) > 0" style="font-size: 15px">
                ▲ {{ fund.changePercent }}
              </span>
              <span v-else-if="parseFloat(fund.changePercent) < 0" style="font-size: 15px">
                ▼ {{ fund.changePercent }}
              </span>
              <span v-else style="font-size: 15px">
                0%
              </span>
            </td>
            <td>{{ formatNumber(fund.totalAssets) }}</td>
            <!-- <td>{{ fund.lastUpdate }}</td> -->
          </tr>
        </tbody>
      </table>

      <div v-if="filteredFunds.length && totalPages > 1" class="pagination">
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

      <br />

      <button @click="resetTest" class="test-reset-button">
        테스트 다시하기
      </button>

      <div v-if="funds.length && !filteredFunds.length" class="no-results">
        검색 결과가 없습니다.
      </div>
      <div v-if="!funds.length" class="no-data">
        표시할 펀드 데이터가 없습니다.
      </div>
    </template>
  </div>
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
    const token = localStorage.getItem('access_token'); // 토큰을 로컬 스토리지에서 가져옵니다.
    const response = await axios.get(
      'http://localhost:8080/recommend/neutral',
      {
        headers: {
          Authorization: `Bearer ${token}`, // 요청 헤더에 토큰을 추가합니다.
        },
      }
    );
    funds.value = response.data;
  } catch (err) {
    console.error('펀드 데이터를 불러오는 데 실패했습니다.', err);
    error.value =
      '펀드 데이터를 불러오는 데 실패했습니다. 잠시 후 다시 시도해주세요.';
    if (err.response && err.response.status === 401) {
      // 인증 오류 시 로그인 페이지로 리다이렉트
      router.push('/login');
    }
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
.fund-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.title {
  font-size: 30px;
  text-align: center;
  color: #333;
  margin-bottom: 20px;
}

.test-reset-button {
  margin-top: 30px;
  margin-bottom: 20px;
  display: block;
  margin: 0 auto 20px;
  padding: 10px 20px;
  font-size: 16px;
  color: white;
  background-color: #d32f2f;
  border: none;
  border-radius: 20px;
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

.fund-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

.fund-table th,
.fund-table td {
  border: 1px solid #ddd;
  padding: 12px;
  text-align: left;
}

.fund-table th {
  background-color: #f2f2f2;
  font-weight: bold;
}

.fund-table tr:nth-child(even) {
  background-color: #f8f8f8;
}

.fund-link {
  color: #1a73e8;
  text-decoration: none;
}

.fund-link:hover {
  text-decoration: underline;
}

.text-green-500 {
  color: #10b981;
}

.text-red-500 {
  color: #ef4444;
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
  background-color: #e4e4e4;
  cursor: pointer;
  transition: background-color 0.3s;
}

.pagination-button:hover:not(:disabled) {
  background-color: #e8e8e8;
}

.pagination-button.active {
  font-weight: bold;
  background-color: #3498db;
  color: white;
  border-color: #3498db;
}

.pagination-button:disabled {
  color: #ccc;
  cursor: not-allowed;
}

.loading,
.error,
.no-results,
.no-data {
  text-align: center;
  margin-top: 20px;
  font-size: 18px;
  color: #666;
}

a {
  color: #6c63ff;
  text-decoration: none;
}

a:hover {
  text-decoration: underline;
}

.recommendation-explanation {
  font-size: 16px;
  color: #555; /* 설명 텍스트 색상 */
  margin-bottom: 20px; /* 설명과 검색창 사이 여백 */
}
</style>
