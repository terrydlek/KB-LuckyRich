import { createWebHistory, createRouter } from 'vue-router'  // npm im vue-router@next
import Home from '@/views/Home.vue'


const routes = [
    // MZ 자산 관리 API 등록 라우터
    {
        path: '/',
        name: 'Home',
        component: Home
    },
]



const router = createRouter({
    history: createWebHistory(),
    routes
});



export default router;
