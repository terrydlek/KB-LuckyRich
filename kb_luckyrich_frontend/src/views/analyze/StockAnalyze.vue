<template>
    <div id="stockanalyze">
        <table>
            <thead>
                <tr>
                    <!-- <th>Stock Symbol</th> -->
                    <th>종목명</th>
                    <th>구매가</th>
                    <th>보유수량</th>
                    <th>구매일</th>
                    <th>현재가</th>
                    <th>이익률 (%)</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="stock in myStocks" :key="stock.holdingId">
                    <!-- <td>{{ stock.stockSymbol }}</td> -->
                    <td>{{ stock.stockName }}</td>
                    <td>{{ stock.purchasePrice }}</td>
                    <td>{{ stock.quantity }}</td>
                    <td>{{ new Date(stock.purchaseDate).toLocaleDateString() }}</td>
                    <td>{{ stock.currentPrice }}</td>
                    <td :class="{'positive-margin': stock.margin > 0, 'negative-margin': stock.margin < 0}">
                        {{ stock.margin }}
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import axios from 'axios';

const myStocks = ref([]);
const currentStockInfo = ref([]);

function getStocks() {
    const token = getToken();
    axios.get('http://localhost:8080/myasset/getstock', {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
        .then((response) => {
            myStocks.value = response.data.myStocks;
            currentStockInfo.value = response.data.stocks;

            // Add currentPrice to myStocks
            myStocks.value.forEach(stock => {
                const currentStock = currentStockInfo.value.find(info => info.stockCode === stock.stockSymbol);
                if (currentStock) {
                    stock.currentPrice = currentStock.stockPrice;
                    stock.margin = calculateMargin(stock.purchasePrice, stock.currentPrice);
                }
            });
        })
        .catch((error) => {
            console.error(error);
        });
};

function getToken() {
    return localStorage.getItem('access_token');
};

function calculateMargin(purchasePrice, currentPrice) {
    // 쉼표 제거 및 Integer형으로 변환
    currentPrice = parseInt(currentPrice.replace(/,/g, ''), 10);
    
    return ((currentPrice - purchasePrice) / purchasePrice * 100).toFixed(2);
}


onMounted(() => {
    getStocks();
});
</script>

<style>
#stockanalyze table {
    width: 100%;
    border-collapse: collapse;
}

#stockanalyze th, #stockanalyze td {
    border: 1px solid #ddd;
    padding: 8px;
}

#stockanalyze th {
    background-color: #f2f2f2;
    text-align: left;
}

.positive-margin {
    color: red;
}

.negative-margin {
    color: blue;
}
</style>
