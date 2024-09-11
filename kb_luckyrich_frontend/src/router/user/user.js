import Home from "@/views/Home.vue";
import Login from "@/views/user/Login.vue";
import UserUpdate from "@/views/user/UserUpdate.vue";

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
    }
]