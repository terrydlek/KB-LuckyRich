<template>
  <div v-if="fund">
    <h1>{{ fund.name }}({{ fund.symbol }})</h1>
    <p><strong>국가:</strong> {{ fund.country }}</p>
    <p><strong>최근가:</strong> {{ fund.lastPrice }}</p>
    <p>
      <strong>변동률:</strong>
      <span
        :class="{
          'text-green-500': parseFloat(fund.changePercent) > 0,
          'text-red-500': parseFloat(fund.changePercent) < 0,
        }"
        >{{ fund.changePercent }}%</span
      >
    </p>
    <p><strong>총 자산:</strong> {{ fund.totalAssets }}</p>
    <p><strong>갱신 시간:</strong> {{ fund.lastUpdate }}</p>
    <p><strong>위험 등급:</strong> {{ fund.riskRating }}</p>

    <!-- <h2>가격 차트</h2>
    <canvas ref="chartCanvas"></canvas> -->
  </div>
  <div v-else-if="error">
    <p>오류 발생: {{ error }}</p>
  </div>
  <div v-else>Loading...</div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRoute } from 'vue-router'; // vue-router에서 useRoute 가져오기

const route = useRoute(); // 현재 라우트 정보 가져오기
const url = decodeURIComponent(route.params.encodedUrl); // URL 파라미터에서 encodedUrl 가져오기

const fund = ref(null);
const error = ref(null);

const fetchFundDetail = async () => {
  try {
    // URL에서 /funds/ 부분을 제거하고 요청
    const cleanUrl = url.replace(/^\/funds/, '');
    const response = await axios.get(
      `http://localhost:8080/recommend/funds${cleanUrl}`
    );
    fund.value = response.data;
  } catch (error) {
    console.error('펀드 상세 정보를 불러오는 데 실패했습니다.', error);
  }
};

onMounted(fetchFundDetail);
</script>
