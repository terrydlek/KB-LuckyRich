<template>
  <div class="sidebar">
    <ul>
      <li><a href="/luckyrich/qa">자주 묻는 질문</a></li>
      <li><a href="/luckyrich/qa/ask">질문하기</a></li>
    </ul>
  </div>
</template>

<script>
export default {
  name: "Sidebar",
};
</script>

<style scoped>
.sidebar {
  flex: 1;
  padding: 20px;
  background-color: #f8b400;
  color: white;
  border-radius: 8px;
  max-height: 130px;
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
