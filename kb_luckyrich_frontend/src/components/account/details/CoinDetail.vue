<template>
  <button @click="goBack" class="back-button">목록으로</button>
  <div v-if="coinData">
    <h1>{{ coinData.name }}({{ coinData.symbol }}) Metrics</h1>

    <div class="metrics">
      <div class="metric">
        <h3>시가 총액</h3>
        <p>{{ formatCurrency(coinData.quotes?.USD?.market_cap) }}</p>
      </div>
      <div class="metric">
        <h3>사상 최고</h3>
        <p>{{ formatCurrency(coinData.quotes?.USD?.ath_price) }}</p>
      </div>
      <div class="metric">
        <h3>24시간 거래량</h3>
        <p>{{ formatCurrency(coinData.quotes?.USD?.volume_24h) }}</p>
      </div>
      <div class="metric">
        <h3>현재 가격</h3>
        <p>{{ formatCurrency(coinData.quotes?.USD?.price) }}</p>
      </div>
    </div>

    <div class="chart">
      <h2>{{ coinData.name }} 일일 가격 차트</h2>
      <CoinChart :coinId="coinData.id" />
    </div>
  </div>
  <div v-else>Loading...</div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import CoinChart from './CoinChart.vue';

const route = useRoute();
const router = useRouter();
const coinData = ref(null);

const goBack = () => {
  router.go(-1);
};

const fetchCoinData = async () => {
  try {
    const response = await axios.get(
      `https://api.coinpaprika.com/v1/tickers/${route.params.id}`
    );
    coinData.value = response.data;
  } catch (error) {
    console.error('Error fetching coin data', error);
  }
};

const formatCurrency = (value) => {
  if (value == null || isNaN(value)) return 'N/A';
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  }).format(value);
};

onMounted(() => {
  fetchCoinData();
});
</script>

<style scoped>
.back-button {
  width: 10%;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 10px 20px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s ease;
  margin-bottom: 20px;
}

.back-button:hover {
  background-color: #2980b9;
}

h1 {
  font-size: 40px;
  font-weight: bold;
  margin-bottom: 30px;
}

.metrics {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.metric {
  background-color: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  padding: 25px 20px;
  flex: 1 1 calc(50% - 20px); /* 반응형으로 카드들이 2열로 정렬됨 */
  min-width: 200px; /* 카드 최소 크기 */
  text-align: center;
}

.metric h3 {
  font-size: 18px;
  margin-bottom: 10px;
}

.metric p {
  font-size: 20px;
  font-weight: bold;
  color: #2c3e50;
}

.chart {
  margin-top: 40px;
}

.chart h2 {
  font-size: 25px;
  font-weight: bold;
  margin-bottom: 20px;
}

@media (max-width: 768px) {
  .metric {
    flex: 1 1 100%; /* 화면이 작아지면 카드가 1열로 정렬됨 */
  }

  h1 {
    font-size: 20px;
  }
}
</style>
