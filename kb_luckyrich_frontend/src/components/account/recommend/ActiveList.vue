<template>
    <center>
        <div class="stocks">
            <h3>당신의 투자 성향은 적극 투자형입니다. 주식 상품 TOP 100을 추천해드릴게요.</h3>

            <!-- 검색 입력 필드 -->
            <input type="text" v-model="searchQuery" placeholder="검색하고 싶은 종목명이 있나요?" />

            <table v-if="filteredStocks.length" class="stock-table">
                <thead>
                    <tr>
                        <th>종목명</th>
                        <th>현재가</th>
                        <th>전일비</th>
                        <th>등락률</th>
                        <th>거래량</th>
                        <th>시가총액</th>
                        <th>매출액</th>
                        <th>영업이익</th>
                        <th>주당 순 이익</th>
                        <th>PER</th>
                        <th>ROE</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(stock, index) in paginatedStocks" :key="index">
                        <td><a :href="`/luckyrich/recommend/active/${stock.stockCode}`">{{ stock.stockName }}</a></td>
                        <td>{{ stock.stockPrice }}</td>
                        <td>{{ stock.comparePre }}</td>
                        <td>{{ stock.fluctuationRate }}</td>
                        <td>{{ stock.tradingVolume }}</td>
                        <td>{{ stock.marketCapitalization }}</td>
                        <td>{{ stock.salesAmount }}</td>
                        <td>{{ stock.operatingProfit }}</td>
                        <td>{{ stock.earningsPerShare }}</td>
                        <td>{{ stock.per }}</td>
                        <td>{{ stock.roe }}</td>
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

            <p v-else-if="stocks.length">찾으시는 종목명은 TOP 100에 없습니다.</p>
            <p v-else>주식 데이터를 불러오고 있어요~</p>
        </div>
    </center>
</template>

<script>
import axios from 'axios';

export default {
    name: 'ActiveList',
    data() {
        return {
            stocks: [],
            searchQuery: '',
            currentPage: 1,
            itemsPerPage: 10,
            pageRange: 10, // 한번에 보여줄 페이지 번호 개수
        };
    },
    computed: {
        // 검색된 종목 리스트
        filteredStocks() {
            return this.stocks.filter(stock =>
                stock.stockName.includes(this.searchQuery)
            );
        },
        // 페이지네이션 적용된 종목 리스트
        paginatedStocks() {
            const start = (this.currentPage - 1) * this.itemsPerPage;
            const end = start + this.itemsPerPage;
            return this.filteredStocks.slice(start, end);
        },
        // 총 페이지 수 계산
        totalPages() {
            return Math.ceil(this.filteredStocks.length / this.itemsPerPage);
        },
        // 페이지 번호 계산
        pageNumbers() {
            const rangeStart = Math.floor((this.currentPage - 1) / this.pageRange) * this.pageRange + 1;
            const rangeEnd = Math.min(rangeStart + this.pageRange - 1, this.totalPages);
            return Array.from({ length: rangeEnd - rangeStart + 1 }, (_, i) => rangeStart + i);
        },
    },
    mounted() {
        this.fetchStockData();
    },
    methods: {
        // 데이터 가져오기
        async fetchStockData() {
            const token = localStorage.getItem('access_token');
            try {
                const response = await axios.get('http://localhost:8080/recommend/active', {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });
                this.stocks = response.data;
            } catch (error) {
                console.error(error);
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
.stocks {
    margin-top: 40px;
}

.stock-table {
    width: 80%;
    border-collapse: collapse;
    margin-top: 20px;
}

.stock-table th,
.stock-table td {
    border: 1px solid #ddd;
}

.stock-table tr:hover {
    background-color: #ddd;
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