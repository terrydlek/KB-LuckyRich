<template>
    <div class="cash-form">
        <div class="input-group">
            <label for="cashAmount">현금 금액</label>
            <input id="cashAmount" v-model="cashAmount" type="text">
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';

const cashAmount = ref('');
</script>

<style scoped>
.cash-form {
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
</style>