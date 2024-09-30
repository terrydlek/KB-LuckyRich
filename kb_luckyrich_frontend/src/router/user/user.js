import Home from "@/views/Home.vue";
import About from "@/views/luckyRich/about.vue";
import PrivacyPolicy from "@/views/luckyRich/PrivacyPolicy.vue";
import Qa from "@/views/luckyRich/Qa.vue";
import TermsOfService from "@/views/luckyRich/TermsOfService.vue";
import Login from "@/views/user/login.vue";
import UserUpdate from "@/views/user/UserUpdate.vue";
import FinanceNews from "@/views/news/FinanceNews.vue";
import PostList from '@/components/account/board/PostList.vue';
// import BoardDetail from "@/components/account/board/PostdDetail.vue";
import PostUpdate from "@/components/account/board/PostUpdate.vue";
import PostCreate from "@/components/account/board/PostCreate.vue";
import PostDetail from "@/components/account/board/PostDetail.vue"
import AccountBook from "@/components/account/AccountBook.vue";

export default [
    {
        path: '/luckyrich',
        name: 'home',
        component: Home
    },
    {
        path: '/luckyrich/login',
        name: 'login',
        component: Login
    },
    {
        path: '/luckyrich/userUpdate',
        name: 'userUpdate',
        component: UserUpdate
    },
    {
        path: '/',
        name: 'about',
        component: About
    },
    {
        path: '/luckyrich/privacy',
        name: 'privacy',
        component: PrivacyPolicy
    },
    {
        path: '/luckyrich/terms',
        name: 'terms',
        component: TermsOfService
    },
    {
        path: '/luckyrich/qa',
        name: 'qa',
        component: Qa
    },
    {
        path: '/financenews',
        name: 'financenews',
        component: FinanceNews
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
        path : '/luckyrich/financenews',
        name : 'financenews',
        component : FinanceNews
    },
]