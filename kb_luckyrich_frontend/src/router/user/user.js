import Home from '@/views/Home.vue';
import About from '@/views/luckyRich/about.vue';
import PrivacyPolicy from '@/views/luckyRich/PrivacyPolicy.vue';
import Qa from '@/views/luckyRich/Qa.vue';
import TermsOfService from '@/views/luckyRich/TermsOfService.vue';
import Login from '@/views/user/login.vue';
import UserUpdate from '@/views/user/UserUpdate.vue';
import PostList from '@/components/account/board/PostList.vue';
// import BoardDetail from "@/components/account/board/PostdDetail.vue";
import PostUpdate from '@/components/account/board/PostUpdate.vue';
import PostCreate from '@/components/account/board/PostCreate.vue';
import PostDetail from '@/components/account/board/PostDetail.vue';
import AccountBook from '@/components/account/AccountBook.vue';
import AdminBoard from '@/views/luckyRich/AdminBoard.vue';
import NotFound from '@/views/luckyRich/NotFound.vue';
import FinanceNews from '@/components/news/FinanceNews.vue';
import EstateNews from '@/components/news/EstateNews.vue';
import SecuritiesNews from '@/components/news/SecuritiesNews.vue';
import PersonalNews from '@/components/news/PersonalNews.vue';
import GlobalEconomyNews from '@/components/news/GlobalEconomyNews.vue';
import EconomyNews from '@/components/news/EconomyNews.vue';

export default [
  {
    path: '/luckyrich',
    name: 'home',
    component: Home,
  },
  {
    path: '/luckyrich/login',
    name: 'login',
    component: Login,
  },
  {
    path: '/luckyrich/userUpdate',
    name: 'userUpdate',
    component: UserUpdate,
  },
  {
    path: '/',
    name: 'about',
    component: About,
  },
  {
    path: '/luckyrich/privacy',
    name: 'privacy',
    component: PrivacyPolicy,
  },
  {
    path: '/luckyrich/terms',
    name: 'terms',
    component: TermsOfService,
  },
  {
    path: '/luckyrich/qa',
    name: 'qa',
    component: Qa,
  },

  {
    path: '/luckyrich/qa/ask',
    name: 'PostList',
    component: PostList,
  },
  {
    path: '/luckyrich/qa/ask/edit/:boardNum',
    name: 'postUpdate',
    component: PostUpdate,
  },
  {
    path: '/luckyrich/qa/ask/create',
    name: 'postCreate',
    component: PostCreate,
  },
  {
    path: '/luckyrich/qa/ask/:boardNum',
    name: 'PostDetail',
    component: PostDetail,
  },

  {
    path: '/luckyrich/qa/admin',
    name: 'adminBoard',
    component: AdminBoard,
  },
  {
    path: '/luckyrich/privacy',
    name: 'privacyPolicy',
    component: PrivacyPolicy,
  },
  {
    path: '/luckyrich/terms',
    name: 'termsOfService',
    component: TermsOfService,
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: NotFound
  },
  {
    path : '/luckyrich/fianaceNews',
    name : 'FinanceNews',
    component : FinanceNews
  },
  {
    path : '/luckyrich/estateNews',
    name : 'EstateNews',
    component : EstateNews
  },
  {
    path : '/luckyrich/securitiesNews',
    name : 'SecuritiesNews',
    component : SecuritiesNews
  },
  {
    path : '/luckyrich/personalNews',
    name : 'PersonalNews',
    component : PersonalNews
  },
  {
    path : '/luckyrich/globaleconomylNews',
    name : 'GlobalEconomyNews',
    component : GlobalEconomyNews
  },
  {
    path : '/luckyrich/economyNews',
    name : 'EconomyNews',
    component : EconomyNews
  },
];
