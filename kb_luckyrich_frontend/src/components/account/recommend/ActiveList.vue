<template>
    <h2>당신의 투자 성향은 적극 투자형입니다. 주식 상품 추천해드릴게요.</h2>
    <div>
        <h2>Top 30 Stock Details</h2>
        <ul>
            <li v-for="(stock, index) in stockData" :key="index">
                {{ stock.currency }}: {{ stock.description }} : {{ stock.displaySymbol }} : {{ stock.figi }} : {{
                    stock.mic }} : {{ stock.symbol }} : {{ stock.type }}
            </li>
        </ul>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            stockSymbols: [
                'AAPL', 'GOOGL', 'AMZN', 'MSFT', 'TSLA', 'NFLX', 'NVDA', 'BABA', 'V', 'JNJ', 'WMT', 'PG', 'DIS', 'HD', 'MA', 'PYPL', 'NKE', 'PFE', 'ADBE', 'INTC', 'VZ', 'BA', 'SBUX', 'UNH', 'AMD', 'BLK', 'MS', 'LOW', 'DHR', 'ISRG'
            ],
            stockData: []
        };
    },
    methods: {
        fetchStockDetails() {
            const apiKey = 'crh8cipr01qrbc71o5m0crh8cipr01qrbc71o5mg';
            this.stockSymbols.forEach(symbol => {
                axios
                    .get(`https://finnhub.io/api/v1/stock/symbol?exchange=US&token=${apiKey}`)
                    .then(response => {
                        const stockInfo = response.data.find(stock => stock.symbol === symbol);
                        if (stockInfo) {
                            this.stockData.push(stockInfo);
                        }
                    })
                    .catch(error => {
                        console.error(error);
                    });
            });
        }
    },
    mounted() {
        this.fetchStockDetails();
    }
};
</script>

<style scoped>
h2 {
    font-size: 24px;
    margin-bottom: 10px;
}

ul {
    list-style-type: none;
    padding: 0;
}

li {
    font-size: 18px;
    margin: 5px 0;
}
</style>