import Home from "@/views/Home.vue";
import About from "@/views/luckyRich/about.vue";
import PrivacyPolicy from "@/views/luckyRich/PrivacyPolicy.vue";
import Qa from "@/views/luckyRich/Qa.vue";
import TermsOfService from "@/views/luckyRich/TermsOfService.vue";
import Login from "@/views/user/login.vue";
import UserUpdate from "@/views/user/UserUpdate.vue";
import FinanceNews from "@/views/news/FinanceNews.vue";

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
        path : '/luckyrich/financenews',
        name : 'financenews',
        component : FinanceNews
    },

]