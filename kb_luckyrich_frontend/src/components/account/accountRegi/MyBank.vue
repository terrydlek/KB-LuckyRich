<template>
    <div id="mybank">
        <h1>My Accounts</h1>
        <table class="account-table">
            <thead>
                <tr>
                    <th>은행</th>
                    <th>계좌번호</th>
                    <th>잔액</th>
                    <th>계좌 종류</th>
                    <th>계좌 선택</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="account in accounts" :key="account.accountNumber">
                    <td>{{ account.bankName }}</td>
                    <td>{{ account.accountNumber }}</td>
                    <td>{{ formatBalance(account.balance) }}</td>
                    <td>{{ account.accountType }}</td>
                    <td>
                        <button @click="toggleAccountSelection(account)">
                            {{ isSelected(account) ? '선택 해제' : '선택' }}
                        </button>
                    </td>
                </tr>
            </tbody>
        </table>
        <button @click="goToAccountFetch" :disabled="selectedAccounts.length === 0">
            선택된 계좌 연동하기
        </button>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router';

const router = useRouter();

const accounts = ref([]);
const selectedAccounts = ref([]);

function formatBalance(balance) {
    // 천 단위로 쉼표를 추가하는 함수
    return balance.toLocaleString()
};

function getMyAccount() {
    const token = getToken();
    axios.get('http://localhost:8080/myasset/myAccount', {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
        .then((response) => {
            accounts.value = response.data
        })
        .catch((error) => {
            console.error(error)
        })
};

function getToken() {
    return localStorage.getItem('access_token');
};

function toggleAccountSelection(account) {
    const index = selectedAccounts.value.findIndex(a => a.accountNumber === account.accountNumber);
    if (index === -1) {
        selectedAccounts.value.push(account);
    } else {
        selectedAccounts.value.splice(index, 1);
    }
}

function isSelected(account) {
    return selectedAccounts.value.some(a => a.accountNumber === account.accountNumber);
}

function goToAccountFetch() {
    axios.post("http://localhost:8080/myasset/myAccount", selectedAccounts.value, {
        headers: {
            'Authorization': `Bearer ${getToken()}`
        }
    })
        .then(res => {
            console.log(res.data);
            router.push({name: 'home'})
        })
        .catch(err => {
            console.log(err);
        });
}

onMounted(() => {
    getMyAccount();
})
</script>

<style>
#mybank {
    font-family: Arial, sans-serif;
    margin: 20px;
}

h1 {
    color: #333;
}

.account-table {
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 20px;
}

.account-table th, .account-table td {
    border: 1px solid #ddd;
    padding: 8px;
    text-align: left;
}

.account-table th {
    background-color: #f2f2f2;
    color: #333;
}

.account-table tr:nth-child(even) {
    background-color: #f9f9f9;
}

.account-table tr:hover {
    background-color: #f1f1f1;
}

button {
    background-color: #4CAF50;
    color: white;
    border: none;
    padding: 10px 20px;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 14px;
    margin: 4px 2px;
    cursor: pointer;
    border-radius: 4px;
}

button:disabled {
    background-color: #ccc;
    cursor: not-allowed;
}
</style>