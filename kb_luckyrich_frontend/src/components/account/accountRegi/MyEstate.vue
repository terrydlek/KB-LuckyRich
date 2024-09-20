<template>
  <div>
    <h1>내 부동산 정보 받아오기</h1>
    <ul>
      <li v-for="(estate, index) in estates" :key="index">
        <p><strong>부동산 이름:</strong> {{ estate.realEstateName }}</p>
        <p>
          <strong>부동산 가격:</strong>
          {{ formatPrice(estate.realEstatePrice) }}
        </p>
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
      estates: [],
    };
  },
  mounted() {
    axios
      .get("http://localhost:8080/myasset/getMyEstate")
      .then((response) => {
        console.log(response.data);
        this.estates = response.data;
      })
      .catch((error) => {
        console.error("There was an error fetching the estates:", error);
      });
  },
  methods: {
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
