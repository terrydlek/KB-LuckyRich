<template>
    <div class="user-info-container">
        <h3>User Info</h3>
        <div class="input-group">
            <label for="userName">이름</label>
            <input id="userName" v-model="username" type="text" disabled>
        </div>
        <div class="input-group">
            <label for="userEmail">이메일</label>
            <input id="userEmail" v-model="email" type="email" disabled>
        </div>
        <div class="input-group">
            <label for="userAge">나이</label>
            <input id="userAge" v-model="age" type="text" :disabled="!updating">
        </div>
        <div class="input-group">
            <label for="userGender">성별</label>
            <input id="userGender" v-model="gender" type="text" :disabled="!updating">
        </div>
        <div class="input-group">
            <label for="balance">총 보유 자산</label>
            <input id="balance" v-model="balance" type="text" disabled>
        </div>
        <div class="button-group">
            <button v-if="!updating" @click="setUpdate">Update</button>
            <button v-else @click="updateUserInfo">Save</button>
        </div>
    </div>
</template>

<script setup>
import axios from 'axios';
import { ref, onMounted } from 'vue';

const username = ref("");
const email = ref("");
const age = ref("");
const gender = ref("");
const balance = ref("");

const updating = ref(false);

function setUpdate() {
    updating.value = !updating.value;
};

// JWT 토큰을 로컬 스토리지에서 가져오는 함수
function getToken() {
    return localStorage.getItem('access_token');
}

// 유저 정보를 업데이트
function updateUserInfo() {
    axios.post("http://localhost:8080/user/update", {
        age: age.value,
        gender: gender.value
    }, {
        headers: {
            'Authorization': `Bearer ${getToken()}`
        }
    })
        .then(res => {
            console.log(res.data);
            updating.value = false;
        })
        .catch(err => {
            console.log(err);
        });
};

// 유저 정보를 가져오기
function fetchUserInfo() {

    axios.get("http://localhost:8080/user/inf", {
        headers: {
            'Authorization': `Bearer ${getToken()}`
        }
    })
        .then(res => {
            const data = res.data;
            console.log(data);

            username.value = data.username;
            email.value = data.email;
            age.value = data.age;
            gender.value = data.gender;
            balance.value = data.balance;
        })
        .catch(err => {
            console.log(err);
        });
};

onMounted(() => {
    fetchUserInfo();
});

</script>


<style scoped>
.user-info-container {
    max-width: 400px;
    margin: 0 auto;
    padding: 20px;
    border: 1px solid #ccc;
    border-radius: 8px;
    background-color: #f9f9f9;
}

h3 {
    text-align: center;
    margin-bottom: 20px;
}

.input-group {
    margin-bottom: 15px;
}

.input-group label {
    display: block;
    margin-bottom: 5px;
    font-weight: bold;
}

.input-group input {
    width: calc(100% - 10px);
    padding: 5px;
    border: 1px solid #ccc;
    border-radius: 4px;
    font-size: 14px;
}

.button-group {
    text-align: center;
    margin-top: 20px;
}

.button-group button {
    padding: 10px 20px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 14px;
}
</style>