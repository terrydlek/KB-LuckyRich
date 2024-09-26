<template>
    <div>
        <button @click="loginWithGoogle">
            <img :src="googleLoginImage" alt="Login with Google" />
        </button>
    </div>
</template>

<script setup>
import { onMounted } from 'vue';
import googleLoginImage from '@/assets/images/google-login.png';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();

function loginWithGoogle() {
    axios.get("http://localhost:8080/api/google")
        .then(res => {
            window.location.href = res.data;
        })
        .catch(err => {
            console.log(err);
        });
}

function handleGoogleLoginCallback() {
    const urlParams = new URLSearchParams(window.location.search);
    const accessToken = urlParams.get('access_token');
    const accountNum = urlParams.get('account_num');
    if (accessToken) {
        localStorage.setItem('access_token', accessToken);
        // userStore.login();
        if (accountNum >= 1) {
            router.push('/');
        }
        router.push({ name: 'myDataAgree' });
    } else {
        console.error('Access token not found');
        // alert("로그인 실패");
    }
}

onMounted(() => {
    handleGoogleLoginCallback();
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
    /* 크기 조정 */
    width: 150px;
    height: auto;
}
</style>