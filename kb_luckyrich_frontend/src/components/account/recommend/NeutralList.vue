<template>
  <h2>
    당신의 투자 성향은 안정추구형입니다. 채권형 펀드 상품을 추천해드릴게요.
  </h2>
  <div v-if="loading">데이터를 불러오는 중...</div>
  <div v-else-if="error">{{ error }}</div>
  <table v-else-if="funds.length">
    <thead>
      <tr>
        <th>국가</th>
        <th>펀드명</th>
        <th>심볼</th>
        <th>최근가</th>
        <th>변동률</th>
        <th>총 자산</th>
        <th>갱신 시간</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="fund in funds" :key="fund.url">
        <td>{{ fund.country }}</td>
        <td>
          <a :href="`/fund/${encodeURIComponent(fund.url)}`">{{ fund.name }}</a>
        </td>
        <td>{{ fund.symbol }}</td>
        <td>{{ formatNumber(fund.lastPrice) }}</td>
        <td
          :class="{
            'text-green-500': parseFloat(fund.changePercent) > 0,
            'text-red-500': parseFloat(fund.changePercent) < 0,
          }"
        >
          {{ fund.changePercent }}
        </td>
        <td>{{ formatNumber(fund.totalAssets) }}</td>
        <td>{{ fund.lastUpdate }}</td>
      </tr>
    </tbody>
  </table>
  <div v-else>표시할 펀드 데이터가 없습니다.</div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const funds = ref([]);
const loading = ref(true);
const error = ref(null);

const fetchFunds = async () => {
  try {
    loading.value = true;
    const response = await axios.get(
      'http://localhost:8080/api/funds?riskRating=3'
    );
    funds.value = response.data;
  } catch (err) {
    console.error('펀드 데이터를 불러오는 데 실패했습니다.', err);
    error.value =
      '펀드 데이터를 불러오는 데 실패했습니다. 잠시 후 다시 시도해주세요.';
  } finally {
    loading.value = false;
  }
};

const formatNumber = (value) => {
  if (!value) return '-';
  return new Intl.NumberFormat('ko-KR').format(
    parseFloat(value.replace(/,/g, ''))
  );
};

const encodeUrl = (url) => {
  return encodeURIComponent(url);
};

onMounted(fetchFunds);
</script>

<style scoped>
table {
  width: 100%;
  border-collapse: collapse;
}
th,
td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}
th {
  background-color: #f2f2f2;
}
.text-green-500 {
  color: green;
}
.text-red-500 {
  color: red;
}
.external-link {
  margin-left: 5px;
  font-size: 0.8em;
  color: #666;
}
</style>
