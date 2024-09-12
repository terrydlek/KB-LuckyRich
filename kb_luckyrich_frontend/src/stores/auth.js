import { reactive } from 'vue';

export const authStore = reactive({
  isLoggedIn: !!localStorage.getItem('naver_access_token'), // 초기 상태 설정
  setLoggedIn(status) {
    this.isLoggedIn = status;
  }
});
