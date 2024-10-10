<template>
  <div :class="['layout-container', { 'dark-mode': isDarkMode }]">
    <!-- 상단 네비게이션 바 -->
    <navbar />

    <!-- 왼쪽 사이드바 -->
    <LeftSidebar />

    <!-- 가운데 메인 콘텐츠 (라우터 뷰가 들어감) -->
    <main class="main-content">
      <router-view />
    </main>

    <!-- 오른쪽 사이드바 -->
    <RightSidebar />

    <!-- 다크 모드 토글 버튼 -->
    <button class="dark-mode-toggle" @click="toggleDarkMode">
      {{ isDarkMode ? '라이트 모드' : '다크 모드' }}
    </button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import Navbar from './components/layout/Navbar.vue';
import LeftSidebar from './components/layout/LeftSidebar.vue';
import RightSidebar from './components/layout/RightSidebar.vue';

// 다크 모드 상태 관리
const isDarkMode = ref(false);

// 다크 모드 토글 함수
const toggleDarkMode = () => {
  isDarkMode.value = !isDarkMode.value;
  localStorage.setItem('darkMode', isDarkMode.value);
  document.body.classList.toggle('dark-mode', isDarkMode.value);
};

// 페이지 로드 시 저장된 다크 모드 상태를 불러옴
onMounted(() => {
  isDarkMode.value = localStorage.getItem('darkMode') === 'true';
  document.body.classList.toggle('dark-mode', isDarkMode.value);
});
</script>

<style scoped>
.layout-container {
  display: grid;
  grid-template-columns: 1.3fr 6fr 1fr;
  gap: 20px;
  max-width: 1600px;
  margin: 50px auto; /* 왼쪽, 오른쪽 여백을 자동으로 추가 */
  padding-left: 20px; /* 왼쪽 여백 */
  padding-right: 20px; /* 오른쪽 여백 */
  min-height: 100vh;
}

.main-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  min-height: 100vh;
}

/* 왼쪽 사이드바와 오른쪽 사이드바도 중앙에 맞춰지도록 수정 */
.left-sidebar,
.right-sidebar {
  background-color: #ffffff;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  min-height: 100vh;
}

/* 다크 모드 토글 버튼 스타일 */
.dark-mode-toggle {
  position: fixed;
  bottom: 20px;
  right: 20px;
  background-color: var(--color-primary);
  color: var(--color-font);
  border: none;
  border-radius: 10px;
  padding: 10px 20px;
  cursor: pointer;
}
</style>
