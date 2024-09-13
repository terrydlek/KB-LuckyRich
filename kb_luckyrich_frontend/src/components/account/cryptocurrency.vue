<template>
  <section class="coin-tracker">
    <div class="title">
      <h1>암호화폐 실시간 TOP 100</h1>
      <div class="btn">
        <button @click="refreshPage">새로고침</button>
      </div>
    </div>
    <!-- 결과 출력 -->
    <div class="result">
      <span v-if="loading" class="loader">Loading...</span>
      <!-- 로딩이 끝나면 결과 출력 -->
      <table v-else>
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
          <tr v-for="coin in coins" :key="coin.rank">
            <td>{{ coin.rank }}위</td>
            <td>{{ coin.name }}</td>
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
            <td>{{ coin.quotes.KRW.percent_change_24h.toFixed(2) }}%</td>
            <td>{{ coin.quotes.KRW.percent_change_7d.toFixed(2) }}%</td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue';

const loading = ref(true); // 로딩 중으로 초기화
const coins = ref([]); // 빈 배열로 초기화

const refreshPage = () => {
  window.location.reload();
};

const fetchCoins = () => {
  fetch('https://api.coinpaprika.com/v1/tickers?quotes=KRW')
    .then((response) => response.json())
    .then((json) => {
      coins.value = json.slice(0, 100); // 가져온 데이터 1~100위까지
      loading.value = false; // 로딩 멈추기
    });
};

onMounted(() => {
  fetchCoins();
});
</script>

<style scoped></style>
