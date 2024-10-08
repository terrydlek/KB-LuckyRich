<template>
  <div class="coins">

    <h3>
      당신의 투자 성향은 공격투자형입니다. 암호화폐 상품 TOP 100을
      추천해드릴게요.
    </h3>
  </div>
  <section class="coin-tracker">
    <div class="btn">
      <button @click="refreshPage">새로고침</button>
    </div>
    <div class="search">
      <input type="text" v-model="searchQuery" placeholder="검색하고 싶은 종목명이 있나요?" />
    </div>
    <div class="result">
      <span v-if="loading" class="loader">Loading...</span>
      <template v-else>
        <table v-if="filteredCoins.length">
          <thead>
            <tr>
              <th>순위</th>
              <th>종목</th>
              <th>심볼</th>
              <th>가격(KRW)</th>
              <th>총 시가</th>
              <th>거래량</th>
              <th>변동(24H)</th>
              <th>변동(7D)</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="coin in paginatedCoins" :key="coin.rank">
              <td>{{ coin.rank }}위</td>
              <td>
                <a href="#" @click.prevent="goToCoinDetail(coin.id)">{{
                  coin.name
                }}</a>
              </td>
              <td>{{ coin.symbol }}</td>
              <td>
                <span v-if="coin.quotes.KRW.price >= 100000000">
                  {{
                    (coin.quotes.KRW.price / 100000000).toLocaleString(
                      undefined,
                      { maximumFractionDigits: 2 }
                    )
                  }}억원
                </span>
                <span v-else-if="coin.quotes.KRW.price >= 10000">
                  {{
                    (coin.quotes.KRW.price / 10000).toLocaleString(undefined, {
                      maximumFractionDigits: 0,
                    })
                  }}만원
                </span>
                <span v-else-if="coin.quotes.KRW.price >= 1000">
                  {{
                    (coin.quotes.KRW.price / 1000).toLocaleString(undefined, {
                      maximumFractionDigits: 1,
                    })
                  }}천원
                </span>
                <span v-else>
                  {{ Math.round(coin.quotes.KRW.price).toLocaleString() }}원
                </span>
              </td>
              <td>
                {{ (coin.quotes.KRW.market_cap / 1000000000000).toFixed(2) }}T
              </td>
              <td>
                {{ (coin.quotes.KRW.volume_24h / 1000000000000).toFixed(2) }}T
              </td>
              <td>
                <span v-if="coin.quotes.KRW.percent_change_24h < 0" style="color: #d32f2f; font-size: 15px;">
                  ▼ {{ coin.quotes.KRW.percent_change_24h.toFixed(2) }}%
                </span>
                <span v-else style="color: #388e3c; font-size: 15px;">
                  ▲ {{ coin.quotes.KRW.percent_change_24h.toFixed(2) }}%
                </span>
              </td>

              <td>
                <span v-if="coin.quotes.KRW.percent_change_7d < 0" style="color: #d32f2f; font-size: 15px;">
                  ▼ {{ coin.quotes.KRW.percent_change_7d.toFixed(2) }}%
                </span>
                <span v-else style="color: #388e3c; font-size: 15px;">
                  ▲ {{ coin.quotes.KRW.percent_change_7d.toFixed(2) }}%
                </span>
              </td>

            </tr>
          </tbody>
        </table>

        <div v-if="filteredCoins.length && totalPages > 1" class="pagination">
          <button @click="currentPage--" :disabled="currentPage === 1">
            이전
          </button>
          <button v-for="page in pageNumbers" :key="page" @click="currentPage = page"
            :class="{ active: currentPage === page }">
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

        <p v-if="coins.length && !filteredCoins.length">
          찾으시는 종목명은 TOP 100에 없습니다.
        </p>
        <p v-if="!coins.length">암호화폐 데이터를 불러오는 중입니다...</p>
      </template>
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const loading = ref(true);
const coins = ref([]);
const userId = ref(null);
const searchQuery = ref('');
const currentPage = ref(1);
const itemsPerPage = 10;
const pageRange = 10; // 한번에 보여줄 페이지 번호 개수

const refreshPage = () => {
  window.location.reload();
};

const fetchCoins = () => {
  fetch('https://api.coinpaprika.com/v1/tickers?quotes=KRW')
    .then((response) => response.json())
    .then((json) => {
      coins.value = json.slice(0, 100);
      loading.value = false;
    });
};

const goToCoinDetail = (coinId) => {
  router.push({ name: 'CoinDetail', params: { id: coinId } });
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

// 검색 및 페이지네이션을 적용한 코인 목록
const filteredCoins = computed(() => {
  return coins.value.filter(
    (coin) =>
      coin.name.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      coin.symbol.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

const totalPages = computed(() =>
  Math.ceil(filteredCoins.value.length / itemsPerPage)
);

const paginatedCoins = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return filteredCoins.value.slice(start, end);
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

onMounted(async () => {
  fetchCoins();

  const token = localStorage.getItem('access_token');
  if (token) {
    try {
      const response = await axios.get('http://localhost:8080/user', {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (response.data && response.data.userId) {
        userId.value = response.data.userId;
      } else {
        console.error('사용자 정보를 가져올 수 없습니다.');
      }
    } catch (error) {
      console.error('사용자 정보 가져오기 중 오류 발생:', error);
    }
  }
});

// 검색어가 변경되면 페이지를 처음으로 초기화
watch(searchQuery, () => {
  currentPage.value = 1;
});
</script>

<style scoped>
.coins {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 20px;
}

.coin-tracker {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
}

.btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 20px;
}

.search {
  flex-grow: 1;
  margin-left: 20px;
  margin-bottom: 20px;
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
</style>
