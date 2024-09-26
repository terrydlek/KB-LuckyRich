<template>
  <div class="create-post">
    <h2>게시글 작성</h2>
    {{ userId }}
    <form action="http://localhost:8080/board/created" method="post">
      <div>
        <label for="title">제목</label>
        <input type="text" id="title" v-model="title" required />
      </div>
      <div>
        <label for="title">작성자</label>
        <input
          type="text"
          id="writer"
          v-model="writer"
          value="{{ userId }}"
          required
        />
      </div>
      <div>
        <label for="content">내용</label>
        <textarea id="content" v-model="content" required></textarea>
      </div>
      <button type="submit">작성하기</button>
    </form>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      userId: sessionStorage.getItem('userId'),
    };
  },
};

/*
const title = ref('');
const content = ref('');
const router = useRouter();

const userId = sessionStorage.getItem('userId'); // 또는 localStorage.getItem('userId')

const submitPost = async () => {
  try {
    const response = await axios.post('http://localhost:8080/board/created', {
      title: title.value,
      content: content.value,
      userId: userId, // 로그인한 사용자의 ID 사용
    });
    const createdPostId = response.data.id; // 서버에서 생성된 게시글 ID를 가져옵니다
    alert('게시글이 성공적으로 작성되었습니다.');
    router.push({ name: 'PostDetail', params: { id: createdPostId } }); // 작성된 글의 상세 페이지로 이동
  } catch (error) {
    console.error('게시글 작성 중 오류가 발생했습니다:', error);
    alert('게시글 작성에 실패했습니다.');
  }
};
*/
</script>

<style scoped>
.create-post {
  max-width: 600px;
  margin: 0 auto;
}
form {
  display: flex;
  flex-direction: column;
}
label {
  margin: 10px 0 5px;
}
input,
textarea {
  padding: 10px;
  margin-bottom: 15px;
}
button {
  padding: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  cursor: pointer;
}
</style>
