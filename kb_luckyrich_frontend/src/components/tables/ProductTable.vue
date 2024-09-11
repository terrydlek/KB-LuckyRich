<template>
    <div class="asset-table">
        <table>
            <!-- <thead>
                <tr>
                    <th>항목</th>
                    <th>기간</th>
                    <th>최대이율</th>
                </tr>
            </thead> -->
            <tbody>
                <tr v-for="(item, index) in items" :key="index">
                    <td>{{ item.name }}</td>
                    <td>{{ item.term }}</td>
                    <td>{{ item.percent }}</td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script setup>
import { ref } from 'vue';

const items = ref([
    { name: '국민은행: 예금', term: '36개월', percent: '4.00%' },
    { name: '국민은행: 예금', term: '36개월', percent: '4.00%' },
    { name: '국민은행: 예금', term: '12개월', percent: '3.95%' },
    { name: '국민은행: 예금', term: '2년', percent: '3.92%' },
    { name: '국민은행: 예금', term: '36개월', percent: '3.90%' }
]);
</script>

<style scoped>
.asset-table {
    margin: 16px;
    border-collapse: collapse;
    width: 100%;
}

.asset-table th,
.asset-table td {
    border: 1px solid #ccc;
    padding: 8px;
    text-align: left;
}

.asset-table th {
    background-color: #f4f4f4;
}
</style>