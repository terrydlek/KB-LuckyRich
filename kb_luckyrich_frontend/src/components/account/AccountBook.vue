<template>
    <div>
      <h1>Account Book</h1>
  
      <div class="carousel">
        <button @click="prevSlide" class="prev"></button>
  
        <div class="carousel-inner">
          <div
            class="carousel-item"
            v-for="(account, index) in accounts"
            :key="account.accountId"
            :class="{ active: index === currentIndex }"
          >
            <p>{{ account.accountName }}</p>
            <p>{{ account.accountNumber }}</p>
            <p>{{ formatBalance(account.balance) }}</p>
            <p>{{ account.accountType }}</p>
          </div>
        </div>
  
        <button @click="nextSlide" class="next"></button>
      </div>
    </div>
  </template>
  
  <script>
  import axios from "axios";
  
  export default {
    data() {
      return {
        accounts: [],
        currentIndex: 0, // 현재 슬라이드 인덱스
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
        return balance.toLocaleString();
      },
      nextSlide() {
        this.currentIndex =
          (this.currentIndex + 1) % this.accounts.length; // 다음 슬라이드로 이동
      },
      prevSlide() {
        this.currentIndex =
          (this.currentIndex - 1 + this.accounts.length) %
          this.accounts.length; // 이전 슬라이드로 이동
      },
    },
  };
  </script>
  
  <style>
  .carousel {
    position: relative;
    width: 300px;
    margin: auto;
    overflow: hidden;
  }
  
  .carousel-inner {
    display: flex;
    transition: transform 0.5s ease-in-out;
  }
  
  .carousel-item {
    min-width: 100%;
    display: none;
    text-align: center;
    padding: 20px;
    background-color: #f9f9f9;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }
  
  .carousel-item.active {
    display: block;
  }
  
  button.prev,
button.next {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 0;
  height: 0;
  border-style: solid;
  cursor: pointer;
  z-index: 10;
}

button.prev {
  left: 10px;
  border-width: 15px 20px 15px 0;
  border-color: transparent #007bff transparent transparent;
}

button.next {
  right: 10px;
  border-width: 15px 0 15px 20px;
  border-color: transparent transparent transparent #007bff;
}
  </style>
  