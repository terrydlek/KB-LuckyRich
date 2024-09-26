import axios from 'axios';
import { authStore } from '@/stores/auth'; // 경로를 자신의 프로젝트에 맞게 조정
import { useRouter } from 'vue-router';


export function handleKakaoLoginCallback() {
    const router = useRouter(); // useRouter를 사용하여 라우터 인스턴스 가져오기

    // URL에서 카카오 인증 코드를 추출
    const urlParams = new URLSearchParams(window.location.search);
    const KakaoCode = urlParams.get('code');
    console.log("kakakoCode", KakaoCode)

    if (KakaoCode) {
        // 백엔드로 인증 코드를 전송하여 로그인 처리
        axios.get(`http://localhost:8080/api/login/kakao?code=${KakaoCode}`)
            .then(res => {
                console.log(res)
                const accessToken = res.data.access_token;
                const accountNum = res.data.account_num;
                console.log(accountNum)

                authStore.setLoggedIn(true); // 로그인 상태 업데이트
                localStorage.setItem('access_token', accessToken);

                if (accountNum > 1) {
                     router.push('/luckyrich'); // 루트 경로로 이동
                } else {
                     router.push({ name: 'myDataAgree' }); // 'myDataAgree'로 이동
                }
                
                
                window.history.replaceState({}, document.title, '/luckyrich');

            })
            .catch(err => {
                console.log(err);
            });
    }
}