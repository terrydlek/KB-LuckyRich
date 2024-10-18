<template>
    <div>
      
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        selectedCategory: 'FinanceNews', // 기본값
      };
    },
    methods: {
      goTo(category) {
        this.selectedCategory = category;
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
  
  a {
    color: #6c63ff;
    text-decoration: none;
  }
  
  a:hover {
    text-decoration: underline;
  }
  </style>
  