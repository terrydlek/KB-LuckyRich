<template>
  <div class="qa-page">
    <div class="sidebar">
      <Sidebar />
    </div>
    <div class="container mt-5">
      <h2 class="mb-4">Q & A</h2>
      <table class="table table-striped">
        <thead class="thead-dark">
          <tr>
            <th>No</th>
            <th>제목</th>
            <th>글쓴이</th>
            <th>작성일</th>
          </tr>
        </thead>
        <tbody v-if="posts">
          <tr v-for="post in posts" :key="post.boardNum">
            <td>{{ post.boardNum }}</td>
            <td>
              <a :href="`/luckyrich/qa/ask/${post.boardNum}`">{{
                post.title
              }}</a>
            </td>
            <td>{{ post.nickName }}</td>
            <td>{{ new Date(post.createdAt).toLocaleString() }}</td>
          </tr>
        </tbody>
      </table>
      <a href="/luckyrich/qa/ask/create" id="bt" class="btn btn-primary"
        >글쓰기</a
      >
    </div>
  </div>
</template>

<script>
import Sidebar from '@/components/layout/QaSidebar.vue';
import axios from 'axios';

export default {
  components: { Sidebar },
  data() {
    return {
      posts: [],
    };
  },
  mounted() {
    axios
      .get('http://localhost:8080/board/getList', {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('access_token')}`,
        },
      })
      .then((response) => {
        this.posts = response.data;
      })
      .catch((error) => {
        console.error('There was an error fetching the posts:', error);
      });
  },
};
</script>

<style scoped>
.qa-page {
  display: flex;
  padding: 20px 40px;
}

#bt {
  margin-top: 20px;
  margin-bottom: 20px;
}
</style>
