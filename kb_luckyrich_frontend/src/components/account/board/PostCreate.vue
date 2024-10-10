<template>
  <div class="container mt-5">
    <form @submit.prevent="createPost">
      <div class="form-group">
        <label for="title">제목</label>
        <input
          type="text"
          id="title"
          class="form-control"
          v-model="title"
          required
          maxlength="100"
        />

        <label for="content" class="mt-3">내용</label>
        <textarea
          id="content"
          class="form-control"
          v-model="content"
          required
          maxlength="800"
        ></textarea>
        <small class="form-text text-muted">
          {{ content.length }}/800 글자
        </small>
      </div>
      <button type="submit" id="bt" class="btn btn-primary mt-3">작성하기</button>
    </form>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      title: "",
      content: "",
    };
  },
  methods: {
    async createPost() {
      const token = localStorage.getItem("access_token");
      try {
        const response = await axios.post(
          "http://localhost:8080/board",
          {
            title: this.title,
            content: this.content,
          },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            }
          }
        );
        if (response.data === "success") {
          alert("게시글이 성공적으로 등록되었습니다.");
          this.$router.push("/luckyrich/qa/ask");
        } else {
          alert("게시글 등록에 실패했습니다.");
        }
      } catch (error) {
        console.error("게시글 등록 중 오류가 발생했습니다.", error);
      }
    },
  },
};
</script>

<style>
#title {
  width: 300px;
}

#content {
  height: 400px;
}

#bt {
  margin-bottom: 10px;
}
</style>
