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
        <li
          @mouseover="toggleSubmenu('luckyRich', true)"
          @mouseleave="toggleSubmenu('luckyRich', false)"
        >
          LuckyRich
          <ul
            v-show="isSubmenuVisible.luckyRich"
            class="submenu"
            :class="{ show: isSubmenuVisible }"
          >
            <li @click="goTo('about')">About</li>
            <li @click="goTo('privacyPolicy')">Privacy Policy</li>
            <li @click="goTo('termsOfService')">Terms of Service</li>
          </ul>
        </li>
        <li @click="goTo('asset')">한눈에 내 자산 확인하기</li>
        <li @click="goTo('test')">나에게 맞는 투자 상품은?</li>
        <li @click="goTo('accountBook')">거래 내역 조회</li>
        <li @click="goTo('realestate')">부동산</li>
        <li @click="goTo('financenews')">금융 뉴스</li>
        <li
          @mouseover="toggleSubmenu('qa', true)"
          @mouseleave="toggleSubmenu('qa', false)"
        >
          Q & A
          <ul
            v-show="isSubmenuVisible.qa"
            class="submenu"
            :class="{ show: isSubmenuVisible }"
          >
            <li @click="goTo('qa')">자주 묻는 질문</li>
            <li @click="goTo('PostList')">게시판</li>
          </ul>
        </li>
      </ul>
    </nav>
  </div>
</template>
<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const isSubmenuVisible = ref({
  luckyRich: false,
  qa: false,
});

// 클릭 시 해당 경로로 이동하는 함수
const goTo = (route) => {
  router.push({ name: route });
};

// Q&A 서브메뉴 표시 및 숨김 제어
const toggleSubmenu = (menu, isVisible) => {
  isSubmenuVisible.value[menu] = isVisible;
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
  margin: 0;
}

.category-list ul li {
  padding: 10px 8px;
  border-bottom: 1px solid #eee;
  cursor: pointer;
  transition: color 0.3s, background-color 0.3s;
}

.category-list ul li:hover {
  color: #3498db;
  /* background-color: #f0f0f0; */
}

.category-list ul li:last-child {
  border-bottom: 1px solid transparent; /* 마지막 줄에 불필요한 줄 제거 */
}

.category-list ul li:last-child::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 1px;
}

.submenu {
  list-style-type: none;
  padding-left: 20px;
  margin-top: 10px;
  opacity: 0; /* 처음엔 보이지 않게 설정 */
  transform: scaleY(0); /* 스케일을 0으로 해서 접혀있는 효과 */
  transform-origin: top; /* 위쪽에서 아래로 펼쳐지도록 설정 */
  transition: opacity 0.5s ease, transform 0.5s ease; /* 트랜지션 적용 */
}

.submenu.show {
  opacity: 1;
  transform: scaleY(1);
}

.submenu li {
  color: rgb(134, 134, 134);
  padding: 8px 0; /* 위아래 패딩을 균일하게 설정 */
  margin: 0;
  cursor: pointer;
  transition: color 0.3s;
  text-decoration: none;
  line-height: 1.5; /* 텍스트 높이를 고르게 설정 */
}

.submenu li:hover {
  color: #3498db;
  text-decoration: none;
}

.submenu li:last-child {
  text-decoration: none;
  margin-bottom: 0; /* 마지막 게시판 항목 아래 불필요한 공간 제거 */
}
</style>
