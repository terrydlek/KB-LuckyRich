import Home from "@/views/Home.vue";
import About from "@/views/luckyRich/about.vue";
import PrivacyPolicy from "@/views/luckyRich/PrivacyPolicy.vue";
import TermsOfService from "@/views/luckyRich/TermsOfService.vue";
import Login from "@/views/user/Login.vue";
import UserUpdate from "@/views/user/UserUpdate.vue";
import FinanceNews from "@/views/news/FinanceNews.vue";

export default [
    {
        path: '/',
        name: 'home',
        component: Home
    },
    {
        path: '/login',
        name: 'login',
        component: Login
    },
    {
        path: '/userUpdate',
        name: 'userUpdate',
        component: UserUpdate
    },
    {
        path: '/about',
        name: 'about',
        component: About
    },
    {
        path: '/privacy',
        name: 'privacy',
        component: PrivacyPolicy
    },
    {
        path: '/terms',
        name: 'terms',
        component: TermsOfService
    },
    {
        path : '/financenews',
        name : 'financenews',
        component : FinanceNews
    },

]