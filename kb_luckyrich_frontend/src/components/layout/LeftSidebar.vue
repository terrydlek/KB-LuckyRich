<template lang="">
  <div class="left-sidebar">
    <div class="profile-card">
      <img
        src="../assets/images/profile.png"
        alt="Profile"
        class="profile-image"
      />
      <div class="profile-info">
        <h3>GRAVITY SKIN 29</h3>
      </div>
    </div>

    <nav class="category-list">
      <h4>Category</h4>
      <ul>
        <li @click="goTo('asset')">한눈에 내 자산 확인하기</li>
        <li @click="goTo('test')">나에게 맞는 투자 상품은?</li>
        <li @click="goTo('accountBook')">거래 내역 조회</li>
        <li @click="goTo('realestate')">부동산</li>
        <li @click="goTo('financenews')">금융 뉴스</li>
        <li @click="goTo('qa')">Q&A 게시판</li>
      </ul>
    </nav>
  </div>
</template>
<script setup>
import { useRouter } from 'vue-router'; // Vue Router 사용

const router = useRouter(); // Vue Router 사용

// 클릭 시 해당 경로로 이동하는 함수
const goTo = (route) => {
  router.push({ name: route });
};
</script>
<style scoped>
.left-sidebar {
  display: flex;
  flex-direction: column;
  gap: 20px;
  background-color: #ffffff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.profile-card {
  text-align: center;
}

.profile-image {
  width: 70px;
  height: 70px;
  border-radius: 50%;
  margin-bottom: 10px;
}

.profile-info h3 {
  font-size: 18px;
  font-weight: bold;
}

.category-list ul {
  list-style-type: none;
  padding: 0;
}

.category-list ul li {
  padding: 8px;
  border-bottom: 1px solid #eee;
  cursor: pointer;
  transition: color 0.3s;
}

.category-list ul li:hover {
  color: #3498db;
}
</style>
