<template>
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
      <h2>{{ coinData.name }} 가격 차트</h2>
      <CoinChart :coinId="coinData.id" />
    </div>
  </div>
  <div v-else>Loading...</div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';
import CoinChart from './CoinChart.vue';

const route = useRoute();
const coinData = ref(null);

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

<style scoped></style>
