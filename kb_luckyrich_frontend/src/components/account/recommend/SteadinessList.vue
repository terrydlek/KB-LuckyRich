<template>
  <center>
    <div class="deposit">
      <h3>당신의 투자 성향은 안정형입니다. 예적금 상품을 추천해드릴게요.</h3>

      <!-- 검색 입력 필드 -->
      <input type="text" v-model="searchQuery" placeholder="검색하고 싶은 금융회사 또는 상품명이 있나요?" />

      <!-- 데이터가 있을 경우 테이블 표시 -->
      <table v-if="filteredDeposits.length">
        <thead>
          <tr>
            <th>금융회사</th>
            <th>상품명</th>
            <th>최고 우대 금리</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(deposit, index) in paginatedDeposits" :key="index">
            <td>{{ deposit.company }}</td>
            <!-- 상품명 클릭 시 상세 페이지로 이동 -->
            <td><a :href="`/luckyrich/recommend/steadiness/${deposit.prodname}`">{{ deposit.prodname }}</a></td>
            <td>{{ deposit.bestinterest ?? 'N/A' }}</td>
          </tr>
        </tbody>
      </table>

      <!-- 페이지네이션 -->
      <div v-if="totalPages > 1">
        <button @click="currentPage--" :disabled="currentPage === 1">이전</button>

        <!-- 페이지 번호 표시 -->
        <button v-for="page in pageNumbers" :key="page" @click="currentPage = page"
          :class="{ active: currentPage === page }">
          {{ page }}
        </button>

        <button @click="currentPage++" :disabled="currentPage === totalPages">다음</button>
      </div>

      <p v-else-if="deposits.length">찾으시는 금융회사 또는 상품명을 찾을 수 없습니다.</p>
      <p v-else>예적금 상품을 불러오고 있어요~</p>
    </div>
  </center>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      deposits: [],
      searchQuery: '',
      currentPage: 1,
      itemsPerPage: 10,
      pageRange: 10, // 한 번에 보여줄 페이지 번호 개수
    };
  },
  computed: {
    // 검색된 예적금 상품 리스트
    filteredDeposits() {
      return this.deposits.filter(deposit =>
        deposit.company.includes(this.searchQuery) || deposit.prodname.includes(this.searchQuery)
      );
    },
    // 페이지네이션 적용된 예적금 상품 리스트
    paginatedDeposits() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredDeposits.slice(start, end);
    },
    // 총 페이지 수 계산
    totalPages() {
      return Math.ceil(this.filteredDeposits.length / this.itemsPerPage);
    },
    // 페이지 번호 계산
    pageNumbers() {
      const rangeStart = Math.floor((this.currentPage - 1) / this.pageRange) * this.pageRange + 1;
      const rangeEnd = Math.min(rangeStart + this.pageRange - 1, this.totalPages);
      return Array.from({ length: rangeEnd - rangeStart + 1 }, (_, i) => rangeStart + i);
    },
  },
  mounted() {
    this.fetchDepositData();
  },
  methods: {
    // 데이터 가져오기
    async fetchDepositData() {
      const token = localStorage.getItem('access_token');
      try {
        const response = await axios.get('http://localhost:8080/recommend/steadiness', {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        this.deposits = response.data;
      } catch (error) {
        console.error('There was an error fetching the deposits:', error);
      }
    }
  },
  watch: {
    // 검색어가 변경되면 페이지를 처음으로 초기화
    searchQuery() {
      this.currentPage = 1;
    }
  }
};
</script>

<style scoped>
.deposit {
  margin-top: 40px;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

th {
  background-color: #f2f2f2;
}

input[type="text"] {
  margin: 10px 0;
  padding: 5px;
  width: 50%;
}

button {
  margin: 5px;
  padding: 5px 10px;
}

button.active {
  font-weight: bold;
  background-color: #4caf50;
  color: white;
}
</style>
