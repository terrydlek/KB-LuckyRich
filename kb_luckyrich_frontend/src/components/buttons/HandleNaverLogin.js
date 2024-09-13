// src/utils/naverLogin.js
import axios from 'axios';
import { authStore } from '@/stores/auth'; // 경로를 자신의 프로젝트에 맞게 조정

export function handleNaverLoginCallback() {
    const urlParams = new URLSearchParams(window.location.search);
    const NaverCode = urlParams.get('code');
    const service = urlParams.get('service');
    console.log('dasssssssss',service)

    if (NaverCode) {
        axios.get(`http://localhost:8080/api/login/naver?code=${NaverCode}&state=STATE_STRING`)
            .then(res => {
                const accessToken = res.data.accessToken;
                authStore.setLoggedIn(true); // 로그인 상태 업데이트
                localStorage.setItem('access_token', accessToken);
                console.log('NaverAccess Token:', accessToken);
                window.history.replaceState({}, document.title, '/');

            })
            .catch(err => {
                console.log(err);
            });
    }
}
