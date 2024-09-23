<template>
  <h2>
    당신의 투자 성향은 위험 중립형입니다. 재간접 펀드 상품을 추천해드릴게요.
  </h2>
  <table v-if="funds.length">
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
      <tr v-for="fund in funds" :key="fund.symbol">
        <td>{{ fund.country }}</td>
        <td>{{ fund.name }}</td>
        <td>{{ fund.symbol }}</td>
        <td>{{ fund.lastPrice }}</td>
        <td
          :class="{
            green: fund.changePercent > 0,
            red: fund.changePercent < 0,
          }"
        >
          {{ fund.changePercent }}%
        </td>
        <td>{{ fund.totalAssets }}</td>
        <td>{{ fund.lastUpdate }}</td>
      </tr>
    </tbody>
  </table>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      funds: [],
    };
  },
  mounted() {
    this.fetchFunds();
  },
  methods: {
    async fetchFunds() {
      try {
        const response = await axios.get(
          'http://localhost:8080/api/funds?riskRating=3'
        );
        this.funds = response.data;
      } catch (error) {
        console.error('펀드 데이터를 불러오는 데 실패했습니다.', error);
      }
    },
  },
};
</script>
