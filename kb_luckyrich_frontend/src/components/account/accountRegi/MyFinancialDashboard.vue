<template>
  <div>
    <h1>My Financial Dashboard</h1>

    <!-- Accounts Section -->
    <section class="dashboard-section">
      <h2>My Accounts</h2>
      <br />
      <table class="dashboard-section">
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
    </section>

    <!-- Car Section -->
    <section class="dashboard-section">
      <h2>My Cars</h2>
      <br />
      <ul>
        <li v-for="(car, index) in cars" :key="index">
          <p><strong>Model:</strong> {{ car.carType }}</p>
          <p><strong>Price:</strong> {{ formatPrice(car.carAmount) }}</p>
          <hr />
        </li>
      </ul>
    </section>

    <!-- Estate Section -->
    <section class="dashboard-section">
      <h2>My Estates</h2>
      <br />
      <ul>
        <li v-for="(estate, index) in estates" :key="index">
          <p><strong>부동산 이름:</strong> {{ estate.realEstateName }}</p>
          <p>
            <strong>부동산 가격:</strong>
            {{ formatPrice(estate.realEstatePrice) }}
          </p>
          <hr />
        </li>
      </ul>
    </section>

    <!-- Stock Section -->
    <section class="dashboard-section">
      <h2>My Stocks</h2>
      <br />
      <ul>
        <li v-for="(stock, index) in stocks" :key="index">
          <p><strong>주식 명:</strong> {{ stock.investSymbol }}</p>
          <p><strong>구매 날짜:</strong> {{ formatDate(stock.investDate) }}</p>
          <p>
            <strong>구매 당시 가격:</strong>
            {{ formatPrice(stock.investPrice) }}
          </p>
          <hr />
        </li>
      </ul>
    </section>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      accounts: [],
      cars: [],
      estates: [],
      stocks: [],
    };
  },
  mounted() {
    axios
      .get('http://localhost:8080/myasset/myAccount')
      .then((response) => {
        console.log(response.data);
        this.accounts = response.data;
      })
      .catch((error) => {
        console.error('There was an error fetching the accounts:', error);
      });

    axios
      .get('http://localhost:8080/myasset/getMyCar')
      .then((response) => {
        console.log(response.data);
        this.cars = response.data;
      })
      .catch((error) => {
        console.error('There was an error fetching the cars:', error);
      });

    axios
      .get('http://localhost:8080/myasset/getMyEstate')
      .then((response) => {
        console.log(response.data);
        this.estates = response.data;
      })
      .catch((error) => {
        console.error('There was an error fetching the estates:', error);
      });

    axios
      .get('http://localhost:8080/myasset/getMyStock')
      .then((response) => {
        console.log(response.data);
        this.stocks = response.data;
      })
      .catch((error) => {
        console.error('There was an error fetching the stocks:', error);
      });
  },
  methods: {
    formatBalance(balance) {
      // 천 단위로 쉼표를 추가하는 함수
      return balance.toLocaleString();
    },
    // 가격 형식 변환 함수 (천 단위 구분 쉼표)
    formatPrice(price) {
      // price가 정의되어 있으면 toLocaleString을 호출, 그렇지 않으면 빈 문자열 반환
      return price ? price.toLocaleString() : '-';
    },
    // 날짜 형식 변환 함수
    formatDate(date) {
      const options = { year: 'numeric', month: 'short', day: 'numeric' };
      return new Date(date).toLocaleDateString(undefined, options);
    },
  },
};
</script>

<style scoped>
.dashboard-section {
  margin-bottom: 50px;
  padding: 40px;
  background-color: #f9f9f9;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* .account-table th,
.account-table td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
} */

/* .account-table th {
  background-color: #f4f4f4;
  font-weight: bold;
}

.account-table tr:nth-child(even) {
  background-color: #f9f9f9;
}

.account-table tr:hover {
  background-color: #f1f1f1;
} */
</style>
