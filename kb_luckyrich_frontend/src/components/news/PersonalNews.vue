<template>
    <div class="news-container">
      <div v-for="newsItem in paginatedNews" :key="newsItem.title" class="news-item">
        <div class="news-image" v-if="newsItem.imageUrl">
          <img :src="newsItem.imageUrl" alt="news image" />
        </div>
        <div class="news-content">
          <h3 class="news-title">
            <a :href="`/luckyrich/personalnews/${newsItem.code}`">{{ newsItem.title }}</a>
          </h3>
          <p class="news-description">{{ newsItem.description }}</p>
        </div>
      </div>
  
      <div class="pagination">
        <button @click="prevPage" :disabled="currentPage === 1">이전</button>
        <span>{{ currentPage }} / {{ totalPages }}</span>
        <button @click="nextPage" :disabled="currentPage === totalPages">다음</button>
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        personalNews: [],
        currentPage: 1,
        itemsPerPage: 10,
      };
    },
    mounted() {
      this.getPersonalNews();
    },
    computed: {
      totalPages() {
        return Math.ceil(this.personalNews.length / this.itemsPerPage);
      },
      paginatedNews() {
        const start = (this.currentPage - 1) * this.itemsPerPage;
        const end = start + this.itemsPerPage;
        return this.personalNews.slice(start, end);
      },
    },
    methods: {
      async getPersonalNews() {
        const token = localStorage.getItem('access_token');
        try {
          const res = await axios.get('http://localhost:8080/news/personal', {
            headers: { Authorization: `Bearer ${token}` },
          });
          this.personalNews = res.data;
        } catch (err) {
          console.log(err);
        }
      },
      nextPage() {
        if (this.currentPage < this.totalPages) {
          this.currentPage++;
        }
      },
      prevPage() {
        if (this.currentPage > 1) {
          this.currentPage--;
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
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
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
  text-align: left;
}

.news-description {
  font-size: 14px;
  line-height: 1.5;
  text-align: left;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
}

.pagination button {
  margin: 0 10px;
  padding: 5px 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.pagination button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

a {
    color: #6c63ff;
    text-decoration: none;
}

a:hover {
    text-decoration: underline;
}  
  </style>
  