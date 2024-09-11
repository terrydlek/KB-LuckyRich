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
import { useUserStore } from '@/stores/store';

const userStore = useUserStore();

function loginWithGoogle() {
    axios.post("http://localhost:8080/api/google", null, { params: {} })
        .then(res => {
            window.location.href = res.data;
        })
        .catch(err => {
            console.log(err);
        });
}

function handleGoogleLoginCallback() {
    const urlParams = new URLSearchParams(window.location.search);
    const code = urlParams.get('code');

    if (code) {
        axios.get(`http://localhost:8080/api/login/google?code=${code}`)
            .then(res => {
                window.location.href = '/';

                // const user = res.data;
                // userStore.setUser(user);
            })
            .catch(err => {
                console.log(err);
            });
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