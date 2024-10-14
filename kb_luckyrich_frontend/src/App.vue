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
    <!-- <RightSidebar /> -->
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import Navbar from './components/layout/Navbar.vue';
import LeftSidebar from './components/layout/LeftSidebar.vue';
import RightSidebar from './components/layout/RightSidebar.vue';
import '@fortawesome/fontawesome-free/css/all.css'; // Font Awesome 스타일 추가
</script>

<style scoped>
.layout-container {
  display: grid;
  grid-template-columns: 2fr 7fr;
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
</style>
