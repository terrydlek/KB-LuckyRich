<template>
  <div class="sidebar">
    <ul>
      <li><a href="/luckyrich/qa">자주 묻는 질문</a></li>
      <li><a href="/luckyrich/qa/ask">질문하기</a></li>
      <li><a href="#" @click.prevent="checkAdmin">관리자 페이지</a></li>
    </ul>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: "Sidebar",
  methods: {
    checkAdmin() {
      const token = localStorage.getItem('access_token');
      axios
        .get(`http://localhost:8080/board/checkAdmin`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })
        .then((response) => {
          if (response.data === "ok") {
            // 권한이 있으면 관리자 페이지로 이동
            this.$router.push('/luckyrich/qa/admin');
          } else {
            alert("권한이 없습니다.");
          }
        })
        .catch((err) => {
          console.error("잘못된 접근:", err);
          alert("권한이 없습니다.");
        });
    },
  },
};
</script>

<style scoped>
.sidebar {
  flex: 1;
  padding: 20px;
  background-color: #f8b400;
  color: white;
  border-radius: 8px;
  max-height: 190px;
}

.sidebar ul {
  list-style: none;
  padding: 0;
}

.sidebar ul li {
  margin-bottom: 20px;
}

.sidebar ul li a {
  color: white;
  text-decoration: none;
  font-size: 1.5rem;
}
</style>
