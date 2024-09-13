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

// send age, gender to backend
function updateUserInfo() {

    // axios.post("", null, {
    //     age: age.value,
    //     gender: gender.value
    // })
    //     .then(res => {

    //     })
    //     .catch(err => {
    //         console.log(err);
    //     });

    updating.value = false;     // res.data == success
};

// function fetchUserInfo() {
//     axios.get("")
//         .then(res => {
//             res.data;
//         })
//         .catch(err => {
//             console.log(err);
//         })
// };

// onMounted(() => {
//     fetchUserInfo();
// });

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