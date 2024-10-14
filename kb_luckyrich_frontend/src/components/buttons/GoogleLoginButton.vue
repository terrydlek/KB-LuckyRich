<template>
    <div>
        <button @click="loginWithGoogle">
            <img :src="googleLoginImage" alt="Login with Google" />
        </button>
    </div>
</template>

<script setup>
import { onMounted } from 'vue';
import googleLoginImage from '@/assets/images/googleKorean.png';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();

// 기존 checkAdminStatus 함수
async function checkAdminStatus() {
    try {
        const token = localStorage.getItem('access_token');
        if (!token) {
            console.error('토큰이 없습니다. 로그인이 필요합니다.');
            return;
        }

        const response = await axios.get('http://localhost:8080/user/role', {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });

        const userRole = response.data;
        localStorage.setItem('user_role', userRole);
        if (userRole === 'ADMIN') {
            console.log('관리자입니다.');
        }
    } catch (error) {
        console.error('사용자 역할 확인 중 오류 발생:', error);
        if (error.response && error.response.status === 401) {
            localStorage.removeItem('access_token');
            localStorage.removeItem('user_role');
            router.push('/luckyrich/login');
        }
    }
}

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

    if (accessToken) {
        localStorage.setItem('access_token', accessToken);
        localStorage.setItem('refresh_token', refreshToken);

        console.log(accountNum);

        // 로그인 이후 관리자 권한 확인
        checkAdminStatus();

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
    width: 200px;
    height: 50px;
    border-radius: 10px;
}
</style>
