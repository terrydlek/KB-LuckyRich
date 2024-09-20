<template>
  <div class="sidebar">
    <nav>
      <ul>
        <li>
          <router-link :to="{ name: 'SteadinessList' }">예적금 상품 보러 가기</router-link>
        </li>
        <li>
          <router-link :to="{ name: 'ConservativeList' }"
            >채권형 펀드 상품 보러 가기</router-link
          >
        </li>
        <li>
          <router-link :to="{ name: 'Neutrility' }">재간접 펀드 상품 보러 가기</router-link>
        </li>
        <li>
          <router-link :to="{ name: 'ActiveList' }">주식 상품 보러 가기</router-link>
        </li>
        <li>
          <router-link :to="{ name: 'AggresiveList' }">암호화폐 상품 보러 가기</router-link>
        </li>
        <li>
          <router-link :to="{ name: 'recommendation' }"
            >테스트 다시 받기</router-link
          >
        </li>
      </ul>
    </nav>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";
const router = useRouter();
</script>

<style scoped>
.sidebar {
  background-color: #f5f5f5;
  padding: 1rem;
  text-align: center;
}

nav ul {
  list-style-type: none;
  padding: 0;
}

nav ul li {
  display: inline;
  margin: 0 15px;
}

nav ul li a {
  text-decoration: none;
  color: #007bff;
  font-weight: bold;
}

nav ul li a:hover {
  color: #0056b3;
}
</style>
