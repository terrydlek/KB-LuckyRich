<template>
  <div>
    <div class="sidebar">
      <ul>
        <li @click="showNews('finance')" :class="{ active: selectedCategory === 'finance' }">금융</li>
        <li @click="showNews('estate')" :class="{ active: selectedCategory === 'estate' }">부동산</li>
        <li @click="showNews('securities')" :class="{ active: selectedCategory === 'securities' }">증권</li>
        <li @click="showNews('personal')" :class="{ active: selectedCategory === 'personal' }">생활경제</li>
      </ul>
    </div>

    <div class="news-container">
      <div v-for="newsItem in filteredNews" :key="newsItem.title" class="news-item">
        <div class="news-image" v-if="newsItem.imageUrl">
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
      news: [], 
      estateNews: [], 
      securitiesNews : [],
      personalNews : [],
      selectedCategory: 'finance', 
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
    }
  },
  methods: {
    async getNews() {
      const token = localStorage.getItem('access_token');
      try {
        const res = await axios.get('http://localhost:8080/news/getnews', {
          headers: { 'Authorization': `Bearer ${token}` },
        });
        this.news = res.data;
      } catch (err) {
        console.log(err);
      }
    },
    async getEstateNews() {
      const token = localStorage.getItem('access_token');
      try {
        const res = await axios.get('http://localhost:8080/news/getnewsestate', {
          headers: { 'Authorization': `Bearer ${token}` },
        });
        this.estateNews = res.data;
      } catch (err) {
        console.log(err);
      }
    },
    async getSecuritiesNews() {
      const token = localStorage.getItem('access_token');
      try {
        const res = await axios.get('http://localhost:8080/news/getnewssecurities', {
          headers: { 'Authorization': `Bearer ${token}` },
        });
        this.securitiesNews = res.data;
      } catch (err) {
        console.log(err);
      }
    },
    async getPersonalNews() {
      const token = localStorage.getItem('access_token');
      try {
        const res = await axios.get('http://localhost:8080/news/getnewspersonal', {
          headers: { 'Authorization': `Bearer ${token}` },
        });
        this.personalNews = res.data;
      } catch (err) {
        console.log(err);
      }
    },
    showNews(category) {
      this.selectedCategory = category;
    },
  },
};
</script>


<style scoped>
.sidebar {
  width: 200px;
  float: left;
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
  margin-left: 220px; /* Adjust margin to make space for sidebar */
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
