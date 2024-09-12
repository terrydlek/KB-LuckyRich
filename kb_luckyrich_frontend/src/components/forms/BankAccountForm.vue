<template>
    <div class="bank-account-form">
        <div class="input-group">
            <label for="bankName">은행명</label>
            <select id="bankName" v-model="bankName">
                <option value="국민">국민</option>
                <option value="카카오">카카오</option>
                <option value="신한">신한</option>
                <option value="농협">농협</option>
            </select>
        </div>
        <div class="input-group">
            <label for="accountNumber">계좌번호</label>
            <input id="accountNumber" v-model="accountNumber" type="text">
        </div>
        <div class="input-group">
            <label for="balance">잔고</label>
            <input id="balance" v-model="balance" type="text">
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';

const bankName = ref('');
const accountNumber = ref('');
const balance = ref('');
</script>

<style scoped>
.bank-account-form {
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

.input-group input,
.input-group select {
    width: 100%;
    padding: 8px;
    box-sizing: border-box;
}
</style>