<template>
  <div class="qa-page">
    <div class="container mt-5">
      <h2 class="faq-heading mb-4">게시판</h2>

      <!-- 검색 입력 필드 -->
      <input
        type="text"
        v-model="searchQuery"
        placeholder="검색하고 싶은 제목 또는 글쓴이가 있나요?"
        class="form-control mb-4"
      />

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
            <td>{{ post.boardNum }}</td>
            <td>
              <a :href="`/luckyrich/qa/ask/${post.boardNum}`">{{
                post.title
              }}</a>
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
        <button
          v-for="page in pageNumbers"
          :key="page"
          @click="currentPage = page"
          :class="{ active: currentPage === page }"
        >
          {{ page }}
        </button>

        <button @click="currentPage++" :disabled="currentPage === totalPages">
          다음
        </button>
      </div>

      <a href="/luckyrich/qa/ask/create" id="bt" class="btn btn-primary"
        >글쓰기</a
      >
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
    };
  },
  computed: {
    // 검색된 게시물 리스트
    filteredPosts() {
      return this.posts.filter(
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
  padding: 20px 40px;
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
}

.pagination button {
  margin: 5px;
  padding: 5px 10px;
}

.pagination button.active {
  font-weight: bold;
  background-color: #4caf50;
  color: white;
}
</style>
