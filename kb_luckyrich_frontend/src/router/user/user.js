import userUpdate from "@/components/user/userUpdate.vue";
import mypage from '@/components/user/mypage.vue';
import Home from "@/views/Home.vue";
import Login from "@/views/Login.vue";
// 
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
        component: userUpdate
    },
    {
        path: '/mypage',
        name: 'mypage',
        component: mypage
    }
]