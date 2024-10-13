<template>
  <div class="stocks">
    <h3>
      당신의 투자 성향은 적극 투자형입니다. 주식 상품 TOP 100을 추천해드릴게요.
      <div @click="refreshPage" class="refresh-icon">
        <i class="fas fa-sync-alt"></i>
      </div>
    </h3>

    <div class="recommendation-explanation">
      <li>적극 투자형 투자자는 높은 수익을 추구하며,위험을 감수할 준비가 되어 있는 투자자입니다. 주식 시장의 기회를 적극적으로 활용하여 장기적인 성장 잠재력에 투자하는
        것을 선호합니다.</li>

      <li>주식은 높은 수익 잠재력을 가지고 있으며, 다양한 산업과 기업에 분산 투자할 수 있는 기회를 제공합니다. 특히 TOP 100 주식은 장기적으로 안정적인
        성장을 기대할 수 있습니다.</li>

      <li>주식 상품은 높은 수익률을 추구하는 데 매우 적합합니다. 시장의 변동성을 활용해 적극적으로 매매를 진행할 수 있는 기회를 제공하며, 장기적으로 자산 증대를 도모할 수
        있습니다.</li>
    </div>
    <div class="search">
      <input type="text" v-model="searchQuery" placeholder="검색하고 싶은 종목명이 있나요?" />
    </div>

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
          <td>
            <a :href="`/luckyrich/recommend/active/${stock.stockCode}`">{{
              stock.stockName
              }}</a>
          </td>
          <td>{{ stock.stockPrice }}</td>
          <td>
            <span v-if="stock.comparePre.includes('하락')" style="color: #d32f2f; font-size: 15px">
              ▼ {{ stock.comparePre.replace('하락', '-') }}
            </span>
            <span v-else-if="stock.comparePre.includes('보합')" style="font-size: 15px">
              {{ stock.comparePre.replace('보합', '') }}
            </span>
            <span v-else style="color: #388e3c; font-size: 15px">
              ▲ {{ stock.comparePre.replace('상승', '+') }}
            </span>
          </td>

          <td>
            <span v-if="stock.fluctuationRate !== '0.00%' && stock.fluctuationRate.startsWith('-')"
              style="color: #d32f2f; font-size: 15px">
              ▼ {{ stock.fluctuationRate }}
            </span>
            <span v-else-if="stock.fluctuationRate !== '0.00%' && !stock.fluctuationRate.startsWith('-')"
              style="color: #388e3c; font-size: 15px">
              ▲ {{ stock.fluctuationRate }}
            </span>
            <span v-else>
              0%
            </span>
          </td>

          <!-- <td>{{ stock.tradingVolume }}</td> -->
          <td>{{ stock.marketCapitalization }}</td>
          <td>{{ stock.salesAmount }}</td>
          <!-- <td>{{ stock.operatingProfit }}</td> -->
          <td>{{ stock.earningsPerShare }}</td>
          <td>{{ stock.per }}</td>

          <td>{{ stock.roe }}</td>
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
    <center>
      <button @click="resetTest" class="reset-test-button">
        테스트 다시하기
      </button>
    </center>

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

const refreshPage = () => {
  console.log('Refresh page.');
  window.location.reload();
};

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

.search {
  width: 100%;
  display: flex;
  /* flex를 사용해 자식 요소 가운데 정렬 */
  justify-content: center;
  /* 수평 가운데 정렬 */
  margin: 0 auto;
  /* margin-bottom: 20px; */
}

.reset-test-button {
  margin-top: 30px;
  margin-bottom: 20px;
  padding: 10px 20px;
  font-size: 16px;
  color: white;
  background-color: #d32f2f;
  border: none;
  border-radius: 20px;
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
  padding: 10px;
  width: 100%;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
}

.pagination {
  margin-top: 30px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.pagination button {
  margin: 0 5px;
  padding: 5px 10px;
  border: 1px solid #ddd;
  background-color: #e4e4e4;
  cursor: pointer;
  transition: background-color 0.3s;
}

.pagination button:hover {
  background-color: #e8e8e8;
}

.pagination button.active {
  font-weight: bold;
  background-color: #3498db;
  color: white;
  border-color: #3498db;
}

.pagination button:disabled {
  color: #ccc;
  cursor: not-allowed;
}

input[type='text'] {
  margin: 10px 0;
  padding: 5px;
  width: 50%;
}

button {
  margin: 5px;
  padding: 5px 10px;
}

.refresh-icon {
  font-size: 24px;
  cursor: pointer;
  margin-left: 2px;
  color: #3498db;
  transition: color 0.3s, transform 0.2s ease;
  display: inline-block;
  /* 클릭할 수 있도록 영역 확장 */
  position: relative;
  z-index: 1000;
  /* 다른 요소 위에 위치 */
}

.refresh-icon:hover .refresh-icon {
  color: #2980b9;
  transform: scale(1.2);
}

/* button.active {
  font-weight: bold;
  background-color: #4caf50;
  color: white;
}*/

.recommendation-explanation {
  font-size: 16px;
  color: #555;
  /* 설명 텍스트 색상 */
  margin-bottom: 20px;
  /* 설명과 검색창 사이 여백 */
}
</style>
