<template>
  <button @click="goBack" class="back-button">목록으로</button>
  <div class="container mt-5">
    <div v-if="stockData">
      <div class="text-center mb-4">
        <h1>{{ stockData.stockName }}</h1>
      </div>
      <table class="table text-center">
        <tbody>
          <tr>
            <td colspan="2" class="align-middle">
              <span class="font-weight-bold">현재가</span>:
              {{ stockData.currentPrice }} 원
            </td>
            <td>전일 {{ stockData.prevClosingPrice }} 원</td>
            <td>고가 {{ stockData.highPrice }} 원</td>
            <td>거래량 {{ stockData.lowPrice }}</td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td>시가 {{ stockData.openingPrice }} 원</td>
            <td>저가 {{ stockData.lowerLimitPrice }} 원</td>
            <td>거래대금 {{ stockData.tradeValue }} 백만원</td>
          </tr>
          <tr>
            <td colspan="5">
              <img
                :src="stockData.chartImageUrl"
                class="img-fluid"
                alt="주식 차트"
                width="800px"
              />
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <StockTimeDetail />
    <center>
      <div class="mb-3">
        <button class="btn btn-primary" @click="goBack">목록으로</button>
      </div>
    </center>
  </div>
</template>

<script>
import axios from 'axios';
import StockTimeDetail from './StockTimeDetail.vue';

export default {
  data() {
    return {
      stockData: null, // 받아올 주식 데이터를 저장할 객체
    };
  },
  components: {
    StockTimeDetail,
  },
  mounted() {
    const stockCode = this.$route.params.stockCode;
    const token = localStorage.getItem('access_token');
    axios
      .get(`http://localhost:8080/recommend/active/${stockCode}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => {
        this.stockData = res.data;
      })
      .catch((err) => {
        console.log(err);
      });
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
  },
};
</script>

<style scoped>
.table td,
.table th {
  vertical-align: middle;
}

.table {
  margin-bottom: 20px;
}

.back-button {
  width: 10%;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 10px 20px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s ease;
}

.back-button:hover {
  background-color: #32d43ace;
}
</style>
