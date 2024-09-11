<template>
    <div class="stock-form">
        <div class="input-group">
            <label for="stockName">주식 종목명</label>
            <input id="stockName" v-model="stockName" type="text">
        </div>
        <div class="input-group">
            <label for="stockAmount">보유량</label>
            <input id="stockAmount" v-model="stockAmount" type="text">
        </div>
        <div class="input-group">
            <label for="purchasePrice">구매가</label>
            <input id="purchasePrice" v-model="purchasePrice" type="text">
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';

const stockName = ref('');
const stockAmount = ref('');
const purchasePrice = ref('');
</script>

<style scoped>
.stock-form {
    border: 1px solid #ccc;
    padding: 16px;
    border-radius: 8px;
    margin-bottom: 16px;
}

.input-group {
    margin-bottom: 16px;
}

.input-group label {
    display: block;
    margin-bottom: 8px;
}

.input-group input {
    width: 100%;
    padding: 8px;
    box-sizing: border-box;
}

button {
    padding: 8px 16px;
    margin-top: 16px;
    cursor: pointer;
}
</style>