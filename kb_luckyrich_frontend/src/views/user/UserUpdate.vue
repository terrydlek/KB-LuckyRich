<template>
  <div class="user-info-container">
    <h3>User Info</h3>
    <div class="input-group">
      <label for="userName">이름</label>
      <input id="userName" v-model="username" type="text" disabled />
    </div>
    <div class="input-group">
      <label for="userEmail">이메일</label>
      <input id="userEmail" v-model="email" type="email" disabled />
    </div>
    <div class="input-group">
      <label for="userAge">나이</label>
      <input id="userAge" v-model="age" type="text" :disabled="!updating" />
    </div>
    <div class="input-group">
      <label for="userGender">성별</label>
      <input
        id="userGender"
        v-model="gender"
        type="text"
        :disabled="!updating"
      />
    </div>
    <div class="button-group">
      <button v-if="!updating" @click="setUpdate">수정</button>
      <button v-else @click="updateUserInfo">저장</button>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <button @click="withdrawUser">탈퇴</button>
    </div>
  </div>
</template>

<script setup>
import axios from 'axios';
import { ref, onMounted } from 'vue';

const username = ref('');
const email = ref('');
const age = ref('');
const gender = ref('');
const userId = ref('');

const updating = ref(false);

function setUpdate() {
  updating.value = !updating.value;
}

// JWT 토큰을 로컬 스토리지에서 가져오는 함수
function getToken() {
  return localStorage.getItem('access_token');
}

// 유저 정보를 업데이트
function updateUserInfo() {
  axios
    .post(
      'http://localhost:8080/user',
      {
        age: age.value,
        gender: gender.value,
      },
      {
        headers: {
          Authorization: `Bearer ${getToken()}`,
        },
      }
    )
    .then((res) => {
      console.log(res.data);
      updating.value = false;
    })
    .catch((err) => {
      console.log(err);
    });
}

function withdrawUser() {
  alert(userId.value);
  axios.delete(`http://localhost:8080/user/${userId.value}`, {
    headers: {
      Authorization: `Bearer ${getToken()}`,
    }
  })
  .then((res) => {
    if (res.data === 'ok') {
      alert('성공적으로 탈퇴되었습니다.');
      localStorage.removeItem("access_token");
      window.location.href = 'http://localhost:5173/';
    } else {
      alert('탈퇴에 실패하였습니다.')
    }
  })
  .catch((err) => {
    console.error('회원 탈퇴 실패: ', err);
    alert('탈퇴에 실패하였습니다.');
  })
}


// 유저 정보를 가져오기
function fetchUserInfo() {
  axios
    .get('http://localhost:8080/user', {
      headers: {
        Authorization: `Bearer ${getToken()}`,
      },
    })
    .then((res) => {
      const data = res.data;
      console.log(data);

      username.value = data.nickName;
      email.value = data.email;
      age.value = data.age;
      gender.value = data.gender;
      userId.value = data.userId;
    })
    .catch((err) => {
      console.log(err);
    });
}

onMounted(() => {
  fetchUserInfo();
});
</script>

<style>
.user-info-container {
  /* max-width: 400px; */
  width: 80%;

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
