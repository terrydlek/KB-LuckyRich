<template>
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
        const response = await fetch(
          `http://localhost:8080/recommend/funds/${url}`
        );
        const data = await response.text(); // HTML 데이터를 받음
        this.htmlContent = data; // v-html로 출력할 HTML 저장
      } catch (error) {
        console.error('Error fetching HTML:', error);
      }
    },
  },
};
</script>
