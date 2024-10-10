<template>
  <button @click="goBack" class="back-button">목록으로</button>
  <div>
    <!-- 뉴스 제목 -->
    <h1>{{ newsDetail.title }}</h1>

    <!-- 날짜 및 시간 -->
    <p>{{ newsDetail.dateTime }}</p>

    <!-- 기자 정보 (선택적) -->
    <p>{{ newsDetail.reporter }}</p>

    <!-- 이미지 출력 -->
    <div v-if="newsDetail.imageUrl">
      <img
        :src="newsDetail.imageUrl"
        alt="뉴스 이미지"
        style="max-width: 100%; height: auto"
      />
      <!-- 이미지 설명 -->
      <p style="font-style: italic; margin-top: 0.5rem">
        {{ newsDetail.imageDescription }}
      </p>
    </div>

    <!-- 뉴스 텍스트 내용 -->
    <div>
      <p v-html="newsDetail.articleText"></p>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      newsDetail: {}, // 빈 객체로 초기화
    };
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
  },
  mounted() {
    const code1 = this.$route.params.code1;
    const code2 = this.$route.params.code2;
    const token = localStorage.getItem('access_token');
    if (!token) {
      throw new Error('토큰없음');
    }
    // 서버에서 뉴스 상세 데이터를 가져옴
    axios
      .get(`http://localhost:8080/news/${code1}/${code2}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => {
        this.newsDetail = res.data; // 데이터를 newsDetail에 저장
        console.log(this.newsDetail);
      })
      .catch((err) => {
        console.log(err);
      });
  },
};
</script>

<style scoped>
h1 {
  font-size: 2rem;
  margin-bottom: 1rem;
}

p {
  font-size: 1rem;
  margin-bottom: 1rem;
  line-height: 1.5;
}

img {
  margin-bottom: 0.5rem;
}

.back-button {
  width: 10%;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 10px 20px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s ease;
}

.back-button:hover {
  background-color: #32d43ace;
}
</style>
