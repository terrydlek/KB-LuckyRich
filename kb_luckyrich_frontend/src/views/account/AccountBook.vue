<template>
    <div id="accountbook" style="width: 100px;">
        <div class="currentmonth">
            <button @click="prevMonth">Previous Month</button>
            <span>{{ currentMonth }}, {{ currentYear }}</span>
            <button @click="nextMonth">Next Month</button>
        </div>
        <div class="transactions">
            <h2>거래내역</h2>
            <table>
                <thead>
                    <tr>
                        <th>Amount</th>
                        <th>Transaction Type</th>
                        <th>Category</th>
                        <th>Description</th>
                        <th>Transaction Date</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="transaction in transactions" :key="transaction.transactionDate">
                        <td>{{ transaction.amount }}</td>
                        <td>{{ transaction.transactionType }}</td>
                        <td>{{ transaction.category }}</td>
                        <td>{{ transaction.description }}</td>
                        <td>{{ formatDate(transaction.transactionDate) }}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import axios from 'axios';

const currentMonth = ref(new Date().getMonth() + 1);
const currentYear = ref(new Date().getFullYear())

const transactions = ref([]);

const prevMonth = () => {
    currentMonth.value = currentMonth.value - 1;
    if (currentMonth.value < 1) {
        currentMonth.value = 12;
        currentYear.value -= 1;
    }
};

const nextMonth = () => {
    currentMonth.value = currentMonth.value + 1;
    if (currentMonth.value > 12) {
        currentMonth.value = 1;
        currentYear.value += 1;
    }
};

function getTransactions() {
    const token = getToken();
    axios.get('http://localhost:8080/myasset/transaction', {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
        .then((response) => {
            transactions.value = response.data;
        })
        .catch((error) => {
            console.error(error);
        });
};

function getToken() {
    return localStorage.getItem('access_token');
};

function formatDate(timestamp) {
    const date = new Date(timestamp);
    return date.toLocaleDateString();
}

onMounted(() => {
    getTransactions();
})
</script>

<style>
#accountbook {
    max-width: 800px;
    padding: 20px;
}

.currentmonth {
    position: sticky;
    top: 0;
    background-color: white;
    z-index: 1;
}

.transactions {
    overflow-y: auto;
    flex-grow: 1;
}

.transaction-item {
    padding: 15px 0;
    border-bottom: 1px solid #eee;
}

.transaction-item:last-child {
    border-bottom: none;
}
</style>
