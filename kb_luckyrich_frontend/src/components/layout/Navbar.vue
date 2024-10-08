<template>
  <nav class="navbar">
    <div class="navbar-inner">
      <div class="navbar-left">
        <router-link to="/luckyrich" class="logo">LuckyRich</router-link>
      </div>
      <div class="navbar-right">
        <button v-if="isLoggedIn" @click="goToMyPage" class="btn my-page">
          My Page
        </button>
        <button @click="handleAuth" class="btn auth">
          {{ isLoggedIn ? 'Logout' : 'Login' }}
        </button>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';

const isLoggedIn = ref(false);
const router = useRouter();
const route = useRoute();

// onMounted(() => {
//   checkLoginStatus();
// });

watch(route, () => {
  checkLoginStatus();
});

function checkLoginStatus() {
  const token = localStorage.getItem('access_token');
  isLoggedIn.value = !!token; // Update the login status
}

const handleAuth = () => {
  if (isLoggedIn.value) {
    localStorage.removeItem('access_token');
    alert('로그아웃 되었습니다.');
    isLoggedIn.value = false; // Immediately update login status
    router.push('/');
  } else {
    router.push({ name: 'login' });
  }
};

const goToMyPage = () => {
  router.push('/luckyrich/userUpdate');
};
</script>

<style scoped>
.navbar {
  width: 100%; /* 전체 화면 너비 */
  background-color: #ffffff;
  display: flex;
  justify-content: center; /* 중앙 정렬 */
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1000;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.navbar-inner {
  max-width: 1200px; /* 콘텐츠와 동일한 최대 너비 */
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 80px;
  padding-left: 0; /* 왼쪽 패딩 제거 */
  padding-right: 0; /* 오른쪽 패딩 제거 */
}

.navbar-left .logo {
  font-family: 'Pretendard', sans-serif;
  font-weight: 700;
  font-size: 24px;
  color: #f8b400;
  text-decoration: none;
}

.navbar-right {
  display: flex;
  gap: 15px;
}

.btn {
  font-family: 'Pretendard', sans-serif;
  font-weight: 600;
  font-size: 16px;
  color: #ffffff;
  background-color: #f8b400;
  padding: 10px 20px;
  border: none;
  border-radius: 25px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.btn:hover {
  background-color: #e0a800;
}

.auth {
  background-color: #3498db;
}

.auth:hover {
  background-color: #2980b9;
}

@media (max-width: 768px) {
  .navbar-inner {
    flex-direction: column;
    padding: 10px;
  }
}
</style>
