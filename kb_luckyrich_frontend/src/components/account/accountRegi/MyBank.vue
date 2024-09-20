<template>
  <div>
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
        <tr v-for="account in accounts" :key="account.accountId">
          <td>{{ account.accountName }}</td>
          <td>{{ account.accountNumber }}</td>
          <td>{{ formatBalance(account.balance) }}</td>
          <td>{{ account.accountType }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      accounts: [],
    };
  },
  mounted() {
    axios
      .get("http://localhost:8080/myasset/getMyAccount")
      .then((response) => {
        console.log(response.data);
        this.accounts = response.data;
      })
      .catch((error) => {
        console.error("There was an error fetching the accounts:", error);
      });
  },
  methods: {
    formatBalance(balance) {
      // 천 단위로 쉼표를 추가하는 함수
      return balance.toLocaleString();
    },
  },
};
</script>

<style scoped>
.account-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.account-table th,
.account-table td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

.account-table th {
  background-color: #f4f4f4;
  font-weight: bold;
}

.account-table tr:nth-child(even) {
  background-color: #f9f9f9;
}

.account-table tr:hover {
  background-color: #f1f1f1;
}
</style>
