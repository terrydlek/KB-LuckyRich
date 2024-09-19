<template>
    <div>
        <h1>은행 계좌</h1>
        <ul v-if="accounts.length">
            <li v-for="account in accounts" :key="account.accountId">
                <strong>{{ account.accountName }}</strong>
                <p>계좌 번호: {{ account.accountNumber }}</p>
                <p>잔액: {{ account.balance }}</p>
                <p>계좌 종류: {{ account.accountType }}</p>
            </li>
        </ul>
        <p v-else>계좌 정보가 없습니다.</p>
    </div>
</template>

<script>
export default {
    data() {
        return {
            accounts: []  // 계좌 정보를 저장할 배열
        };
    },
    created() {
        this.fetchAccounts();  // 컴포넌트가 생성되면 계좌 정보를 가져옴
    },
    methods: {
        async fetchAccounts() {
            try {
                const response = await fetch('http://localhost:8080/api/account/accountRegi/MyBank');  // API에서 계좌 정보 가져오기
                const data = await response.json();  // JSON 형식으로 변환
                this.accounts = data;  // 계좌 정보를 accounts 배열에 저장
                console.log("DADADADADAD",data)
            } catch (error) {
                console.error('계좌 정보를 가져오는 도중 오류 발생:', error);  // 오류 처리
            }
        }
    }
}
</script>

<style>
/* 스타일을 여기에 추가하세요 */
</style>
