<template>
    <div v-if="deposit">
        <h2>{{ deposit.prodname }} 상품 상세 정보</h2>
        <ul>
            <li><strong>금융회사:</strong> {{ deposit.company }}</li>
            <li><strong>상품명:</strong> {{ deposit.prodname }}</li>
            <li><strong>세전 이자율:</strong> {{ deposit.prerate ?? 'N/A' }}</li>
            <li><strong>세후 이자율:</strong> {{ deposit.afterrate ?? 'N/A' }}</li>
            <li><strong>세후 이자:</strong> {{ deposit.afterinterest ?? 'N/A' }}</li>
            <li><strong>최고 우대 금리:</strong> {{ deposit.bestinterest ?? 'N/A' }}</li>
            <li><strong>가입 제한 여부:</strong> {{ deposit.limit ?? 'N/A' }}</li>
            <li><strong>이자 계산 방식:</strong> {{ deposit.calmethod }}</li>
        </ul>

        <!-- 사용자로부터 가입 기간과 원금을 입력받기 위한 폼 -->
        <h3>이자 계산기</h3>
        <form @submit.prevent="calculateInterest">
            <div>
                <label for="principal">원금 (만원):</label>
                <input type="number" v-model="principal" min="1" required>
            </div>
            <div>
                <label for="period">가입 기간 (개월):</label>
                <input type="number" v-model="period" min="1" required>
            </div>
            <div>
                <label for="method">계산 방식:</label>
                <select v-model="calcMethod" required>
                    <option value="단리">단리</option>
                    <option value="복리">복리</option>
                </select>
            </div>
            <button type="submit">계산하기</button>
        </form>

        <!-- 계산 결과 표시 -->
        <div v-if="totalInterest !== null">
            <h4>계산 결과:</h4>
            <p><strong>입력하신 정보를 바탕으로 {{ deposit.prodname }} 상품에 대한 이자 합계를 계산해봤어요:</strong> {{ totalInterest.toFixed(2) }} 원</p>
        </div>
    </div>
    <p v-else>상품 정보를 불러오는 중입니다...</p>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            deposit: null,
            principal: null,  // 사용자 입력 원금 (만원 단위)
            period: null,     // 사용자 입력 가입 기간 (개월)
            calcMethod: '단리',  // 계산 방식 (단리 or 복리)
            totalInterest: null  // 계산된 이자 합계
        };
    },
    mounted() {
        const prodname = this.$route.params.prodname;  // URL 파라미터로부터 상품명 추출
        axios.get(`http://localhost:8080/asset/getDeposit/${prodname}`)  // 상품명에 따라 상세 정보 요청
            .then(response => {
                this.deposit = response.data;
            })
            .catch(error => {
                console.error("There was an error fetching the deposit details:", error);
            });
    },
    methods: {
        // 이자 계산 함수
        calculateInterest() {
            const rate = parseFloat(this.deposit.bestinterest) / 100;  // 최고 우대 금리를 소수로 변환
            const principalInWon = this.principal * 10000;  // 원금 단위를 만원에서 원으로 변환
            const months = parseFloat(this.period);

            if (this.calcMethod === '단리') {
                // 단리 계산: 원금 * 금리 * 기간
                this.totalInterest = (principalInWon * rate * (months / 12)) / 10000;  // 결과를 만원 단위로 변환
            } else if (this.calcMethod === '복리') {
                // 복리 계산: 원금 * (1 + 금리/12)^개월 수 - 원금
                this.totalInterest = (principalInWon * Math.pow((1 + rate / 12), months) - principalInWon) / 10000;  // 만원 단위로 변환
            }
        }
    }
}
</script>
