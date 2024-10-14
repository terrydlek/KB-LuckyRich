<template>
  <div :class="['layout-container', { 'dark-mode': isDarkMode }]">
    <!-- 상단 네비게이션 바 -->
    <Navbar @resetMenu="resetCategoryMenu" />

    <!-- 왼쪽 사이드바 -->
    <LeftSidebar ref="sidebarRef" />

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
import LoadingOverlay from './assets/LoadingOverlay.vue';
import '@fortawesome/fontawesome-free/css/all.css'; // Font Awesome 스타일 추가

const isLoading = ref(false);
const isDarkMode = ref(false); // 다크 모드 상태 관리

// LeftSidebar에 접근하기 위한 ref
const sidebarRef = ref(null);
const router = useRouter();

// 로고 클릭 시 LeftSidebar의 메뉴를 초기화하는 함수
const resetCategoryMenu = () => {
  if (sidebarRef.value) {
    sidebarRef.value.resetCategoryMenu(); // LeftSidebar의 카테고리 초기화 함수 호출
  }
};

// 페이지 이동 시 로딩 상태를 간단히 변경하는 함수
router.beforeEach(() => {
  isLoading.value = true;
});

router.afterEach(() => {
  setTimeout(() => {
    isLoading.value = false;
  }, 300); // 적절한 딜레이 적용
});

// 다크 모드 상태를 로컬 스토리지에서 불러와 설정
onMounted(() => {
  isDarkMode.value = localStorage.getItem('darkMode') === 'true';
});
</script>

<style scoped>
.layout-container {
  display: grid;
  grid-template-columns: 2fr 7fr;
  gap: 20px;
  max-width: 1600px;
  margin: 50px auto;
  padding-left: 20px;
  padding-right: 20px;
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

/* 다크 모드 스타일 */
.dark-mode .layout-container {
  background-color: #333;
  color: #fff;
}

.dark-mode .main-content,
.dark-mode .left-sidebar,
.dark-mode .right-sidebar {
  background-color: #444;
  color: #fff;
}
</style>
