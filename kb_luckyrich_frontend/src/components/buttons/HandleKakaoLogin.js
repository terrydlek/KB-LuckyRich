import axios from 'axios';
import { authStore } from '@/stores/auth'; // 경로를 자신의 프로젝트에 맞게 조정


export function handleKakaoLoginCallback() {
    // URL에서 카카오 인증 코드를 추출
    const urlParams = new URLSearchParams(window.location.search);
    const KakaoCode = urlParams.get('code');
    console.log("kakakoCode", KakaoCode)

    if (KakaoCode) {
        // 백엔드로 인증 코드를 전송하여 로그인 처리
        axios.get(`http://localhost:8080/api/login/kakao?code=${KakaoCode}`)
            .then(res => {
                const accessToken = res.data.access_token;
                authStore.setLoggedIn(true); // 로그인 상태 업데이트
                localStorage.setItem('access_token', accessToken);
                console.log('KakaoAccess Token:', accessToken);
                window.history.replaceState({}, document.title, '/');

            })
            .catch(err => {
                console.log(err);
            });
    }
}