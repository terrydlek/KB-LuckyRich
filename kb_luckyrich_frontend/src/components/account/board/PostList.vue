<template>
  <div class="qa-page">
    <div class="container mt-5">
      <h2 class="faq-heading mb-4">게시판</h2>

      <!-- 검색 입력 필드 -->
      <input type="text" v-model="searchQuery" placeholder="검색하고 싶은 제목 또는 글쓴이가 있나요?" class="form-control mb-4" />

      <table class="table table-striped">
        <thead class="thead-dark">
          <tr>
            <th>No</th>
            <th>제목</th>
            <th>글쓴이</th>
            <th>작성일</th>
          </tr>
        </thead>
        <tbody v-if="filteredPosts.length">
          <tr v-for="(post, index) in paginatedPosts" :key="index">
            <td>{{ getIndex(index) }}</td> <!-- No 값 -->
            <td>
              <a :href="`/luckyrich/qa/ask/${post.boardNum}`">{{ post.title }}</a>
            </td>
            <td>{{ post.nickName }}</td>
            <td>{{ new Date(post.createdAt).toLocaleString() }}</td>
          </tr>
        </tbody>
      </table>

      <!-- 페이지네이션 -->
      <div v-if="totalPages > 1" class="pagination">
        <button @click="currentPage--" :disabled="currentPage === 1">
          이전
        </button>

        <!-- 페이지 번호 표시 -->
        <button v-for="page in pageNumbers" :key="page" @click="currentPage = page"
          :class="{ active: currentPage === page }">
          {{ page }}
        </button>

        <button @click="currentPage++" :disabled="currentPage === totalPages">
          다음
        </button>
      </div>

      <a v-if="isLoggedIn" href="/luckyrich/qa/ask/create" id="bt" class="btn btn-primary">글쓰기</a>
    </div>
  </div>
</template>

<script>
import Sidebar from '@/components/layout/QaSidebar.vue';
import axios from 'axios';

export default {
  components: { Sidebar },
  data() {
    return {
      posts: [],
      searchQuery: '',
      currentPage: 1,
      itemsPerPage: 10,
      pageRange: 5, // 한 번에 보여줄 페이지 번호 개수
      accessToken: localStorage.getItem('access_token'),
    };
  },
  computed: {
    // 작성일 기준으로 오름차순 정렬된 게시물 리스트
    sortedPosts() {
      return this.posts.slice().sort((a, b) =>  new Date(b.createdAt) - new Date(a.createdAt));
    },
    isLoggedIn() {
      return !!this.accessToken; // accessToken이 있으면 true 반환
    },
    // 검색된 게시물 리스트
    filteredPosts() {
      return this.sortedPosts.filter(
        (post) =>
          post.title.includes(this.searchQuery) ||
          post.nickName.includes(this.searchQuery)
      );
    },
    // 페이지네이션 적용된 게시물 리스트
    paginatedPosts() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredPosts.slice(start, end);
    },
    // 총 페이지 수 계산
    totalPages() {
      return Math.ceil(this.filteredPosts.length / this.itemsPerPage);
    },
    // 페이지 번호 계산
    pageNumbers() {
      const rangeStart =
        Math.floor((this.currentPage - 1) / this.pageRange) * this.pageRange +
        1;
      const rangeEnd = Math.min(
        rangeStart + this.pageRange - 1,
        this.totalPages
      );
      return Array.from(
        { length: rangeEnd - rangeStart + 1 },
        (_, i) => rangeStart + i
      );
    },
  },
  mounted() {
    this.fetchPosts();
  },
  methods: {
    // 데이터 가져오기
    fetchPosts() {
      axios
        .get('http://localhost:8080/board', {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('access_token')}`,
          },
        })
        .then((response) => {
          this.posts = response.data;
        })
        .catch((error) => {
          console.error('There was an error fetching the posts:', error);
        });
    },
    // No 값 계산
    getIndex(index) {
      return (this.currentPage - 1) * this.itemsPerPage + index + 1;
    },
  },
  watch: {
    // 검색어가 변경되면 페이지를 처음으로 초기화
    searchQuery() {
      this.currentPage = 1;
    },
  },
};
</script>


<style scoped>
.qa-page {
  display: flex;
  justify-content: center;
  /* 페이지 가운데 정렬 */
  padding: 0px 30px;
  background-color: #f4f7f9;
  /* 배경색 추가 */
  min-height: 100vh;
  /* 화면 전체 높이 차지 */
}

.faq-heading {
  font-size: 2.5rem;
  color: #343a40;
  margin-bottom: 30px;
  font-weight: 700;
}

#bt {
  margin-top: 20px;
  margin-bottom: 20px;
  text-align: center;
  /* 제목 가운데 정렬 */
}

input[type='text'] {
  border-radius: 8px;
  /* 입력 필드 모서리 둥글게 */
  border: 1px solid #ccc;
  padding: 12px 20px;
  font-size: 1rem;
  transition: border-color 0.3s ease;
}

input[type='text']:focus {
  border-color: #007bff;
  /* 포커스 시 파란색으로 변경 */
  outline: none;
}

table {
  width: 100%;
  margin-top: 20px;
  border-collapse: collapse;
  background-color: #fff;
}

thead {
  background-color: #007bff;
  /* 테이블 헤더 배경색 */
  color: white;
  text-align: left;
}

th,
td {
  padding: 15px;
  text-align: center;
  border-bottom: 1px solid #ddd;
}

th {
  font-weight: bold;
  font-size: 1.1rem;
}

td a {
  color: #007bff;
  text-decoration: none;
  transition: color 0.3s ease;
}

td a:hover {
  color: #0056b3;
  /* 링크 마우스 오버 시 색상 변경 */
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.pagination button {
  margin: 5px;
  padding: 10px 20px;
  border-radius: 5px;
  background-color: #f0f0f0;
  border: 1px solid #ccc;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.pagination button:hover {
  background-color: #007bff;
  color: white;
}

.pagination button.active {
  font-weight: bold;
  background-color: #007bff;
  color: white;
}

.pagination button:disabled {
  background-color: #e0e0e0;
  cursor: not-allowed;
}

#bt {
  display: block;
  width: 150px;
  margin: 30px auto;
  padding: 12px;
  text-align: center;
  background-color: #28a745;
  /* 글쓰기 버튼 배경색 */
  color: white;
  font-weight: bold;
  text-transform: uppercase;
  border-radius: 5px;
  transition: background-color 0.3s ease;
  text-decoration: none;
}

#bt:hover {
  background-color: #218838;
  /* 마우스 오버 시 배경색 변경 */
}
</style>
