<template>
    <div>
        <button @click="loginWithGoogle">
            <img :src="googleLoginImage" alt="Login with Google" />
        </button>
    </div>
</template>

<script setup>
import { onMounted } from 'vue';
import googleLoginImage from '@/assets/images/GoogleLoginB.png';
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
    const refreshToken = urlParams.get('refresh_token');
    const userRole = urlParams.get('user_role');

    if (accessToken) {
        localStorage.setItem('access_token', accessToken);
        localStorage.setItem('refresh_token', refreshToken);
        localStorage.setItem('user_role', userRole);

        if (accountNum > 0) {
            router.push({ name: 'about' });
            return;
        }
        router.push({ name: 'myDataAgree' });
    } else {
        console.error('Access token not found');
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
    width: 100%;
    height: 50px;
    border-radius: 10px;
}
</style>
