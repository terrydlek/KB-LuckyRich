import { createWebHistory, createRouter } from 'vue-router';
import account from '@/router/account/account.js';
import user from '@/router/user/user.js';
import analyze from './analyze/analyze';
//

const router = createRouter({
  history: createWebHistory(),
  routes: [...account, ...user, ...analyze],
});

export default router;
