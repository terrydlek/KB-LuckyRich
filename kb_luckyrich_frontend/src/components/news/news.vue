<template>
  <div>
    <div class="news-container">
      <div v-for="newsItem in news" :key="newsItem.title" class="news-item">
        <div class="news-image">
          <img :src="newsItem.imageUrl" alt="news image" />
        </div>
        <div class="news-content">
          <h3 class="news-title">
            <a :href="`/luckyrich/financenews/${newsItem.code}`">{{ newsItem.title }}</a>
          </h3>
          <p class="news-description">{{ newsItem.description }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'News',
  data() {
    return {
      news: [], // 뉴스 리스트
    };
  },
  mounted() {
    this.getNews();
  },
  methods: {
    async getNews() {
      const token = localStorage.getItem('access_token');

      try {
        const res = await axios.get('http://localhost:8080/news/getnews', {
          headers: {
                        'Authorization': `Bearer ${token}`
                    }
        });
        this.news = res.data; 
        console.log(res.data);
      } catch (err) {
        console.log(err);
      }
    },
  },
};
</script>

<style scoped>
.news-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.news-item {
  display: flex;
  gap: 20px;
  border-bottom: 1px solid #e6e9eb;
  padding-bottom: 20px;
}

.news-image img {
  width: 150px;
  height: 100px;
  object-fit: cover;
}

.news-content {
  flex-grow: 1;
}

.news-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 5px;
}

.news-description {
  font-size: 14px;
  line-height: 1.5;
}
</style>
