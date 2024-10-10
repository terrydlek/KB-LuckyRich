<template>
  <div class="stocks">

    <h3>
      당신의 투자 성향은 적극 투자형입니다. 주식 상품 TOP 100을 추천해드릴게요.
    </h3>

    <input type="text" v-model="searchQuery" placeholder="검색하고 싶은 종목명이 있나요?" />

    <table v-if="filteredStocks.length" class="stock-table">
      <thead>
        <tr>
          <th>종목명</th>
          <th>현재가</th>
          <th>전일비</th>
          <th>등락률</th>
          <!-- <th>거래량</th> -->
          <th>시가총액</th>
          <th>매출액</th>
          <!-- <th>영업이익</th> -->
          <th>주당 순 이익</th>
          <th>PER</th>
          <th>ROE</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(stock, index) in paginatedStocks" :key="index">
          <td><a :href="`/luckyrich/recommend/active/${stock.stockCode}`">{{ stock.stockName }}</a></td>
          <td>{{ stock.stockPrice }}</td>
          <td>
            <span v-if="stock.comparePre.includes('하락')" style="color: #d32f2f; font-size: 15px;">
              ▼ {{ stock.comparePre.replace('하락', '-') }}
            </span>
            <span v-else-if="stock.comparePre.includes('보합')" style="color: #388e3c; font-size: 15px;">
              ▲ {{ stock.comparePre.replace('보합', '+') }}
            </span>
            <span v-else style="color: #388e3c; font-size: 15px;">
              ▲ {{ stock.comparePre.replace('상승', '+') }}
            </span>

          </td>


          <td>
            <span v-if="stock.fluctuationRate.startsWith('-')" style="color: #d32f2f; font-size: 15px;">
              ▼ {{ stock.fluctuationRate }}
            </span>
            <span v-else style="color: #388e3c; font-size: 15px;">
              ▲ {{ stock.fluctuationRate }}
            </span>
          </td>

          <!-- <td>{{ stock.tradingVolume }}</td> -->
          <td>{{ stock.marketCapitalization }}</td>
          <td>{{ stock.salesAmount }}</td>
          <!-- <td>{{ stock.operatingProfit }}</td> -->
          <td>{{ stock.earningsPerShare }}</td>
          <td>
            <span v-if="stock.per.startsWith('-')" style="color: #d32f2f; font-size: 15px;">
              ▼ {{ stock.per }}
            </span>
            <span v-else style="color: #388e3c; font-size: 15px;">
              ▲ {{ stock.per }}
            </span>
          </td>

          <td>
            <span v-if="stock.roe.startsWith('-')" style="color: #d32f2f; font-size: 15px;">
              ▼ {{ stock.roe }}
            </span>
            <span v-else style="color: #388e3c; font-size: 15px;">
              ▲ {{ stock.roe }}
            </span>
          </td>

        </tr>
      </tbody>
    </table>

    <div v-if="filteredStocks.length && totalPages > 1" class="pagination">
      <button @click="currentPage--" :disabled="currentPage === 1">이전</button>
      <button v-for="page in pageNumbers" :key="page" @click="currentPage = page"
        :class="{ active: currentPage === page }">
        {{ page }}
      </button>
      <button @click="currentPage++" :disabled="currentPage === totalPages">
        다음
      </button>
    </div>

    <button @click="resetTest" class="reset-test-button">
      테스트 다시하기
    </button>

    <p v-if="stocks.length && !filteredStocks.length">
      찾으시는 종목명은 TOP 100에 없습니다.
    </p>
    <p v-else-if="!stocks.length">주식 데이터를 불러오는 중입니다...</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const stocks = ref([]);
const searchQuery = ref('');
const currentPage = ref(1);
const itemsPerPage = 10;
const pageRange = 10; // 한번에 보여줄 페이지 번호 개수

// 검색된 종목 리스트
const filteredStocks = computed(() => {
  return stocks.value.filter((stock) =>
    stock.stockName.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

// 페이지네이션 적용된 종목 리스트
const paginatedStocks = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return filteredStocks.value.slice(start, end);
});

// 총 페이지 수 계산
const totalPages = computed(() => {
  return Math.ceil(filteredStocks.value.length / itemsPerPage);
});

// 페이지 번호 계산
const pageNumbers = computed(() => {
  const rangeStart =
    Math.floor((currentPage.value - 1) / pageRange) * pageRange + 1;
  const rangeEnd = Math.min(rangeStart + pageRange - 1, totalPages.value);
  return Array.from(
    { length: rangeEnd - rangeStart + 1 },
    (_, i) => rangeStart + i
  );
});

// 데이터 가져오기
const fetchStockData = async () => {
  const token = localStorage.getItem('access_token');
  try {
    const response = await axios.get('http://localhost:8080/recommend/active', {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    stocks.value = response.data;
  } catch (error) {
    console.error('주식 데이터를 가져오는 중 오류 발생:', error);
  }
};

// 테스트 다시하기
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

    // 로컬 스토리지에서 투자 유형 삭제
    localStorage.removeItem('investmentType');

    // 테스트 페이지로 리다이렉트
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

onMounted(() => {
  fetchStockData();
});

// 검색어가 변경되면 페이지를 처음으로 초기화
watch(searchQuery, () => {
  currentPage.value = 1;
});
</script>

<style scoped>
.stocks {
  margin-top: 40px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.reset-test-button {
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

.reset-test-button:hover {
  background-color: #b71c1c;
}

.stock-table {
  width: 95%;
  border-collapse: collapse;
  margin-top: 20px;
}

.stock-table th,
.stock-table td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

.stock-table th {
  background-color: #f2f2f2;
}

.stock-table tr:hover {
  background-color: #f5f5f5;
}

input[type='text'] {
  margin: 10px 0;
  padding: 10px;
  width: 50%;
  border: 1px solid #ddd;
  border-radius: 4px;
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

input[type="text"] {
  margin: 10px 0;
  padding: 5px;
  width: 50%;
}

button {
  margin: 5px;
  padding: 5px 10px;
}

button.active {
  font-weight: bold;
  background-color: #4caf50;
  color: white;
}

a {
    color: #6c63ff;
    text-decoration: none;
}

a:hover {
    text-decoration: underline;
}
</style>