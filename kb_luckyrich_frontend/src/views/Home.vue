<template>
  <div class="home-container">
    <h1 class="main-heading">
      <span
        class="letter"
        v-for="(char, index) in headingText"
        :key="index"
        :style="{ 'animation-delay': index * 0.05 + 's' }"
      >
        {{ char === ' ' ? '\u00A0' : char }}
      </span>
    </h1>
    <main class="main-content">
      <div class="chart-section">
        <component
          :is="totalChart"
          class="rounded-chart"
          style="width: 430px"
        />
        <component
          :is="assetGraph"
          class="rounded-chart"
          style="width: 430px"
        />
      </div>

      <div class="chart-section">
        <component
          :is="consumptionstatus"
          class="rounded-chart"
          style="width: 430px"
        />
        <component
          :is="accountBookChart"
          class="rounded-chart"
          style="width: 430px"
        />
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

const headingText = '효율적인 자산 관리로 더 나은 미래를 설계하세요'.split('');

// const charts = [
//   totalChart,
//   goalChart,
//   assetGraph,
//   accountBookChart,
//   assetcomparison,
//   consumptionstatus,
// ];
// const currentIndex = ref(0);
// const currentChart = ref(charts[currentIndex.value]);

// const nextChart = () => {
//   currentIndex.value = (currentIndex.value + 1) % charts.length;
//   currentChart.value = charts[currentIndex.value];
// };

// const prevChart = () => {
//   currentIndex.value = (currentIndex.value - 1 + charts.length) % charts.length;
//   currentChart.value = charts[currentIndex.value];
// };
</script>

<style scoped>
/* 전체 레이아웃 */
.home-container {
  width: 100% !important;

  display: grid;
  /* grid-template-columns: 1fr 5fr 1fr; */
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
  justify-content: space-between;
  gap: 10px;
  width: 100%;
  margin-bottom: 50px;
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

.chart-section {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.rounded-chart {
  border-radius: 130px;
  /* 둥근 모서리 적용 */
  overflow: hidden;
  /* 둥근 모서리를 넘어가는 콘텐츠를 숨김 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  /* 살짝의 그림자 효과 */
}

.main-heading {
  text-align: center;
  font-size: 35px;
  color: #333;
  margin-bottom: 20px;
}

@keyframes fadeIn {
  0% {
    opacity: 0;
    transform: translateY(20px);
  }

  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

.letter {
  display: inline-block;
  opacity: 0;
  animation: fadeIn 0.3s forwards;
}
</style>
