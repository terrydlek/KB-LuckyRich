<template>
    <nav class="navbar">
        <div class="navbar-left">
            <router-link to="/">Home</router-link>
        </div>
        <div class="navbar-right">
            <router-link to="/mypage">My Page</router-link>
            <button @click="handleAuth">{{ isLoggedIn ? 'Logout' : 'Login' }}</button>
        </div>
    </nav>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const isLoggedIn = ref(false); // 로그인 상태
const router = useRouter();

const handleAuth = () => {
    if (isLoggedIn.value) {
        // 로그아웃
        isLoggedIn.value = false;
        alert('Logged out');
    } else {
        // 로그인
        router.push({ name: 'login' });
    }
};
</script>

<style>
.navbar {
    position: fixed;
    top: 0;
    display: flex;
    justify-content: space-between;
    background-color: #333;
    padding: 1rem;
}

.navbar-left,
.navbar-right {
    display: flex;
    /* align-items: center; */
    margin: 0px, 10px;
}

.navbar a,
.navbar button {
    color: white;
    text-decoration: none;
    margin-right: 1rem;
    background: none;
    border: none;
    cursor: pointer;
}
</style>