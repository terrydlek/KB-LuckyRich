<template>
  <div :class="['layout-container', { 'dark-mode': isDarkMode }]">
    <!-- 상단 네비게이션 바 -->
    <navbar />

    <!-- 왼쪽 사이드바 -->
    <LeftSidebar />

    <!-- 가운데 메인 콘텐츠 (라우터 뷰가 들어감) -->
    <main class="main-content">
      <!-- 로딩 중일 때 로딩 오버레이를 표시 -->
      <LoadingOverlay :visible="isLoading" />
      <!-- 로딩이 끝나면 콘텐츠 표시 -->
      <router-view v-if="!isLoading" />
    </main>

    <!-- 오른쪽 사이드바 -->
    <!-- <RightSidebar /> -->
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import Navbar from './components/layout/Navbar.vue';
import LeftSidebar from './components/layout/LeftSidebar.vue';
import LoadingOverlay from './assets/LoadingOverlay.vue'; // 로딩 화면 컴포넌트 추가
import '@fortawesome/fontawesome-free/css/all.css'; // Font Awesome 스타일 추가

const isLoading = ref(false);

const router = useRouter();

// 페이지 이동 시 로딩 상태를 간단히 변경하는 함수
router.beforeEach(() => {
  isLoading.value = true; // 페이지 이동 시작할 때 로딩 시작
});

router.afterEach(() => {
  setTimeout(() => {
    isLoading.value = false; // 페이지 이동 후 로딩 종료
  }, 500); // 적절한 딜레이 적용 가능
});
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

.left-sidebar,
.right-sidebar {
  background-color: #ffffff;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  min-height: 100vh;
}
</style>
