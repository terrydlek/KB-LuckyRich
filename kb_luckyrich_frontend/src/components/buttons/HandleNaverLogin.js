// src/utils/naverLogin.js
import axios from 'axios';
import { authStore } from '@/stores/auth'; // 경로를 자신의 프로젝트에 맞게 조정
import { useRouter } from 'vue-router';

export function handleNaverLoginCallback() {
    const urlParams = new URLSearchParams(window.location.search);
    const NaverCode = urlParams.get('code');
    const service = urlParams.get('service');
    console.log('naverCode',NaverCode)
    const router = useRouter();

    if (NaverCode) {
        axios.get(`http://localhost:8080/api/login/naver?code=${NaverCode}&state=STATE_STRING`)
            .then(res => {
                const accessToken = res.data.accessToken;
                const accountNum = res.data.account_num;
                console.log(accountNum)
                localStorage.setItem('access_token', accessToken);

                authStore.setLoggedIn(true); // 로그인 상태 업데이트
                console.log('NaverAccess Token:', accessToken);
                if (accountNum > 1) {
                    router.push('/');
                }else {
                    router.push({ name: 'myDataAgree' });
                }
                
                window.history.replaceState({}, document.title, '/luckyrich');

            })
            .catch(err => {
                console.log(err);
            });
    }
}
