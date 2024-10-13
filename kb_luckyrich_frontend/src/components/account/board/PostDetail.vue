<template>
  <div>
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

    <center>
      <div class="mb-3">
        <button id="bt2" v-if="post && post.userName === post.board.userName" class="btn btn-primary" @click="edit">
          수정
        </button>
        <button id="bt3" v-if="post && post.userName === post.board.userName" class="btn btn-primary"
          @click="deletePost">
          삭제
        </button>
        <button id="bt1" class="btn btn-primary" @click="goBack">
          목록으로
        </button>
      </div>
    </center>

    <!-- 댓글 입력 부분 -->
    <div v-if="post" class="comment-section mt-4">
      <h5>댓글 작성</h5>
      <textarea v-model="comment" class="form-control" rows="3" placeholder="댓글을 입력하세요"></textarea>
      <button class="btn btn-primary mt-3" @click="submitComment">
        댓글 달기
      </button>
    </div>

    <!-- 댓글 리스트 출력 부분 -->
    <div v-if="replies.length > 0" class="comment-list mt-4">
      <ul class="list-group">
        <li v-for="reply in replies" :key="reply.replyNum" class="list-group-item">
          <p>{{ reply.reply }}</p>
          <small class="text-muted">관리자 작성일:
            {{ new Date(reply.replyAt).toLocaleString() }}</small>
          <button class="btn btn-danger btn-sm float-end" @click="deleteReply(reply.replyNum)">
            삭제
          </button>
        </li>
      </ul>
    </div>
    <div v-else class="mt-4">
      <p>댓글이 없습니다.</p>
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

      console.log("boardNum Type: ", typeof boardNum);
      axios
        .put(
          `http://localhost:8080/board/changeDel`, // PUT 요청으로 변경
          { boardNum: this.$route.params.boardNum },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        )
        .then((resp) => {
          if (resp.data === "ok") {
            // 서버에서 보내는 메시지와 비교
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
      console.log("123");
      // 권한 확인 후 댓글 작성 로직
      axios
        .get(`http://localhost:8080/board/checkAdmin`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })
        .then((response) => {
          if (response.data === "ok") {
            // 댓글 작성 API 호출
            axios
              .post(
                `http://localhost:8080/board/reply`,
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
          console.error("잘못된 접근:", err);
          alert("권한이 없습니다.");
        });
    },

    // 댓글 삭제 메서드
    deleteReply(replyNum) {
      const token = localStorage.getItem("access_token");

      // 권한 확인 API 호출
      axios
        .get(`http://localhost:8080/board/checkAdmin`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })
        .then((response) => {
          if (response.data === "ok") {
            // 권한이 있을 경우 댓글 삭제 API 호출
            axios
              .delete(`http://localhost:8080/admin/reply/${replyNum}`, {
                headers: {
                  Authorization: `Bearer ${token}`,
                },
              })
              .then((resp) => {
                if (resp.data === "ok") {
                  alert("댓글이 성공적으로 삭제되었습니다.");
                  this.fetchPostAndReplies(); // 댓글 삭제 후 리스트 갱신
                } else {
                  alert("댓글 삭제에 실패했습니다.");
                }
              })
              .catch((err) => {
                console.error("댓글 삭제 중 오류가 발생했습니다:", err);
              });
          } else {
            alert("권한이 없습니다.");
          }
        })
        .catch((err) => {
          console.error("권한 확인 중 오류가 발생했습니다:", err);
          alert("권한이 없습니다.");
        });
    },
  },
};
</script>

<style scoped>
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.row {
  display: flex;
  gap: 20px;
}

/* 사이드바 */
/* .col-md-3 {
  flex-basis: 25%;
} */

.col-md-9 {
  flex-basis: 100%;
}

/* 카드 스타일 */
.card {
  background-color: #f7f7f7;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  width: 100% !important;
  min-height: 80%;
  margin-top: 0;
}

.card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);
}

.card-header {
  background-color: #FFD232;
  color: white;
  border-radius: 10px 10px 0 0;
  padding: 15px;
}

.card-title {
  font-size: 18px;
  font-weight: bold;
}

.card-body {
  padding: 20px;
}

.card-text {
  font-size: 16px;
  line-height: 1.6;
  color: #333;
}

.card-footer {
  background-color: #e9ecef;
  padding: 15px;
  font-size: 14px;
  color: #777;
  border-radius: 0 0 10px 10px;
}

/* 댓글 섹션 */
.comment-section {
  background-color: #f0f0f0;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

textarea.form-control {
  border: 2px solid #4a90e2;
  border-radius: 5px;
  padding: 10px;
  transition: border-color 0.3s ease;
}

textarea.form-control:focus {
  border-color: #0d6efd;
  outline: none;
}

.btn-primary {
  background-color: #4a90e2;
  border: none;
  padding: 10px 20px;
  font-size: 16px;
  border-radius: 5px;
  transition: background-color 0.3s ease, box-shadow 0.2s ease;
  cursor: pointer;
}

.btn-primary:hover {
  background-color: #357abd;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.btn-danger {
  background-color: #d9534f;
  border: none;
  padding: 5px 10px;
  font-size: 14px;
  border-radius: 5px;
  transition: background-color 0.3s ease;
  cursor: pointer;
}

.btn-danger:hover {
  background-color: #c9302c;
}

/* 댓글 리스트 */
.comment-list {
  margin-top: 10px;
}

.list-group-item {
  background-color: white;
  border: 1px solid #ddd;
  padding: 15px;
  border-radius: 8px;
  margin-bottom: 10px;
  transition: transform 0.2s ease;
}

.list-group-item:hover {
  transform: translateY(-3px);
}

.text-muted {
  color: #999;
}

/* 버튼 영역 */
#bt1,
#bt2,
#bt3 {
  font-size: 16px;
  padding: 10px 20px;
  margin: 10px;
  border-radius: 5px;
  transition: all 0.3s ease;
}

#bt1:hover,
#bt2:hover,
#bt3:hover {
  transform: translateY(-2px);
}

#bt2,
#bt3 {
  background-color: #f39c12;
}

#bt2:hover,
#bt3:hover {
  background-color: #e67e22;
}

.center {
  text-align: center;
}
</style>
