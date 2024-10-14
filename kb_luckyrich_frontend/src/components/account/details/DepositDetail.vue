<template>
  <button @click="goBack" class="back-button">목록으로</button>
  <div v-if="deposit" class="container mt-5">
    <h2 class="mb-4">{{ deposit.prodname }} 상품 상세 정보</h2>
    <ul class="list-group mb-4">
      <li class="list-group-item">
        <strong>금융회사:</strong> {{ deposit.company }}
      </li>
      <li class="list-group-item">
        <strong>상품명:</strong> {{ deposit.prodname }}
      </li>
      <li class="list-group-item">
        <strong>세전 이자율:</strong> {{ formatNumber(deposit.prerate) ?? 'N/A' }}%
      </li>
      <li class="list-group-item">
        <strong>세후 이자율:</strong> {{ formatNumber(deposit.afterrate) ?? 'N/A' }}%
      </li>
      <li class="list-group-item">
        <strong>최고 우대 금리:</strong> {{ formatNumber(deposit.bestinterest) ?? 'N/A' }}%
      </li>
      <li class="list-group-item">
        <strong>가입 제한 여부:</strong> {{ deposit.limit ?? 'N/A' }}
      </li>
      <li class="list-group-item">
        <strong>이자 계산 방식:</strong> {{ deposit.calmethod }}
      </li>
    </ul>

    <h3 class="mb-3">이자 계산기</h3>
    <form @submit.prevent="calculateInterest">
      <div class="mb-3">
        <label for="principal" class="form-label">원금:</label>
        <input type="number" class="form-control" v-model="principal" min="1" required />
      </div>
      <div class="mb-3">
        <label for="period" class="form-label">가입 기간 (개월):</label>
        <input type="number" class="form-control" v-model="period" min="1" required />
      </div>
      <div class="mb-3">
        <label for="method" class="form-label">계산 방식:</label>
        <select class="form-select" v-model="calcMethod" required>
          <option value="단리">단리</option>
          <option value="복리">복리</option>
        </select>
      </div>
      <button type="submit" class="btn btn-primary">계산하기</button>
    </form>

    <!-- 계산 결과 표시 -->
    <div v-if="totalInterest !== null" class="mt-4 alert alert-info">
      <h4><strong>현재 상품에 대한 이자 합계를 계산해봤어요</strong></h4>
      <br>
      <p>
        <strong>세전 이자:</strong> {{ formatCurrency(totalInterest) }} 원
      </p>
      <p>
        <strong>세후 이자:</strong> {{ formatCurrency(afterTaxInterest) }} 원
      </p>

      <p class="text-muted">
        * 이 계산은 {{ period }}개월 동안 {{ formatNumber(deposit.prerate) }}%의 세전 이자율과, {{ formatNumber(deposit.afterrate) }}% 세후 이자율을 기준으로 했으며,
        {{ taxRate * 100 }}%의 세율이 적용되었습니다.
      </p>
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
      principal: null, // 사용자 입력 원금 (만원 단위)
      period: null, // 사용자 입력 가입 기간 (개월)
      calcMethod: '단리', // 계산 방식 (단리 or 복리)
      totalInterest: null, // 계산된 이자 합계
      beforeTaxInterest: null, // 세전 이자
      afterTaxInterest: null, // 세후 이자
      taxRate: 0.154, // 이자세율(15.4%)
    };
  },
  mounted() {
    const prodname = this.$route.params.prodname; // URL 파라미터로부터 상품명 추출
    const token = localStorage.getItem('access_token');
    axios
      .get(`http://localhost:8080/recommend/steadiness/${prodname}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }) // 상품명에 따라 상세 정보 요청
      .then((response) => {
        this.deposit = response.data;
      })
      .catch((error) => {
        console.error(
          'There was an error fetching the deposit details:',
          error
        );
      });
  },
  methods: {
    // 이자 계산 함수
    calculateInterest() {
      const rate = parseFloat(this.deposit.bestinterest) / 100; // 최고 우대 금리를 소수로 변환
      const principalInWon = this.principal * 10000; // 원금 단위를 만원에서 원으로 변환
      const months = parseFloat(this.period);

      let interest = 0;

      if (this.calcMethod === '단리') {
        // 단리 계산: 원금 * 금리 * 기간
        interest = principalInWon * rate * (months / 12);
      } else if (this.calcMethod === '복리') {
        // 복리 계산: 원금 * (1 + 금리/12)^개월 수 - 원금
        interest = principalInWon * Math.pow(1 + rate / 12, months) - principalInWon;
      }

      // 세전 이자
      this.beforeTaxInterest = interest;

      // 세후 이자 (세전 이자에서 세금(15.4%) 차감)
      this.afterTaxInterest = interest * (1 - this.taxRate) / 10000;

      // 총 이자 (만 원 단위로 변환)
      this.totalInterest = interest / 10000;
    },
    // 숫자 콤마 추가 함수
    formatCurrency(value) {
      if (!value) return 'N/A';
      return value.toLocaleString(); // 천 단위 구분
    },
    // 숫자를 포맷팅하는 함수 (소수점 포함)
    formatNumber(value) {
      if (!value) return 'N/A';
      return parseFloat(value).toLocaleString('ko-KR', { maximumFractionDigits: 2 });
    },
    goBack() {
      this.$router.go(-1);
    },
  },
};
</script>


<style scoped>
.back-button {
  width: 10%;
  height: 30px;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 0 20px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s ease;
}

.back-button:hover {
  background-color: #32d43ace;
}

.btn {
  color: white;
  border: none;
  border-radius: 8px;
  padding: 0 20px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s ease;
}
</style>
