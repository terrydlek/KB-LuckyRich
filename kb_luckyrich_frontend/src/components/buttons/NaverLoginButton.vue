<template>
    <div>
        <button @click="loginWithNaver">
            <img :src="naverLoginImage" alt="Login with Naver" />
        </button>
    </div>
</template>

<script setup>
import { onMounted } from 'vue';
import naverLoginImage from '@/assets/images/NaverLoginB.png';
import axios from 'axios';

function loginWithNaver() {
    // 백엔드에서 네이버 로그인 URL을 가져옴
        
    axios.post("http://localhost:8080/api/naver")
        .then(res => {
            // 해당 URL로 리다이렉트하여 네이버 로그인 진행
            window.location.href = res.data;
        })
        .catch(err => {
            console.log(err);
        });
}

 function handleNaverLoginCallback() {
    // URL에서 네이버 인증 코드를 추출
    const urlParams = new URLSearchParams(window.location.search);
    const code = urlParams.get('code');
    console.log("asdddddddd", code);
    const state = urlParams.get('state')

    if (code) {
        // 백엔드로 인증 코드를 전송하여 로그인 처리
        axios.get(`http://localhost:8080/api/login/naver?code=${code}&state=${state}`)
            .then(res => {
                const accessToken = res.data.accessToken;
                alert('Access Token:', accessToken);

                // 액세스 토큰을 로컬 스토리지나 쿠키에 저장
                localStorage.setItem('access_token', accessToken);
                console.log('Access Token:', accessToken);
                // 성공 시 홈으로 리다이렉트
                //window.location.href = 'http://localhost:5173/';
            })
            .catch(err => {
                console.log(err);
            });
    }
}


</script>

<style scoped>
button {
    border: none;
    background: none;
    padding: 0;
    cursor: pointer;
}

button img {
    /* 이미지 크기 조정 */
    width: 300px;
    height: 50px;
    
}
</style>
