<template>
  <nav class="navbar">
    <div class="navbar-left">
      <router-link to="/luckyrich">Home</router-link>
    </div>

    <div class="middle">
      <center><h2>Lucky Rich</h2></center>
      <p>MZ 세대를 위한 스마트 자산 관리 플랫폼. 맞춤형 투자 상품 추천, 실시간 자산 정보 및 더 나은 재정 계획을 위한 도구들을 제공합니다.</p>
    </div>

    <div class="navbar-right">
      <router-link to="/luckyrich/userUpdate">My Page</router-link>

      <button @click="handleAuth">{{ isLoggedIn ? "Logout" : "Login" }}</button>
    </div>
  </nav>
</template>

<script setup>
import { authStore } from "@/stores/auth"; // 경로를 자신의 프로젝트에 맞게 조정
import { ref, onMounted, onBeforeUpdate } from "vue";
import { useRouter } from "vue-router";

// 로그인 상태 관리
const isLoggedIn = ref(false);
const router = useRouter();

// 컴포넌트가 마운트될 때 로그인 상태 확인
onMounted(() => {
  const token = localStorage.getItem('access_token');
  alert(token);

  isLoggedIn.value = !token; // 토큰이 있으면 로그인 상태로 변경
  isLogined();
});

// onBeforeUpdate(() => {

//   console.log("onBeforeUpdate");

//   isLogined();
// });

// 로그인/로그아웃 버튼 핸들러
const handleAuth = () => {
  if (isLoggedIn.value) {
    localStorage.removeItem("access_token");
    authStore.setLoggedIn(false); // 상태 업데이트
    alert("로그아웃 되었습니다.");
    isLoggedIn.value = false;
    router.push("/"); // 홈 페이지로 리다이렉트
  } else {
    router.push({ name: "login" });
  }
};

function isLogined() {
  const urlParams = new URLSearchParams(window.location.search);
  const accessToken = urlParams.get("access_token");
  // console.log(accessToken);

  if (accessToken != "") {
    isLoggedIn.value = true;
    return;
  }
  isLoggedIn.value = false;
  // return false;
}
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

.navbar-left{
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

.middle h2{
  color: #f8b400;
  margin-bottom: 10px;
}

.middle p{
  color: aliceblue;
  font-size: 14px;
}
</style>
