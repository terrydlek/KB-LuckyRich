// src/utils/naverLogin.js
import axios from 'axios';
import { authStore } from '@/stores/auth'; // 경로를 자신의 프로젝트에 맞게 조정

export function handleNaverLoginCallback() {
    const urlParams = new URLSearchParams(window.location.search);
    const code = urlParams.get('code');
    const state = urlParams.get('state');

    if (code) {
        axios.get(`http://localhost:8080/api/login/naver?code=${code}&state=${state}`)
            .then(res => {
                const accessToken = res.data.accessToken;
                authStore.setLoggedIn(true); // 로그인 상태 업데이트
                localStorage.setItem('naver_access_token', accessToken);
                console.log('Access Token:', accessToken);
            })
            .catch(err => {
                console.log(err);
            });
    }
}
