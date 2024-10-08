<template>
  <div class="login-container">
      <h2>회원가입하기</h2>
      <p>소셜 로그인 및 이메일로 가입할 수 있습니다.</p>
      <hr class="dotted-line" />
      <GoogleLoginButton class="social-button google" />
      <KakaoLoginButton class="social-button kakao" />
      <NaverLoginButton class="social-button naver" />
  </div>
</template>

<script setup>
import GoogleLoginButton from '@/components/buttons/GoogleLoginButton.vue';
import NaverLoginButton from '@/components/buttons/NaverLoginButton.vue';
import KakaoLoginButton from '@/components/buttons/KakaoLoginButton.vue';

</script>

<style>
.login-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  text-align: center;
}

.login-container h2 {
  font-size: 24px;
  margin-bottom: 10px;
}

.login-container p {
  margin-bottom: 20px;
}

.dotted-line {
  border: none;
  border-top: 3px dashed #ccc;
  width: 300px;
  margin: 20px 0;
}

.social-button {
  display: block;
  width: 300px;
  height: 50px;
  font-size: 16px;
  line-height: 50px;
  text-align: center;
  border-radius: 5px;
  margin: 10px 0;
  font-weight: bold;
}

.google {
  background-color: #f2f2f2;
  border: 1px solid #ccc;
  color: #444;
}

.kakao {
  background-color: #fee500;
  border: none;
  color: #3c1e1e;
}

.naver {
  background-color: #03c75a;
  border: none;
  color: white;
}
</style>
