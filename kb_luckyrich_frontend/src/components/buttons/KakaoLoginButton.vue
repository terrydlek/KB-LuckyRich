<template>
  <div>
    <button @click="loginWithKakao">
      <img :src="kakaoLoginImage" alt="Login with Kakao" />
    </button>
  </div>
</template>

<script setup>
import { onMounted } from 'vue';
import kakaoLoginImage from '@/assets/images/kakao-login.png';
import axios from 'axios';

function loginWithKakao() {
  // 백엔드에서 카카오 로그인 URL을 가져옴
  axios
    .post('http://localhost:8080/api/kakao')
    .then((res) => {
      // 해당 URL로 리다이렉트하여 카카오 로그인 진행
      window.location.href = res.data;
    })
    .catch((err) => {
      console.log(err);
    });
}

function handleKakaoLoginCallback() {
  // URL에서 카카오 인증 코드를 추출
  const urlParams = new URLSearchParams(window.location.search);
  const code = urlParams.get('code');

  if (code) {
    // 백엔드로 인증 코드를 전송하여 로그인 처리
    axios
      .get(`http://localhost:8080/api/login/kakao?code=${code}`)
      .then((res) => {
        // 성공 시 홈으로 리다이렉트
        window.location.href = '/';
      })
      .catch((err) => {
        console.log(err);
      });
  }
}

// 컴포넌트가 처음 로드될 때 카카오 로그인 콜백을 처리
onMounted(() => {
  //handleKakaoLoginCallback();
});
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
  width: 200px;
  height: 48px;
}
</style>
