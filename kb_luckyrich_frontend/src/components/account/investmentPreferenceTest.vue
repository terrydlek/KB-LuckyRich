<template lang="">
  <div class="test-wrapper">
    <h2>투자 성향 테스트</h2>
    <hr />
    <div v-if="currentQuestionIndex < questions.length">
      <div class="index">
        {{ currentQuestionIndex + 1 }} / {{ questions.length }}
      </div>
      <div class="question">Q. {{ questions[currentQuestionIndex].text }}</div>
      <div>
        <div
          class="answers"
          v-for="(option, index) in questions[currentQuestionIndex].options"
          :key="index"
        >
          <input
            type="radio"
            :id="'option' + index"
            :value="index"
            v-model="answers[currentQuestionIndex]"
          />
          <label :for="'option' + index">{{ option.text }}</label>
        </div>
      </div>
      <div>
        <button
          class="button"
          @click="nextQuestion"
          :disabled="answers[currentQuestionIndex] === null"
        >
          {{ isLastQuestion ? '결과 보기' : '다음 질문' }}
        </button>
      </div>
    </div>
    <div v-else>
      <h2>테스트 완료</h2>
      <p>당신의 투자 성향 : {{ investmentType }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue';
import { useRouter } from 'vue-router';
const router = useRouter();

const questions = [
  {
    text: '투자하고자 하는 자금의 투자 가능 기간은 얼마나 됩니까?',
    options: [
      { text: '6개월 이내', score: 3.1 },
      { text: '6개월 이상 ~ 1년 이내', score: 6.2 },
      { text: '1년 이상 ~ 2년 이내', score: 9.3 },
      { text: '2년 이상 ~ 3년 이내', score: 12.5 },
      { text: '3년 이상', score: 15.6 },
    ],
  },
  {
    text: '다음 중 투자경험과 가장 가까운 것은 어느 것입니까?',
    options: [
      { text: '은행의 예·적금, 국채, 지방채, 보증채, MMF, CMA 등', score: 3.1 },
      {
        text: '금융채, 신용도가 높은 회사채, 채권형펀드, 원금보존추구형 ELS 등',
        score: 6.2,
      },
      {
        text: '신용도 중간 등급의 회사채, 원금의 일부만 보장되는 ELS, 혼합형펀드 등',
        score: 9.3,
      },
      {
        text: '신용도가 낮은 회사채, 주식, 원금이 보장되지 않는 ELS, 시장수익률 수준의 수익을 추구하는 주식형펀드 등',
        score: 12.5,
      },
      {
        text: 'ELW, 선물옵션, 시장수익률 이상의 수익을 추구하는 주식형펀드, 파생상품에 투자하는 펀드, 주식 신용거래 등',
        score: 15.6,
      },
    ],
  },
  {
    text: '금융상품 투자에 대한 본인의 지식수준은 어느 정도라고 생각하십니까?',
    options: [
      {
        text: '[매우 낮은 수준] 투자의사 결정을 스스로 내려본 경험이 없는 정도',
        score: 3.1,
      },
      {
        text: '[낮은 수준] 주식과 채권의 차이를 구별할 수 있는 정도',
        score: 6.2,
      },
      {
        text: '[높은 수준] 투자할 수 있는 대부분의 금융상품의 차이를 구별할수 있는 정도',
        score: 9.3,
      },
      {
        text: '[매우 높은 수준] 금융상품을 비롯하여 모든 투자대상 상품의 차이를 이해할 수있는 정도',
        score: 12.5,
      },
    ],
  },
  {
    text: '현재 투자하고자 하는 자금은 전체 금융자산(부동산 등을 제외) 중 어느 정도의 비중을 차지합니까?',
    options: [
      { text: '10% 이내', score: 3.1 },
      { text: '10% 이상 ~ 20% 이내', score: 6.2 },
      { text: '20% 이상 ~ 30% 이내', score: 9.3 },
      { text: '30% 이상 ~ 40% 이내', score: 12.5 },
      { text: '40%', score: 15.6 },
    ],
  },
  {
    text: '다음 중 당신의 수입원을 가장 잘 나타내고 있는 것은 어느 것입니까?',
    options: [
      {
        text: '현재 일정한 수입이 발생하고 있으며, 향후 현재 수준을 유지하거나 증가할 것으로 예상된다',
        score: 9.3,
      },
      {
        text: '현재 일정한 수입이 발생하고 있으나, 향후 감소하거나 불안정할 것으로 예상된다.',
        score: 6.2,
      },
      { text: '현재 일정한 수입이 없으며, 연금이 주수입원이다.', score: 3.1 },
    ],
  },
  {
    text: '만약 투자원금에 손실이 발생할 경우 다음 중 감수할 수 있는 손실 수준은 어느 것입니까?',
    options: [
      {
        text: '무슨 일이 있어도 투자원금은 보전되어야 한다.',
        score: 6.2,
      },
      {
        text: '10% 미만까지는 손실을 감수할 수 있을 것 같다.',
        score: 6.2,
      },
      { text: '20% 미만까지는 손실을 감수할 수 있을 것 같다', score: 12.5 },
      { text: '기대수익이 높다면 위험이 높아도 상관하지 않겠다.', score: 18.7 },
    ],
  },
];

const currentQuestionIndex = ref(0);
const answers = ref(new Array(questions.length).fill(null));

const isLastQuestion = computed(
  () => currentQuestionIndex.value === questions.length - 1
);

const allQuestionsAnswered = computed(() => {
  for (let i = 0; i < answers.value.length; i++) {
    if (answers.value[i] === null) {
      return false;
    }
  }
  return true;
});

const nextQuestion = () => {
  if (currentQuestionIndex.value < questions.length - 1) {
    currentQuestionIndex.value++;
  } else if (allQuestionsAnswered.value) {
    showResult();
  } else {
    alert('모든 질문에 답해주세요.');
  }
};

const totalScore = computed(() => {
  return answers.value.reduce((sum, answerIndex, questionIndex) => {
    const question = questions[questionIndex];
    return sum + (question.options[answerIndex]?.score || 0);
  }, 0);
});

const investmentType = computed(() => {
  const score = totalScore.value;
  if (score <= 29) return '안정형';
  if (score <= 40) return '안정추구형';
  if (score <= 60) return '위험중립형';
  if (score <= 80) return '적극투자형';
  return '공격투자형';
});

const showResult = () => {
  if (investmentType.value === '안정형') {
    router.push('/SteadinessList');
  } else if (investmentType.value === '안정추구형') {
    router.push('/ConservativeList');
  } else if (investmentType.value === '위험중립형') {
    router.push('/Neutrility');
  } else if (investmentType.value === '적극투자형') {
    router.push('/ActiveList');
  } else {
    router.push('/AggresiveList');
  }
};

// watch(
//   () => currentQuestionIndex.value,
//   (newIndex) => {
//     if (newIndex === questions.length) {
//       logTestResults();
//     }
//   }
// );
</script>

<style scoped>
.test-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  max-width: 600px; /* 원하는 최대 너비로 조정 가능 */
  margin: 0 auto;
  padding: 20px;
}

.index {
  margin: 30px 0;
}

.question {
  margin: 30px 0;
}

.answer {
  margin: 30px 0;
}

.button {
  margin: 30px 0;
}

@media (max-width: 768px) {
  .charts-wrapper {
    flex-direction: column;
  }

  .chart {
    margin-bottom: 20px;
  }
}
</style>
