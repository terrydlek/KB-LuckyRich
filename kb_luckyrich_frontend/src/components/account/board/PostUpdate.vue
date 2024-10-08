<template>
  <!-- <div class="container mt-5"> -->
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
  <!-- </div> -->
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
      const boardNum = this.$route.params.boardNum;
      const token = localStorage.getItem("access_token");
      try {
        const response = await axios.put(
          `http://localhost:8080/board`, // URL에 postId를 포함
          {
            boardNum : this.$route.params.boardNum,
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
        this.$router.push(`/luckyrich/qa/ask/${boardNum}`);
      } catch (error) {
        console.error("게시글 수정 중 오류가 발생했습니다.", error);
        alert("게시글 수정에 실패하였습니다."); // 실패 시 알림 추가
      }
    },
  },
};
</script>

<style scoped>
.container {
  max-width: 600px;
  margin: 50px auto; /* Center container with margin from top */
  background-color: #ffffff;
  padding: 40px; /* Increased padding for better aesthetics */
  box-shadow: 0px 8px 30px rgba(0, 0, 0, 0.15); /* Soft shadow for elevation */
  border-radius: 12px; /* Rounded corners */
}

h2 {
  text-align: center;
  font-size: 2rem;
  color: #333333;
  font-weight: bold;
  margin-bottom: 30px;
}

.form-group {
  margin-bottom: 20px;
}

.form-label {
  font-size: 1.1rem;
  font-weight: 500;
  color: #333333;
  margin-bottom: 10px;
  display: block;
}

input[type="text"],
textarea {
  width: 100%;
  padding: 12px 15px;
  font-size: 1rem;
  border: 1px solid #ced4da;
  border-radius: 8px; /* Rounded corners */
  transition: border-color 0.3s ease;
  background-color: #f9f9f9;
}

input[type="text"]:focus,
textarea:focus {
  border-color: #007bff; /* Blue border on focus */
  background-color: #ffffff; /* White background on focus */
  outline: none;
}

textarea {
  resize: none;
}

.btn-primary {
  width: 100%;
  padding: 12px;
  font-size: 1.2rem;
  font-weight: bold;
  text-transform: uppercase;
  background-color: #007bff; /* Primary button color */
  border: none;
  color: white;
  border-radius: 8px; /* Rounded button */
  transition: background-color 0.3s ease;
  cursor: pointer;
}

.btn-primary:hover {
  background-color: #0056b3; /* Darker shade on hover */
}

.alert {
  text-align: center;
  font-size: 1.1rem;
}

.alert-info {
  background-color: #e9f7fd;
  color: #007bff;
}

.alert-danger {
  background-color: #f8d7da;
  color: #721c24;
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
