<template>
  <nav class="navbar">
    <div class="navbar-left">
      <router-link to="/luckyrich">Lucky Rich</router-link>
    </div>

    <div class="navbar-right">
      <button v-if="isLoggedIn" @click="goToMyPage">My Page</button>
      <button @click="handleAuth">{{ isLoggedIn ? "Logout" : "Login" }}</button>
    </div>
  </nav>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";

const isLoggedIn = ref(false);
const router = useRouter();

onMounted(() => {
  checkLoginStatus();
});

function checkLoginStatus() {
  const token = localStorage.getItem('access_token');

  isLoggedIn.value = !!token;
}

const handleAuth = () => {
  if (isLoggedIn.value) {
    localStorage.removeItem("access_token");
    alert("로그아웃 되었습니다.");
    checkLoginStatus();
    router.push("/");
  } else {
    router.push({ name: "login" });
  }
};

const goToMyPage = () => {
  router.push("/luckyrich/userUpdate");
};

router.afterEach(() => {
  checkLoginStatus();
});
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
  margin: 0 50px;
  font-size: 23px;
}

.navbar-left {
  margin-left: 50px;
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

.middle h2 {
  color: #f8b400;
  margin-bottom: 10px;
}

.middle p {
  color: aliceblue;
  font-size: 14px;
}
</style>