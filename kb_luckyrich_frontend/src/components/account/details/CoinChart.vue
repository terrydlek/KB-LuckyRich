<template>
  <div>
    <canvas v-if="!error && chartData.length" ref="chartCanvas"></canvas>
    <p v-else-if="error" class="error-message">{{ error }}</p>
    <p v-else>로딩 중...</p>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, nextTick } from 'vue';
import Chart from 'chart.js/auto';
import axios from 'axios';

const props = defineProps({
  coinId: {
    type: String,
    required: true,
  },
});

const chartCanvas = ref(null);
const error = ref(null);
const chartData = ref([]);
let chart = null;

const getCoingeckoId = (coinpaprikaId) => {
  const mapping = {
    'btc-bitcoin': 'bitcoin',
    'eth-ethereum': 'ethereum',
    'usdt-tether': 'tether',
    'bnb-binance-coin': 'binancecoin',
    'xrp-xrp': 'ripple',
    // 필요에 따라 더 많은 매핑을 추가할 수 있습니다.
  };
  return mapping[coinpaprikaId] || coinpaprikaId.split('-')[1];
};

const fetchCoinGeckoData = async (coinId) => {
  try {
    const geckoId = getCoingeckoId(coinId);
    const response = await axios.get(
      `https://api.coingecko.com/api/v3/coins/${geckoId}/market_chart`,
      {
        params: {
          vs_currency: 'usd',
          days: 30,
        },
      }
    );
    return response.data.prices;
  } catch (error) {
    console.error(`Error fetching data from CoinGecko for ${coinId}:`, error);
    throw error;
  }
};

const createChart = async (data, coinName) => {
  if (chart) {
    chart.destroy();
  }

  await nextTick(); // DOM 업데이트를 기다립니다.

  if (!chartCanvas.value) {
    console.error('Chart canvas is not available');
    error.value = '차트를 그릴 수 없습니다. 캔버스 요소가 없습니다.';
    return;
  }

  const ctx = chartCanvas.value.getContext('2d');
  if (!ctx) {
    console.error('Failed to get 2D context from canvas');
    error.value = '차트 컨텍스트를 가져올 수 없습니다.';
    return;
  }

  try {
    chart = new Chart(ctx, {
      type: 'line',
      data: {
        labels: data.map((item) => new Date(item[0]).toLocaleDateString()),
        datasets: [
          {
            label: `${coinName} Price (USD)`,
            data: data.map((item) => item[1]),
            borderColor: 'rgb(75, 192, 192)',
            tension: 0.1,
          },
        ],
      },
      options: {
        responsive: true,
        scales: {
          x: {
            display: true,
            title: {
              display: true,
              text: 'Date',
            },
          },
          y: {
            display: true,
            title: {
              display: true,
              text: 'Price (USD)',
            },
          },
        },
      },
    });
  } catch (err) {
    console.error('Error creating chart:', err);
    error.value = `차트 생성 중 오류가 발생했습니다: ${err.message}`;
  }
};

const updateChart = async () => {
  error.value = null;
  chartData.value = [];
  try {
    const data = await fetchCoinGeckoData(props.coinId);
    if (data && data.length > 0) {
      chartData.value = data;
      await createChart(data, props.coinId.split('-')[1].toUpperCase());
    } else {
      error.value = '이 코인에 대한 데이터를 찾을 수 없습니다';
    }
  } catch (err) {
    console.error('Error updating chart:', err);
    error.value = `차트 데이터를 불러오는데 실패했습니다: ${err.message}`;
  }
};

onMounted(async () => {
  await nextTick(); // DOM이 완전히 렌더링될 때까지 기다립니다.
  await updateChart();
});

watch(() => props.coinId, updateChart);
</script>

<style scoped>
.error-message {
  color: red;
  text-align: center;
}
</style>
