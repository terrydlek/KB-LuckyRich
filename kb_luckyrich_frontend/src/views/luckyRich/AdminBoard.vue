<template>
    <div class="board-management">
        <h2 class="title mb-4">게시판 관리</h2>

        <!-- 검색 입력 필드 -->
        <input type="text" v-model="searchQuery" placeholder="제목 또는 글쓴이 검색" class="search-bar mb-4" />

        <!-- 게시글 목록 테이블 -->
        <table class="table table-striped">
            <thead>
                <tr>
                    <th><input type="checkbox" @click="toggleSelectAll" /></th> <!-- 전체 선택 체크박스 -->
                    <th>No</th>
                    <th>제목</th>
                    <th>글쓴이</th>
                    <th>작성일</th>
                </tr>
            </thead>
            <tbody v-if="paginatedPosts.length">
                <tr v-for="(post, index) in paginatedPosts" :key="post.boardNum" class="table-row">
                    <td>
                        <input type="checkbox" v-model="selectedPosts" :value="post.boardNum" />
                    </td>
                    <td>{{ post.boardNum }}</td>
                    <td><a :href="`/luckyrich/qa/ask/${post.boardNum}`">{{ post.title }}</a></td>
                    <td>{{ post.nickName }}</td>
                    <td>{{ new Date(post.createdAt).toLocaleString() }}</td>
                </tr>
            </tbody>
        </table>

        <!-- 페이지네이션 -->
        <div v-if="totalPages > 1" class="pagination">
            <button @click="currentPage--" :disabled="currentPage === 1" class="pagination-button">이전</button>
            <button v-for="page in pageNumbers" :key="page" @click="currentPage = page"
                :class="{ active: currentPage === page }" class="pagination-button">
                {{ page }}
            </button>
            <button @click="currentPage++" :disabled="currentPage === totalPages" class="pagination-button">다음</button>
        </div>

        <!-- 게시글 삭제 버튼 -->
        <button @click="deleteSelectedPosts" class="btn btn-danger mb-4 delete-button">
            선택한 게시글 삭제
        </button>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            posts: [],
            searchQuery: '',
            currentPage: 1,
            itemsPerPage: 10,
            selectedPosts: [], // 선택된 게시글의 ID들
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
            const rangeStart = Math.floor((this.currentPage - 1) / this.pageRange) * this.pageRange + 1;
            const rangeEnd = Math.min(rangeStart + this.pageRange - 1, this.totalPages);
            return Array.from({ length: rangeEnd - rangeStart + 1 }, (_, i) => rangeStart + i);
        },
    },
    mounted() {
        this.fetchPosts();
    },
    methods: {
        // 데이터 가져오기
        fetchPosts() {
            axios
                .get('http://localhost:8080/admin/boards', {
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
        // 선택된 게시글 삭제
        deleteSelectedPosts() {
            if (this.selectedPosts.length === 0) {
                alert('삭제할 게시글을 선택하세요.');
                return;
            }

            alert(this.selectedPosts);
            axios
                .delete('http://localhost:8080/admin/boards', // DELETE 요청으로 변경
                    {
                        data: this.selectedPosts, // 요청 본문에 배열 전송
                        headers: {
                            Authorization: `Bearer ${localStorage.getItem('access_token')}`,
                        },
                    })
                .then(() => {
                    alert('관리자 권한으로 게시글이 삭제되었습니다.');
                    this.fetchPosts(); // 게시글 다시 불러오기
                    this.selectedPosts = []; // 선택 초기화
                })
                .catch((error) => {
                    console.error('게시글 삭제 중 오류가 발생했습니다:', error);
                });
        },
        // 전체 선택 및 해제 기능
        toggleSelectAll(event) {
            if (event.target.checked) {
                this.selectedPosts = this.paginatedPosts.map((post) => post.boardNum);
            } else {
                this.selectedPosts = [];
            }
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
.board-management {
    background-color: #f9f9f9;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    font-family: 'Roboto', sans-serif;
}

.title {
    color: #333;
    font-size: 1.8em;
    font-weight: bold;
    text-align: center;
    border-bottom: 2px solid #ddd;
    padding-bottom: 10px;
    margin-bottom: 20px;
}

.search-bar {
    width: 100%;
    padding: 10px;
    border-radius: 5px;
    border: 1px solid #ccc;
    box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.1);
}

.table {
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 20px;
}

.table thead {
    background-color: #333;
    color: white;
    text-align: left;
    font-weight: bold;
}

.table-row {
    background-color: white;
    transition: background-color 0.3s ease;
}

.table-row:hover {
    background-color: #f1f1f1;
}

.table th,
.table td {
    padding: 12px;
    text-align: center;
    border-bottom: 1px solid #ddd;
}

.table a {
    color: #007bff;
    text-decoration: none;
}

.table a:hover {
    text-decoration: underline;
}

.pagination {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 5px;
}

.pagination-button {
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 4px;
    padding: 8px 16px;
    cursor: pointer;
    transition: background-color 0.3s ease;
}

.pagination-button:hover {
    background-color: #0056b3;
}

.pagination-button.active {
    background-color: #0056b3;
    font-weight: bold;
}

.pagination-button:disabled {
    background-color: #ccc;
    cursor: not-allowed;
}

.delete-button {
    background-color: #dc3545;
    color: white;
    padding: 12px 20px;
    border: none;
    border-radius: 5px;
    font-size: 16px;
    cursor: pointer;
    transition: background-color 0.3s ease;
    display: block;
    margin: 0 auto;
    margin-top: 20px;
}

.delete-button:hover {
    background-color: #c82333;
}

.delete-button:disabled {
    background-color: #aaa;
}
</style>