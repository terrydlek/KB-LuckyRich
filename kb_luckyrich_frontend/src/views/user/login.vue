<template>
    <div class="login-container">
        <GoogleLoginButton />
        <NaverLoginButton />
        <KakaoLoginButton />
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
}
.login-container > * {
  margin: 10px 0;
}
</style>