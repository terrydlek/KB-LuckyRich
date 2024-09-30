<template>
  <div class="container mt-5">
    <div class="row">
      <div class="col-md-3">
        <Sidebar />
      </div>
      <div class="col-md-9">
        <div v-if="post" class="card">
          <div class="card-header">
            <h2>{{ post.board.title }}</h2>
          </div>
          <div class="card-body">
            <h5 class="card-title">작성자: {{ post.board.nickName }}</h5>
            <p class="card-text">{{ post.board.content }}</p>
          </div>
          <div class="card-footer text-muted">
            작성일: {{ new Date(post.board.createdAt).toLocaleString() }}
          </div>
        </div>
        <p v-else>게시글을 불러오는 중입니다...</p>

        <!-- 댓글 리스트 출력 부분 -->
        <div v-if="replies.length > 0" class="comment-list mt-4">
          <h5>댓글</h5>
          <ul class="list-group">
            <li
              v-for="reply in replies"
              :key="reply.replyAt"
              class="list-group-item"
            >
              <p>{{ reply.reply }}</p>
              <small class="text-muted"
                >관리자 작성일: {{ new Date(reply.replyAt).toLocaleString() }}</small
              >
            </li>
          </ul>
        </div>
        <div v-else class="mt-4">
          <p>댓글이 없습니다.</p>
        </div>

        <!-- 댓글 입력 부분 -->
        <div v-if="post" class="comment-section mt-4">
          <h5>댓글 작성</h5>
          <textarea
            v-model="comment"
            class="form-control"
            rows="3"
            placeholder="댓글을 입력하세요"
          ></textarea>
          <button class="btn btn-primary mt-3" @click="submitComment">
            댓글 달기
          </button>
        </div>

        <center>
          <div class="mb-3">
            <button
              id="bt2"
              v-if="post && post.userName === post.board.userName"
              class="btn btn-primary"
              @click="edit"
            >
              수정
            </button>
            <button
              id="bt3"
              v-if="post && post.userName === post.board.userName"
              class="btn btn-primary"
              @click="deletePost"
            >
              삭제
            </button>
            <button id="bt1" class="btn btn-primary" @click="goBack">
              목록으로
            </button>
          </div>
        </center>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import Sidebar from "@/components/layout/QaSidebar.vue";

export default {
  components: { Sidebar },
  data() {
    return {
      post: null,
      comment: "",
      replies: [],
    };
  },
  mounted() {
    this.fetchPostAndReplies();
  },
  methods: {
    fetchPostAndReplies() {
      const boardNum = this.$route.params.boardNum;
      const token = localStorage.getItem("access_token");

      // 게시글 정보 불러오기
      axios
        .get(`http://localhost:8080/board/${boardNum}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })
        .then((resp) => {
          this.post = resp.data;
        })
        .catch((err) => {
          console.error("게시글 불러오는 중 오류 발생:", err);
        });

      axios
        .get(`http://localhost:8080/board/reply/${boardNum}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })
        .then((resp) => {
          this.replies = resp.data; // 댓글 리스트 저장
        })
        .catch((err) => {
          console.error("댓글 불러오는 중 오류 발생:", err);
        });
    },

    goBack() {
      this.$router.push("/luckyrich/qa/ask");
    },

    deletePost() {
      const token = localStorage.getItem("access_token");
      const boardNum = this.$route.params.boardNum;

      console.log("boardNum: ", boardNum);
      console.log("boardNum Type: ", typeof boardNum);
      axios
        .post(
          `http://localhost:8080/board/deleteBoard`,
          {
            boardNum: boardNum
          },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        )
        .then((resp) => {
          if (resp.data === "ok") {
            alert("게시글이 성공적으로 삭제되었습니다.");
            this.$router.push("/luckyrich/qa/ask");
          } else {
            alert("게시글 삭제에 실패했습니다.");
          }
        })
        .catch((err) => {
          console.error("게시글 삭제 중 오류가 발생했습니다:", err);
        });
    },

    edit() {
      const postId = this.post.board.boardNum;
      this.$router.push(`/luckyrich/qa/ask/edit/${postId}`);
    },

    // 댓글 작성 메서드
    submitComment() {
      const token = localStorage.getItem("access_token");

      // 권한 확인 후 댓글 작성 로직
      axios
        .get(`http://localhost:8080/board/checkAdmin`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })
        .then((response) => {
          if (response.data === "yes") {
            // 댓글 작성 API 호출
            axios
              .post(
                `http://localhost:8080/board/addComment`,
                {
                  boardNum: this.post.board.boardNum,
                  reply: this.comment,
                },
                {
                  headers: {
                    Authorization: `Bearer ${token}`,
                  },
                }
              )
              .then((resp) => {
                if (resp.data === "ok") {
                  alert("댓글이 성공적으로 등록되었습니다.");
                  this.comment = ""; // 입력한 댓글 초기화
                  this.fetchPostAndReplies(); // 댓글 작성 후 리스트 갱신
                } else {
                  alert("댓글 등록에 실패했습니다.");
                }
              })
              .catch((err) => {
                console.error("댓글 등록 중 오류가 발생했습니다:", err);
              });
          } else {
            alert("권한이 없습니다.");
          }
        })
        .catch((err) => {
          console.error("권한 확인 중 오류가 발생했습니다:", err);
          alert("권한 확인에 실패했습니다.");
        });
    },
  },
};
</script>

<style>
#bt1,
#bt2,
#bt3 {
  margin-top: 20px;
}

#bt2,
#bt3 {
  margin-right: 20px;
}

.card {
  margin-left: 150px;
  width: 600px;
}

.comment-section {
  margin-left: 150px;
  width: 600px;
}
</style>
