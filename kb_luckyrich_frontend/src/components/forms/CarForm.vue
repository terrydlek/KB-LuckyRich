<template>
    <div class="car-form">
        <div class="input-group">
            <label for="carModel">자동차 모델명</label>
            <input id="carModel" v-model="carModel" type="text">
        </div>
        <div class="input-group">
            <label for="carValue">금액</label>
            <input id="carValue" v-model="carValue" type="text">
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';

const carModel = ref('');
const carValue = ref('');
</script>

<style scoped>
.car-form {
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