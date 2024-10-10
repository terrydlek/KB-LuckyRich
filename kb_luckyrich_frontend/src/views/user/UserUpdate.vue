<template>
  <div class="user-info-container">
    <h2>User Info</h2>
    <p>회원 정보를 확인하고 수정할 수 있습니다.</p>
    <hr class="dotted-line" />

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
      <input
        id="userAge"
        v-model="age"
        type="text"
        @input="validateAge"
        :disabled="!updating"
      />
      <p v-if="ageError" class="error-message">{{ ageError }}</p>
    </div>
    <div class="input-group">
  <label for="userGender">성별</label>
  <select
    id="userGender"
    v-model="gender"
    :disabled="!updating"
    class="input-box"
  >
    <option value="남">남</option>
    <option value="여">여</option>
  </select>
</div>

    <div class="button-group">
      <button
        class="btn save-btn"
        v-if="!updating"
        @click="setUpdate"
      >
        수정
      </button>
      <button
        class="btn save-btn"
        v-else
        @click="updateUserInfo"
        :disabled="ageError || !age"
        :class="{ 'disabled-btn': ageError || !age }"
      >
        저장
      </button>
      &nbsp;&nbsp;&nbsp;&nbsp;
      <button class="btn withdraw-btn" @click="withdrawUser">탈퇴</button>
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
const ageError = ref(''); 

const updating = ref(false);

function setUpdate() {
  updating.value = !updating.value;
}

function validateAge() {
  // 입력된 값에서 선행 0을 제거
  const ageValue = age.value.replace(/^0+/, '');
  
  // 정수로 변환 후, 유효성 검사
  const parsedAge = parseInt(ageValue, 10);
  
  if (isNaN(parsedAge) || parsedAge <= 0) {
    ageError.value = '나이를 확인해주세요.';
  } else {
    age.value = parsedAge.toString();
    ageError.value = '';
  }
}

// JWT 토큰을 로컬 스토리지에서 가져오는 함수
function getToken() {
  return localStorage.getItem('access_token');
}

// 유저 정보를 업데이트
function updateUserInfo() {
  if (!ageError.value) {
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
}

function withdrawUser() {
  alert(userId.value);
  axios.delete(`http://localhost:8080/user/withdrawUser/${userId.value}`, {
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

<style scoped>
.user-info-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 80%;
  margin: 0 auto;
  padding: 40px;
  background-color: #fff;
  border-radius: 8px;
}

h2 {
  font-size: 24px;
  margin-bottom: 10px;
}

p {
  margin-bottom: 20px;
}

.dotted-line {
  border: none;
  border-top: 3px dashed #ccc;
  width: 100%;
  margin: 20px 0;
}

.input-group {
  width: 100%;
  margin-bottom: 20px;
}

.input-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.input-group input {
  width: calc(100% - 10px);
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 14px;
}

.error-message {
  color: red;
  font-size: 12px;
  margin-top: 5px;
}

.button-group {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.btn {
  width: 150px;
  height: 50px;
  font-size: 16px;
  border-radius: 4px;
  cursor: pointer;
}

.save-btn {
  background-color: #4CAF50;
  color: white;
  border: none;
}

.save-btn:hover {
  background-color: #45a049;
}

.withdraw-btn {
  background-color: #f44336;
  color: white;
  border: none;
}

.withdraw-btn:hover {
  background-color: #e53935;
}

.disabled-btn {
  background-color: block !important;
  cursor: not-allowed;
  color: white;
  
}
.input-box {
  width: calc(100% - 10px);
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 14px;
  background-color: white;
}
</style>
