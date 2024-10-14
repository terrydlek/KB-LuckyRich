<template lang="">
  <div class="left-sidebar">
    <nav class="category-list">
      <h4>Category</h4>
      <ul>
        <li @click="toggleSubmenu('luckyRich')">
          LuckyRich
          <ul :class="['submenu', { show: isSubmenuVisible.luckyRich }]">
            <li @click="goTo('about')" :class="{ bold: selectedCategory === 'about' }">About</li>
            <li @click="goTo('privacyPolicy')" :class="{ bold: selectedCategory === 'privacyPolicy' }">Privacy Policy</li>
            <li @click="goTo('termsOfService')" :class="{ bold: selectedCategory === 'termsOfService' }">Terms of Service</li>
          </ul>
        </li>

        <li @click="goTo('asset')" :class="{ bold: selectedCategory === 'asset' }">한눈에 내 자산 확인하기</li>
        <li @click="goTo('test')" :class="{ bold: selectedCategory === 'test' }">나에게 맞는 투자 상품은?</li>
        <li @click="goTo('accountBook')" :class="{ bold: selectedCategory === 'accountBook' }">거래 내역 조회</li>
        <li @click="goTo('realestate')" :class="{ bold: selectedCategory === 'realestate' }">부동산</li>
        <li @click="goTo('financenews')" :class="{ bold: selectedCategory === 'financenews' }">금융 뉴스</li>

        <li @click="toggleSubmenu('qa')">
          Q & A
          <ul :class="['submenu', { show: isSubmenuVisible.qa }]">
            <li @click="goTo('qa')" :class="{ bold: selectedCategory === 'qa' }">자주 묻는 질문</li>
            <li @click="goTo('PostList')" :class="{ bold: selectedCategory === 'PostList' }">게시판</li>
          </ul>
        </li>

        
        <li v-if="isAdmin" @click="goTo('adminBoard')" :class="{ bold: selectedCategory === 'adminBoard' }">서비스 관리</li>
      </ul>
    </nav>
  </div>
</template>


<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const isAdmin = ref(false); // 관리자 여부 확인용 변수
const selectedCategory = ref(''); // 현재 선택된 카테고리 저장
const isSubmenuVisible = ref({
  luckyRich: false,
  qa: false,
});

const getAccessToken = () => {
  return localStorage.getItem('access_token');
};

// const checkAdminStatus = async () => {
//   try {
//     const token = localStorage.getItem('access_token');
//     if (!token) {
//       console.error('토큰이 없습니다. 로그인이 필요합니다.');
//       return;
//     }

//     const response = await axios.get('http://localhost:8080/user/role', {
//       headers: {
//         Authorization: `Bearer ${token}`,
//       },
//     });

//     const userRole = response.data;
//     isAdmin.value = userRole === 'ADMIN'; // 관리자 여부 업데이트
//     localStorage.setItem('user_role', userRole); // 역할 정보를 로컬 스토리지에 저장
//   } catch (error) {
//     console.error('사용자 역할 확인 중 오류 발생:', error);
//     if (error.response && error.response.status === 401) {
//       localStorage.removeItem('access_token');
//       localStorage.removeItem('user_role');
//       router.push('/luckyrich/login');
//     }
//   }
// };

const goTo = (route) => {
  selectedCategory.value = route;
  if (route === 'adminBoard' && !isAdmin.value) {
    alert('관리자만 접근 가능한 페이지입니다.');
    return;
  }
  const accessToken = getAccessToken();

  const restrictedRoutes = ['asset', 'test', 'accountBook', 'PostList'];

  if (restrictedRoutes.includes(route) && !accessToken) {
    router.push({ name: 'login' });
  } else {
    router.push({ name: route });
  }
};

const toggleSubmenu = (menu) => {
  isSubmenuVisible.value[menu] = !isSubmenuVisible.value[menu];
};


// 페이지가 마운트될 때 관리자 상태 확인 후 메뉴 렌더링
// onMounted(async () => {
//   await checkAdminStatus();
// });
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
  border-bottom: 1px solid transparent;
  /* 마지막 줄에 불필요한 줄 제거 */
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
  max-height: 0;
  /* 처음엔 보이지 않게 설정 */
  overflow: hidden;
  /* 내용이 넘칠 경우 숨기기 */
  opacity: 0;
  transition: max-height 0.5s ease, opacity 0.5s ease;
  /* 트랜지션 적용 */
}

.submenu.show {
  max-height: 300px;
  /* 적당한 높이 설정, 내용에 따라 달라질 수 있음 */
  opacity: 1;
}

.submenu li {
  color: rgb(134, 134, 134);
  padding: 8px 0;
  /* 위아래 패딩을 균일하게 설정 */
  margin: 0;
  cursor: pointer;
  transition: color 0.3s;
  text-decoration: none;
  line-height: 1.5;
  /* 텍스트 높이를 고르게 설정 */
}

.submenu li:hover {
  color: #3498db;
  text-decoration: none;
}

.submenu li:last-child {
  text-decoration: none;
  margin-bottom: 0;
  /* 마지막 게시판 항목 아래 불필요한 공간 제거 */
}

.bold {
  font-weight: bold;
  font-size: 20px;
}
</style>
