<template>
  <div class="left-sidebar">
    <nav class="category-list">
      <h4>Category</h4>
      <ul>
        <!-- LuckyRich Main Menu -->
        <li
          @mouseenter="showSubmenu('luckyRich')"
          @mouseleave="hideSubmenu('luckyRich', false)"
          :class="{ bold: isLuckyRichSelected }"
        >
          LuckyRich
          <ul
            :class="[
              'submenu',
              { show: isSubmenuVisible.luckyRich || isLuckyRichSelected },
            ]"
            @mouseleave="hideSubmenu('luckyRich', true)"
          >
            <li
              @click="goTo('about')"
              :class="{ bold: selectedCategory === 'about' }"
            >
              About
            </li>
            <li
              @click="goTo('privacyPolicy')"
              :class="{ bold: selectedCategory === 'privacyPolicy' }"
            >
              Privacy Policy
            </li>
            <li
              @click="goTo('termsOfService')"
              :class="{ bold: selectedCategory === 'termsOfService' }"
            >
              Terms of Service
            </li>
          </ul>
        </li>

        <li
          @click="goTo('asset')"
          :class="{ bold: selectedCategory === 'asset' }"
        >
          한눈에 내 자산 확인하기
        </li>
        <li
          @click="goTo('test')"
          :class="{ bold: selectedCategory === 'test' }"
        >
          나에게 맞는 투자 상품은?
        </li>
        <li
          @click="goTo('accountBook')"
          :class="{ bold: selectedCategory === 'accountBook' }"
        >
          거래 내역 조회
        </li>
        <li
          @click="goTo('realestate')"
          :class="{ bold: selectedCategory === 'realestate' }"
        >
          부동산
        </li>

        <li @click="toggleSubmenu('news')">
          뉴스
          <ul :class="['submenu', { show: isSubmenuVisible.news }]">
            <li
              @click="goTo('FinanceNews')"
              :class="{ bold: selectedCategory === 'FinanceNews' }"
            >
              금융
            </li>
            <li
              @click="goTo('EstateNews')"
              :class="{ bold: selectedCategory === 'EstateNews' }"
            >
              부동산
            </li>
            <li
              @click="goTo('SecuritiesNews')"
              :class="{ bold: selectedCategory === 'SecuritiesNews' }"
            >
              증권
            </li>
            <li
              @click="goTo('PersonalNews')"
              :class="{ bold: selectedCategory === 'PersonalNews' }"
            >
              생활경제
            </li>
            <li
              @click="goTo('GlobalEconomyNews')"
              :class="{ bold: selectedCategory === 'GlobalEconomyNews' }"
            >
              글로벌 경제
            </li>
            <li
              @click="goTo('EconomyNews')"
              :class="{ bold: selectedCategory === 'EconomyNews' }"
            >
              경제 일반
            </li>
          </ul>
        </li>

        <!-- Q & A Main Menu -->
        <li
          @mouseenter="showSubmenu('qa')"
          @mouseleave="hideSubmenu('qa', false)"
          :class="{ bold: isQaSelected }"
        >
          Q & A
          <ul
            :class="['submenu', { show: isSubmenuVisible.qa || isQaSelected }]"
            @mouseleave="hideSubmenu('qa', true)"
          >
            <li
              @click="goTo('qa')"
              :class="{ bold: selectedCategory === 'qa' }"
            >
              자주 묻는 질문
            </li>
            <li
              @click="goTo('PostList')"
              :class="{ bold: selectedCategory === 'PostList' }"
            >
              게시판
            </li>
          </ul>
        </li>

        <!-- Admin menu -->
        <li
          v-if="isAdmin"
          @click="goTo('adminBoard')"
          :class="{ bold: selectedCategory === 'adminBoard' }"
        >
          서비스 관리
        </li>
      </ul>
    </nav>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const isAdmin = ref(false);
const selectedCategory = ref('');
const isSubmenuVisible = ref({
  luckyRich: false,
  qa: false,
  news: false,
});

const resetCategoryMenu = () => {
  selectedCategory.value = ''; // 선택된 카테고리 초기화
  isSubmenuVisible.value = {
    luckyRich: false,
    qa: false,
  };
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

// 메뉴 항목 클릭 시 해당 경로로 이동
const goTo = (route) => {
  selectedCategory.value = route; // 선택한 메뉴로 갱신
  // 관리자 페이지 접근 시 관리자 권한 확인
  if (route === 'adminBoard' && !isAdmin.value) {
    alert('관리자만 접근 가능한 페이지입니다.');
    return;
  }

  const accessToken = localStorage.getItem('access_token');
  const restrictedRoutes = ['asset', 'test', 'accountBook', 'PostList'];

  // 인증 필요 경로 접근 시 로그인 화면으로 이동
  if (restrictedRoutes.includes(route) && !accessToken) {
    router.push({ name: 'login' });
  } else {
    router.push({ name: route }); // 선택한 메뉴로 라우팅
  }
};

// 서브메뉴 표시
const showSubmenu = (menu) => {
  isSubmenuVisible.value[menu] = true;
};

// 서브메뉴 숨기기
const hideSubmenu = (menu, immediate) => {
  if (immediate) {
    setTimeout(() => {
      isSubmenuVisible.value[menu] = false;
    }, 300);
  } else {
    isSubmenuVisible.value[menu] = false;
  }
};

// 페이지가 마운트될 때 관리자 상태 확인 후 메뉴 렌더링
// onMounted(async () => {
//   await checkAdminStatus();
// });
</script>

<style scoped>
h4 {
  margin: 10px 5px;
  font-weight: bold;
}

.left-sidebar {
  display: flex;
  flex-direction: column;
  gap: 20px;
  background-color: #ffffff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.category-list ul {
  list-style-type: none;
  font-size: 20px;
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
}

.submenu {
  list-style-type: none;
  padding-left: 20px;
  margin-top: 10px;
  max-height: 0;
  overflow: hidden;
  opacity: 0;
  transition: max-height 0.5s ease, opacity 0.5s ease;
}

.submenu.show {
  max-height: 300px;
  opacity: 1;
}

.submenu li {
  color: rgb(134, 134, 134);
  padding: 8px 0;
  margin: 0;
  cursor: pointer;
  transition: color 0.3s;
}

.submenu li:hover {
  color: #3498db;
}

.bold {
  font-weight: bold;
  font-size: 20px;
}
</style>
