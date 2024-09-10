import { createWebHistory, createRouter } from 'vue-router'  // npm im vue-router@next
import account from '@/router/account/account.js'
import user from '@/router/user/user.js'


const router = createRouter({
    history: createWebHistory(),
    routes: [
        ...account,
        ...user
    ]
})

export default router;