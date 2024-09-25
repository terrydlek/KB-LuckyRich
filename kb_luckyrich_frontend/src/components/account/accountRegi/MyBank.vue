<template>
    <div id="mybank">
        <h1>My Accounts</h1>
        <table class="account-table">
            <thead>
                <tr>
                    <th>Account Name</th>
                    <th>Account Number</th>
                    <th>Balance</th>
                    <th>Account Type</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="account in accounts" :key="account.accountNumber">
                    <td>{{ account.bankName }}</td>
                    <td>{{ account.accountNumber }}</td>
                    <td>{{ formatBalance(account.balance) }}</td>
                    <td>{{ account.accountType }}</td>
                    <td><button @click="selectAccount(account)">선택</button></td>
                </tr>
            </tbody>
        </table>
        <div v-if="selectedAccount">
            <h2>Selected Account</h2>
            <p>Account Name: {{ selectedAccount.bankName }}</p>
            <p>Account Number: {{ selectedAccount.accountNumber }}</p>
            <p>Balance: {{ formatBalance(selectedAccount.balance) }}</p>
            <p>Account Type: {{ selectedAccount.accountType }}</p>
            <button @click="goToAccountFetch">다음</button>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router';

const router = useRouter();

const accounts = ref([]);
const selectedAccount = ref(null);

function formatBalance(balance) {
    // 천 단위로 쉼표를 추가하는 함수
    return balance.toLocaleString()
};

function getMyAccount() {
    const token = getToken();
    axios.get('http://localhost:8080/myasset/getMyAccount', {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
        .then((response) => {
            // console.log(response.data)
            accounts.value = response.data
        })
        .catch((error) => {
            console.error(error)
        })
};

function getToken() {
    return localStorage.getItem('access_token');
};

function selectAccount(account) {
    selectedAccount.value = account;
}

function goToAccountFetch() {
    axios.post("http://localhost:8080/myasset/fetchaccount", selectedAccount.value, {
        headers: {
            'Authorization': `Bearer ${getToken()}`
        }
    })
        .then(res => {
            console.log(res.data);
            // updating.value = false;
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

<style></style>