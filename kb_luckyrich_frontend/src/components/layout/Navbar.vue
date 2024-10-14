<template>
  <nav class="navbar">
    <div class="navbar-inner">
      <div class="navbar-left">
        <a @click="handleLogoClick" class="logo">
          <img
            src="/src/assets/images/LuckyRichLogo.png"
            alt="LuckyRich Logo"
            class="logo-image"
          />
        </a>
      </div>
      <div class="navbar-right">
        <button v-if="isLoggedIn" @click="goToMyPage" class="btn my-page">
          My Page
        </button>
        <button @click="handleAuth" class="btn auth">
          {{ isLoggedIn ? 'Logout' : 'Login' }}
        </button>
        <!-- 다크 모드 토글 버튼 (아이콘 추가) -->
      </div>
    </div>
  </nav>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import '@fortawesome/fontawesome-free/css/all.css'; // Font Awesome 스타일 추가

const isLoggedIn = ref(false);
const router = useRouter();
const route = useRoute();
const isDarkMode = ref(false);

// 다크 모드 토글 함수
const toggleDarkMode = () => {
  isDarkMode.value = !isDarkMode.value;
  localStorage.setItem('darkMode', isDarkMode.value);
  document.body.classList.toggle('dark-mode', isDarkMode.value);
};

watch(route, () => {
  checkLoginStatus();
});

function checkLoginStatus() {
  const token = localStorage.getItem('access_token');
  isLoggedIn.value = !!token;
}

const handleAuth = () => {
  if (isLoggedIn.value) {
    localStorage.removeItem('access_token');
    localStorage.clear();
    alert('로그아웃 되었습니다.');
    isLoggedIn.value = false;
    checkLoginStatus();
    router.push('/').then(() => {
      router.go(0); // 페이지를 강제로 새로고침하여 상태 반영
    });
  } else {
    router.push({ name: 'login' });
  }
};

const goToMyPage = () => {
  router.push('/luckyrich/userUpdate');
};

// 로고 클릭 시 경로 설정
const handleLogoClick = () => {
  if (isLoggedIn.value) {
    router.push('/luckyrich');
  } else {
    router.push('/');
  }
};

// 페이지 로드 시 저장된 다크 모드 상태를 불러옴
onMounted(() => {
  isDarkMode.value = localStorage.getItem('darkMode') === 'true';
  document.body.classList.toggle('dark-mode', isDarkMode.value);
});
</script>

<style scoped>
.navbar {
  width: 100%;
  background-color: #ffffff;
  display: flex;
  justify-content: center;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1001;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.navbar-inner {
  max-width: 1200px;
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 80px;
  padding-left: 0;
  padding-right: 0;
  gap: 20px;
}

.navbar-left .logo {
  font-family: 'Pretendard', sans-serif;
  font-weight: 500;
  font-size: 30px;
  color: #f8b400;
  text-decoration: none;
  cursor: pointer;
}

/* 다크 모드 토글 버튼 스타일 */
.dark-mode-toggle {
  /* position: fixed;
  bottom: 20px;
  right: 20px; */
  background-color: var(--color-primary);
  color: var(--color-font);
  border: none;
  border-radius: 50px;
  padding: 10px 20px;
  cursor: pointer;
  font-size: 20px; /* 아이콘 크기 조절 */
}

.logo-image {
  width: 170px; /* 원하는 크기로 설정 */
  height: auto;
  cursor: pointer; /* 손가락 모양 커서 */
}

.logo:hover {
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
  background-color: #ffd232;
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
