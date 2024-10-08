<template>
  <div class="home-container">
    <main class="main-content">
      <div class="chart-section">
        <h4>Graph</h4>
        <component :is="currentChart" v-if="currentChart" />
        <div v-else>
          <p>차트를 불러오는 중입니다...</p>
        </div>
        <button @click="prevChart">←</button>
        <button @click="nextChart">→</button>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import totalChart from '@/components/account/chart/totalChart.vue';
import goalChart from '@/components/account/chart/goalChart.vue';
import assetGraph from '@/components/account/chart/assetGraph.vue';
import accountBookChart from '@/components/account/chart/accountBookChart.vue';
import assetcomparison from '@/components/account/chart/assetComparison.vue';
import consumptionstatus from '@/components/account/chart/consumptionstatus.vue';

const charts = [
  totalChart,
  goalChart,
  assetGraph,
  accountBookChart,
  assetcomparison,
  consumptionstatus,
];
const currentIndex = ref(0);
const currentChart = ref(charts[currentIndex.value]);

const nextChart = () => {
  currentIndex.value = (currentIndex.value + 1) % charts.length;
  currentChart.value = charts[currentIndex.value];
};

const prevChart = () => {
  currentIndex.value = (currentIndex.value - 1 + charts.length) % charts.length;
  currentChart.value = charts[currentIndex.value];
};
</script>

<style scoped>
/* 전체 레이아웃 */
.home-container {
  display: grid;
  grid-template-columns: 1fr 2fr 1fr;
  gap: 20px;
  padding: 20px;
  background-color: #f9f9f9;
}

.image-section {
  text-align: center;
}

.main-image {
  width: 100%;
  height: auto;
  border-radius: 10px;
}

.image-caption {
  margin-top: 10px;
  font-size: 14px;
  color: #666;
}

.progress-section ul {
  list-style-type: none;
  padding: 0;
}

.chart-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 10px;
  width: 100%;
}

.chart-section button {
  background-color: #2ecc71;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.chart-section button:hover {
  background-color: #27ae60;
}
</style>
