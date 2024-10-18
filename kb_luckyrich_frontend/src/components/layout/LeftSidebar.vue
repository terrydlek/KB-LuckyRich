<template>
  <div class="left-sidebar">
    <nav class="category-list">
      <h4>Category</h4>
      <ul>
        <li
          @mouseenter="showSubmenu('luckyRich')"
          @mouseleave="hideSubmenu('luckyRich', false)"
          :class="{ bold: isLuckyRichSelected }"
        >
          Lucky Rich
          <ul
            :class="[
              'submenu',
              { show: isSubmenuVisible.luckyRich || isAnyLuckyRichSelected },
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

        <li
          @mouseenter="showSubmenu('news')"
          @mouseleave="hideSubmenu('news', false)"
          :class="{ bold: isNewsSelected }"
        >
          뉴스
          <ul
            :class="[
              'submenu',
              { show: isSubmenuVisible.news || isAnyNewsSelected },
            ]"
            @mouseleave="hideSubmenu('news', true)"
          >
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

        <li
          @mouseenter="showSubmenu('qa')"
          @mouseleave="hideSubmenu('qa', false)"
          :class="{ bold: isQaSelected }"
        >
          Q & A
          <ul
            :class="[
              'submenu',
              { show: isSubmenuVisible.qa || isAnyQaSelected },
            ]"
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

        <li
          v-if="checkAdminRole()"
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
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const isAdmin = ref(false);
const selectedCategory = ref('');
const isSubmenuVisible = ref({
  luckyRich: false,
  qa: false,
  news: false,
});

const isAnyLuckyRichSelected = computed(() =>
  luckyRichCategories.includes(selectedCategory.value)
);
const isAnyNewsSelected = computed(() =>
  newsCategories.includes(selectedCategory.value)
);
const isAnyQaSelected = computed(() =>
  qaCategories.includes(selectedCategory.value)
);

const luckyRichCategories = ['about', 'privacyPolicy', 'termsOfService'];
const newsCategories = [
  'FinanceNews',
  'EstateNews',
  'SecuritiesNews',
  'PersonalNews',
  'GlobalEconomyNews',
  'EconomyNews',
];
const qaCategories = ['qa', 'PostList'];

const isLuckyRichSelected = ref(false);
const isNewsSelected = ref(false);
const isQaSelected = ref(false);

const goTo = (route) => {
  selectedCategory.value = route;

  const accessToken = localStorage.getItem('access_token');
  const restrictedRoutes = ['asset', 'test', 'accountBook', 'PostList'];

  if (restrictedRoutes.includes(route) && !accessToken) {
    router.push({ name: 'login' });
  } else {
    router.push({ name: route });
  }
};

const checkAdminRole = () => {
  return localStorage.getItem('user_role') === 'ADMIN';
};

const showSubmenu = (menu) => {
  isSubmenuVisible.value[menu] = true;
};

const hideSubmenu = (menu, immediate) => {
  if (immediate) {
    setTimeout(() => {
      isSubmenuVisible.value[menu] = false;
    }, 300);
  } else {
    isSubmenuVisible.value[menu] = false;
  }
};
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
