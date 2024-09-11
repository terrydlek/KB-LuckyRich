<template>
    <button @click="loginWithNaver">
        <img :src="naverLoginImage" alt="Login with Naver" />
    </button>
</template>

<script setup>
import naverLoginImage from '@/assets/images/naver-login.png'

const loginWithNaver = () => {
    alert("login Naver");
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