<template>
  <div>
    <h1>내 주식 정보 받아오기</h1>
    <ul>
      <li v-for="(stock, index) in stocks" :key="index">
        <p><strong>주식 명:</strong> {{ stock.investSymbol }}</p>
        <p><strong>구매 날짜:</strong> {{ formatDate(stock.investDate) }}</p>
        <p><strong>구매 당시 가격:</strong> {{ formatPrice(stock.investPrice) }}</p>
        <hr />
      </li>
    </ul>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      stocks: [],
    };
  },
  mounted() {
    axios
      .get("http://localhost:8080/myasset/getMyStock")
      .then((response) => {
        console.log(response.data);
        this.stocks = response.data;
      })
      .catch((error) => {
        console.error("There was an error fetching the stocks:", error);
      });
  },
  methods: {
    // 날짜 형식 변환 함수
    formatDate(date) {
      const options = { year: "numeric", month: "short", day: "numeric" };
      return new Date(date).toLocaleDateString(undefined, options);
    },
    // 가격 형식 변환 함수 (천 단위 구분 쉼표)
    formatPrice(price) {
      return price.toLocaleString();
    },
  },
};
</script>

<style scoped>
h1 {
  color: #333;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  margin-bottom: 20px;
}

hr {
  border: none;
  border-top: 1px solid #eee;
}
</style>
