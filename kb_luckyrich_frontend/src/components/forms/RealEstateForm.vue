<template>
    <div class="real-estate-form">
        <div class="input-group">
            <label for="propertyType">부동산 형태</label>
            <input id="propertyType" v-model="propertyType" type="text">
        </div>
        <div class="input-group">
            <label for="propertyValue">금액</label>
            <input id="propertyValue" v-model="propertyValue" type="text">
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';

const propertyType = ref('');
const propertyValue = ref('');
</script>

<style scoped>
.real-estate-form {
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