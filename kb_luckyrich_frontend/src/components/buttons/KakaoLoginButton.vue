<template>
    <button @click="loginWithKakao">
        <img :src="kakaoLoginImage" alt="Login with Kakao" />
    </button>
</template>

<script setup>
import kakaoLoginImage from '@/assets/images/kakao-login.png'

const loginWithKakao = () => {
    alert("login Kakao");
};

</script>

<style scoped>
button {
    border: none;
    background: none;
    padding: 0;
    cursor: pointer;
}

button img {
    /* 크기 조정 */
    width: 150px;
    height: auto;
}
</style>