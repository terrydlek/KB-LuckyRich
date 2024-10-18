<template>
    <div class="account-update">
        <div class="bank-accounts">
            <h3>은행 계좌</h3>
            <hr />
            <BankAccountForm v-for="(account, index) in accounts" :key="index" />
            <div class="button-group">
                <button @click="addAccount">추가</button>
                <button v-if="accounts.length > 1" @click="removeAccount">삭제</button>
            </div>
        </div>
        <div class="cash">
            <h3>현금</h3>
            <CashForm />
        </div>
        <div class="stocks">
            <h3>주식 정보</h3>
            <StockForm v-for="(stock, index) in stocks" :key="index" />
            <div class="button-group">
                <button @click="addStock">추가</button>
                <button v-if="stocks.length > 1" @click="removeStock">삭제</button>
            </div>
        </div>
        <div class="realestate">
            <h3>부동산</h3>
            <RealEstateForm />
        </div>
        <div class="car">
            <h3>자동차</h3>
            <CarForm />
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import BankAccountForm from '@/components/forms/BankAccountForm.vue';
import CashForm from '@/components/forms/CashForm.vue';
import StockForm from '@/components/forms/StockForm.vue';
import RealEstateForm from '@/components/forms/RealEstateForm.vue';
import CarForm from '@/components/forms/CarForm.vue';

const accounts = ref([{}]);
const stocks = ref([{}]);

function addAccount() {
    accounts.value.push({});
}

function removeAccount() {
    if (accounts.value.length > 1) {
        accounts.value.pop();
    }
}

function addStock() {
    stocks.value.push({});
}

function removeStock(index) {
    if (stocks.value.length > 1) {
        stocks.value.pop();
    }
}
</script>

<style scoped>
.account-update {
}

.bank-accounts,
.cash,
.stocks,
.realestate,
.car {
    padding: 16px;
    max-width: 500px;
}

.button-group {
    display: flex;
    gap: 8px;
    margin-top: 16px;
}

button {
    padding: 8px 16px;
    cursor: pointer;
}
</style>