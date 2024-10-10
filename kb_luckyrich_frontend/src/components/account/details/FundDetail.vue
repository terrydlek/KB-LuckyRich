<template>
  <button @click="goBack" class="back-button">뒤로 가기</button>
  <div v-html="htmlContent"></div>
  <!-- HTML 소스를 렌더링 -->
</template>

<script>
export default {
  data() {
    return {
      htmlContent: '', // HTML 소스 저장
    };
  },
  created() {
    this.fetchHtmlContent();
  },
  methods: {
    async fetchHtmlContent() {
      try {
        const url = this.$route.params.fundUrl.substring(7); // 원하는 URL path
        const token = localStorage.getItem('access_token');
        const response = await fetch(
          `http://localhost:8080/recommend/funds/${url}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        const data = await response.text(); // HTML 데이터를 받음
        this.htmlContent = data; // v-html로 출력할 HTML 저장
      } catch (error) {
        console.error('Error fetching HTML:', error);
      }
    },
    goBack() {
      this.$router.go(-1);
    },
  },
};
</script>
<style scoped>
.back-button {
  width: 15%;
  margin-top: 50px;
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
