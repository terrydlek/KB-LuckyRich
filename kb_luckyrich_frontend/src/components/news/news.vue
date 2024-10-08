<template>
  <div>
    <div class="sidebar">
      <ul>
        <li
          @click="showNews('finance')"
          :class="{ active: selectedCategory === 'finance' }"
        >
          금융
        </li>
        <li
          @click="showNews('estate')"
          :class="{ active: selectedCategory === 'estate' }"
        >
          부동산
        </li>
        <li
          @click="showNews('securities')"
          :class="{ active: selectedCategory === 'securities' }"
        >
          증권
        </li>
        <li
          @click="showNews('personal')"
          :class="{ active: selectedCategory === 'personal' }"
        >
          생활경제
        </li>
      </ul>
    </div>

    <div class="news-container">
      <div
        v-for="newsItem in paginatedNews"
        :key="newsItem.title"
        class="news-item"
      >
        <div class="news-image" v-if="newsItem.imageUrl">
          <img :src="newsItem.imageUrl" alt="news image" />
        </div>
        <div class="news-content">
          <h3 class="news-title">
            <a :href="`/luckyrich/financenews/${newsItem.code}`">{{
              newsItem.title
            }}</a>
          </h3>
          <p class="news-description">{{ newsItem.description }}</p>
        </div>
      </div>
    </div>

    <div class="pagination">
      <button @click="prevPage" :disabled="currentPage === 1">이전</button>
      <span>{{ currentPage }} / {{ totalPages }}</span>
      <button @click="nextPage" :disabled="currentPage === totalPages">
        다음
      </button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'News',
  data() {
    return {
      news: [],
      estateNews: [],
      securitiesNews: [],
      personalNews: [],
      selectedCategory: 'finance',
      currentPage: 1,
      itemsPerPage: 10,
    };
  },
  mounted() {
    this.getNews();
    this.getEstateNews();
    this.getSecuritiesNews();
    this.getPersonalNews();
  },
  computed: {
    filteredNews() {
      if (this.selectedCategory === 'finance') {
        return this.news;
      } else if (this.selectedCategory === 'estate') {
        return this.estateNews;
      } else if (this.selectedCategory === 'securities') {
        return this.securitiesNews;
      } else if (this.selectedCategory === 'personal') {
        return this.personalNews;
      }
    },
    totalPages() {
      return Math.ceil(this.filteredNews.length / this.itemsPerPage);
    },
    paginatedNews() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredNews.slice(start, end);
    },
  },
  methods: {
    async getNews() {
      const token = localStorage.getItem('access_token');
      try {
        const res = await axios.get('http://localhost:8080/news/finance', {
          headers: { Authorization: `Bearer ${token}` },
        });
        this.news = res.data;
      } catch (err) {
        console.log(err);
      }
    },
    async getEstateNews() {
      const token = localStorage.getItem('access_token');
      try {
        const res = await axios.get('http://localhost:8080/news/estate', {
          headers: { Authorization: `Bearer ${token}` },
        });
        this.estateNews = res.data;
      } catch (err) {
        console.log(err);
      }
    },
    async getSecuritiesNews() {
      const token = localStorage.getItem('access_token');
      try {
        const res = await axios.get('http://localhost:8080/news/securities', {
          headers: { Authorization: `Bearer ${token}` },
        });
        this.securitiesNews = res.data;
      } catch (err) {
        console.log(err);
      }
    },
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
    showNews(category) {
      this.selectedCategory = category;
      this.currentPage = 1;
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
.sidebar {
  width: 200px;
  float: left;
  position: fixed; /* 사이드바를 고정 */
}

.sidebar ul {
  list-style-type: none;
  padding: 0;
}

.sidebar li {
  padding: 10px;
  cursor: pointer;
}

.sidebar li.active {
  font-weight: bold;
  background-color: #e6e9eb;
}

.news-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
  max-width: 800px;
  margin: 0 auto;
  margin-left: 220px;
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
</style>
