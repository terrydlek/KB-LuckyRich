<template>
  <div class="container mt-5">
    <h2 class="mb-4">게시글 수정</h2>

    <div v-if="loading" class="alert alert-info">로딩 중...</div>
    <div v-else-if="error" class="alert alert-danger">{{ error }}</div>

    <form v-else @submit.prevent="updatePost">
      <div class="form-group mb-3">
        <label for="title" class="form-label">제목:</label>
        <input
          type="text"
          v-model="post.board.title"
          id="title"
          class="form-control"
          readonly
        />
      </div>
      <div class="form-group mb-3">
        <label for="content" class="form-label">내용:</label>
        <textarea
          v-model="post.board.content"
          id="content"
          class="form-control"
          rows="5"
          required
        ></textarea>
      </div>
      <button type="submit" class="btn btn-primary">수정 완료</button>
    </form>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      post: {
        board: {
          title: "",
          content: "",
        },
      },
      loading: true,
      error: null,
    };
  },
  mounted() {
    this.fetchPostDetails();
  },
  methods: {
    async fetchPostDetails() {
      const postId = this.$route.params.boardNum;
      const token = localStorage.getItem("access_token");
      try {
        const response = await axios.get(
          `http://localhost:8080/board/${postId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        this.post = response.data;
        this.loading = false;
      } catch (error) {
        this.error = "게시글을 불러오는 중 오류가 발생했습니다.";
        this.loading = false;
      }
    },
    async updatePost() {
      const postId = this.$route.params.boardNum;
      const token = localStorage.getItem("access_token");
      try {
        const response = await axios.post(
          "http://localhost:8080/board/updateBoard",
          {
            boardNum: postId,
            title: this.post.board.title,
            content: this.post.board.content,
          },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        alert("게시글이 성공적으로 수정되었습니다.", response.data);
        this.$router.push(`/luckyrich/qa/ask/${postId}`);
      } catch (error) {
        console.error("게시글 수정 중 오류가 발생했습니다.", error);
      }
    },
  },
};
</script>

<style scoped>
.container {
  max-width: 600px;
}

button {
  margin-top: 20px;
}
</style>

<!-- <template>
    <div id="summernote"></div>
</template>

<script setup>
import { onMounted } from 'vue';


onMounted(() => {
    $('#summernote').summernote({
        placeholder: 'Hello stand alone ui',
        tabsize: 2,
        height: 400,
        width: 1500,
        toolbar: [
            ['style', ['style']],
            ['font', ['bold', 'underline', 'clear']],
            ['color', ['color']],
            ['para', ['ul', 'ol', 'paragraph']],
            ['table', ['table']],
            ['insert', ['link', 'picture', 'video']],
            ['view', ['fullscreen', 'codeview', 'help']]
        ]
    });
})

</script> -->
