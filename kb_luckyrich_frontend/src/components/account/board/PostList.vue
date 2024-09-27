<template>
  <div class="board-list">
    <h2>Q & A</h2>
    <table>
      <thead>
        <tr>
          <th>No.</th>
          <th>제목</th>
          <th>작성자</th>
          <th>작성일</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="post in posts" :key="post.id">
          <td>{{ post.id }}</td>
          <td>{{ post.title }}</td>
          <td>{{ post.author }}</td>
          <td>{{ post.createdAt }}</td>
        </tr>
        <tr v-if="posts.length === 0">
          <td colspan="4">게시글이 없습니다.</td>
        </tr>
      </tbody>
    </table>
    <button @click="goToCreatePost">글쓰기</button>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      posts: [],
    };
  },
  mounted() {
    axios
      .get('http://localhost:8080/board/post')
      .then((response) => {
        alert(response.data);
        console.log(JSON.stringify(response.data));
        this.posts = response.data;
      })
      .catch((error) => {
        console.error('There was an error fetching the lists:', error);
      });
  },
};
</script>

<style scoped>
table {
  width: 100%;
  border-collapse: collapse;
}
th,
td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}
th {
  background-color: #f2f2f2;
}
tr:hover {
  background-color: #f5f5f5;
  cursor: pointer;
}
button {
  margin-bottom: 10px;
  padding: 5px 10px;
  background-color: #007bff;
  color: white;
  border: none;
  cursor: pointer;
  border-radius: 4px;
}
</style>
