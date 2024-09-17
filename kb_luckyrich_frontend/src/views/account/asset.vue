<template>
  <div class="dashboard-container">
    <!-- 상단 타이틀과 설명 -->
    <div class="header">
      <h1>내 자산을 쉽고 똑똑하게 관리하세요.</h1>
      <router-link to="/asset/accountupdate">자산 등록 또는 수정</router-link>
    </div>

    <!-- 카드형 데이터 박스 -->
    <div class="data-cards">
      <div class="card">
        <p>지난 주 review</p>
        <p class="highlight">10.6% 성장했어요.</p>
      </div>
      <div class="card">
        <p>나의 총 자산</p>
        <p class="amount">12,345,600원</p>
      </div>
      <div class="card">
        <p>나의 투자 금액</p>
        <p class="amount">9,762,924원</p>
      </div>
      <div class="card">
        <p>투자 성과 순위</p>
        <p class="highlight">상위 16%</p>
      </div>
    </div>

    <!-- 차트 섹션 -->
    <div>
      <div class="chart-row">
        <section><totalChart /></section>
        <section><goalChart /></section>
        <section><assetGraph /></section>
      </div>
    </div>
    <div>
      <div class="chart-row">
        <section><account-book-chart /></section>
        <section><consumptionstatus /></section>
        <section><assetcomparison /></section>
      </div>
    </div>
  </div>
</template>

<script>
import totalChart from "@/components/account/chart/totalChart.vue";
import goalChart from "@/components/account/chart/goalChart.vue";
import assetGraph from "@/components/account/chart/assetGraph.vue";
import accountBookChart from "@/components/account/chart/accountBookChart.vue";
import assetcomparison from "@/components/account/chart/assetComparison.vue";
import consumptionstatus from "@/components/account/chart/consumptionstatus.vue";

export default {
  components: {
    totalChart,
    goalChart,
    assetGraph,
    accountBookChart,
    consumptionstatus,
    assetcomparison,
  },
};
</script>

<style scoped>
/* 상단 타이틀 */
.header {
  text-align: center;
  margin-bottom: 30px;
}

.header h1 {
  font-size: 2.5em;
  margin-bottom: 10px;
}

.header a {
  display: inline-block;
  margin-top: 10px;
  font-size: 1.2em;
  color: #007bff;
}

/* 데이터 카드 */
.data-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 40px;
}

.card {
  background-color: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  text-align: center;
}

.card .amount {
  font-size: 1.5em;
  font-weight: bold;
  color: #343a40;
}

.card .highlight {
  font-size: 1.2em;
  color: #28a745;
}

/* 차트 섹션 */
.chart-section {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.chart-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.chart-row section {
  flex: 1;
  background-color: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.chart-row section:not(:last-child) {
  margin-right: 20px;
}
</style>
