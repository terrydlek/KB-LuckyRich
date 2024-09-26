<template>
    <center>
        <div>
            <h3>당신의 투자 성향은 적극 투자형입니다. 주식 상품 TOP 100을 추천해드릴게요.</h3>

            <table v-if="stocks.length" class="stock-table">
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
                    <tr v-for="(stock, index) in stocks" :key="index">
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

            <p v-else>주식 데이터를 불러오는 중입니다...</p>
        </div>
    </center>
</template>

<script>
import axios from 'axios'

export default {
    name: 'ActiveList',
    data() {
        return {
            stocks: [],
        };
    },
    mounted() {
        this.fetchStockData();
    },
    methods: {
        async fetchStockData() {
            try {
                const response = await axios.get('http://localhost:8080/recommend/active');
                this.stocks = response.data;
            } catch (error) {
                console.error(error);
            }
        }
    }
}
</script>

<style scoped>
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

</style>
