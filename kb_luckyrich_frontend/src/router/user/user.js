import login from "@/components/user/login.vue";
import userUpdate from "@/components/user/userUpdate.vue";
import mypage from '@/components/user/mypage.vue';
import home from '@/components/home.vue';

export default [
    {
        path: '/',
        name: 'home',
        component: home
    },
    {
        path: '/login',
        name: 'login',
        component: login
    },
    {
        path: '/userUpdate',
        name: 'userUpdate',
        component: userUpdate
    },
    {
        path: '/mypage',
        name: 'mypage',
        component: mypage
    }
]