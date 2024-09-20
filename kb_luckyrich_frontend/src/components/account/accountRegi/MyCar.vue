<template>
  <div>
    <h1>My Cars</h1>
    <ul>
      <li v-for="(car, index) in cars" :key="index">
        <p><strong>Model:</strong> {{ car.carType }}</p>
        <p><strong>Price:</strong> {{ formatPrice(car.carAmount) }}</p>
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
      cars: [],
    };
  },
  mounted() {
    axios
      .get("http://localhost:8080/myasset/getMyCar")
      .then((response) => {
        console.log(response.data);
        this.cars = response.data;
      })
      .catch((error) => {
        console.error("There was an error fetching the cars:", error);
      });
  },
  methods: {
    // 가격 형식 변환 함수 (천 단위 구분 쉼표)
    formatPrice(price) {
      // price가 정의되어 있으면 toLocaleString을 호출, 그렇지 않으면 빈 문자열 반환
      return price ? price.toLocaleString() : "-";
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
