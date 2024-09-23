<template>
    <div>
        <!-- <h4>{{ stockData.stockName }} ({{ stockCode }})</h4> -->
        <div v-if="stockData">
            <p>현재가: {{ stockData.currentPrice }} 원</p>
            <p>전일 종가: {{ stockData.prevClosingPrice }} 원</p>
            <p>시가: {{ stockData.openingPrice }} 원</p>
            <p>고가: {{ stockData.highPrice }} 원</p>
            <p>상한가: {{ stockData.upperLimitPrice }} 원</p>
            <p>저가: {{ stockData.lowPrice }} 원</p>
            <p>하한가: {{ stockData.lowerLimitPrice }} 원</p>
            <p>거래량: {{ stockData.tradeVolume }} 주</p>
            <p>거래대금: {{ stockData.tradeValue }} 원</p>
        </div>
    </div>
    <StockCompareDetail />
</template>

<script>
import axios from 'axios';
import StockCompareDetail from './StockCompareDetail.vue';

export default {
    data() {
        return {
            stockData: null,     // 받아올 주식 데이터를 저장할 객체
        };
    },
    components: {
        StockCompareDetail
    },
    mounted() {
        const stockCode = this.$route.params.stockCode;
        axios.get(`http://localhost:8080/asset/stock/${stockCode}`)
            .then(res => {
                this.stockData = res.data;
            })
            .catch(err => {
                console.log(err);
            })
    }
};
</script>

<style scoped>
img {
    width: 300px;
    height: auto;
}
</style>