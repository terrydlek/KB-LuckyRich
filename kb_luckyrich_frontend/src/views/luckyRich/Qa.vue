<template>
  <div class="faq-page">
    <div class="sidebar">
      <ul>
        <li><a href="/luckyrich/qa">자주 묻는 질문</a></li>
        <li><a href="/luckyrich/qa/ask">질문하기</a></li>
      </ul>
    </div>
    <div class="faq-content">
      <section id="faq">
        <h1>자주 묻는 질문</h1>
        <div class="faq-item" v-for="(faq, index) in faqs" :key="index">
          <div class="faq-question" @click="toggleFaq(index)">
            <h2>{{ faq.question }}</h2>
            <span v-if="activeFaq === index">▲</span>
            <span v-else>▼</span>
          </div>
          <div class="faq-answer" v-if="activeFaq === index">
            <p>{{ faq.answer }}</p>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const faqs = ref([
  {
    question: '자산관리는 어떻게 시작하나요?',
    answer:
      '회원가입 후, 자산 등록을 완료하면 자산 관리를 시작할 수 있습니다. 각 자산의 수익률을 실시간으로 확인할 수 있으며, 목표 설정과 비교해 진척도를 확인할 수 있습니다.',
  },
  {
    question: '투자 상품 추천은 어떻게 받나요?',
    answer:
      '고객님의 재정 상태 및 투자 성향에 맞춘 맞춤형 추천을 제공합니다. 투자 성향 테스트를 통해 정확한 상품 추천을 받을 수 있습니다.',
  },
  {
    question: '계좌 정보를 안전하게 보호하나요?',
    answer:
      '계좌 정보는 256비트 암호화 기술을 사용해 전송되며, 최신 보안 프로토콜로 보호합니다. 또한 개인정보는 최소한의 접근 권한을 가진 직원만 확인할 수 있습니다.',
  },
  {
    question: '부동산 정보는 어떻게 확인하나요?',
    answer:
      '부동산 정보는 최신 시장 데이터를 바탕으로 제공되며, 자산 페이지에서 시세 변동과 투자 가치를 실시간으로 확인할 수 있습니다.',
  },
  {
    question: '회원 탈퇴는 어떻게 하나요?',
    answer:
      '회원 탈퇴는 마이페이지에서 진행할 수 있으며, 탈퇴 후에도 일정 기간 동안 투자 내역 조회는 가능합니다. 하지만 모든 계좌 정보는 즉시 삭제됩니다.',
  },
  {
    question: '거래 내역 조회는 어떻게 하나요?',
    answer:
      '마이페이지의 거래 내역 메뉴에서 일자별, 카테고리별로 거래 내역을 조회할 수 있으며, CSV로 다운로드하여 보관할 수 있습니다.',
  },
  {
    question: '서비스 이용 요금은 있나요?',
    answer: 'Lucky Rich는 모든 자산관리 서비스가 무료로 제공됩니다.',
  },
  {
    question: '투자 리포트는 어떻게 확인하나요?',
    answer:
      '투자 리포트는 주기적으로 업데이트되며, 맞춤형 리포트를 받아볼 수 있습니다. 앱 내에서도 최신 리포트를 열람할 수 있습니다.',
  },
  {
    question: '고객 지원은 어디서 받을 수 있나요?',
    answer:
      '고객 지원은 마이페이지의 고객센터에서 채팅 상담을 통해 받으실 수 있으며, 상담 가능 시간은 월-금 오전 9시부터 오후 6시까지입니다.',
  },
  {
    question: '투자 위험 관리는 어떻게 이루어지나요?',
    answer:
      '실시간 시장 변동성에 대한 경고 알림을 설정해두면, 갑작스러운 위험 요소에 대비할 수 있습니다. 또한, 포트폴리오 위험도 분석을 제공해 드립니다.',
  },
]);

const activeFaq = ref(null);

const toggleFaq = (index) => {
  activeFaq.value = activeFaq.value === index ? null : index;
};
</script>

<style scoped>
.faq-page {
  display: flex;
  padding: 20px 40px;
}

.sidebar {
  flex: 1;
  padding: 20px;
  background-color: #f8b400;
  color: white;
  border-radius: 8px;
  max-height: 130px;
}

.sidebar ul {
  list-style: none;
  padding: 0;
}

.sidebar ul li {
  margin-bottom: 20px;
}

.sidebar ul li a {
  color: white;
  text-decoration: none;
  font-size: 1.5rem;
}

.faq-content {
  flex: 7;
  padding-left: 40px;
}

.faq-content h1 {
  font-size: 2.5rem;
  color: #f8b400;
  margin-bottom: 20px;
}

.faq-item {
  margin-bottom: 20px;
}

.faq-question {
  display: flex;
  justify-content: space-between;
  cursor: pointer;
  background-color: #f8b400;
  color: white;
  padding: 15px;
  border-radius: 8px;
  transition: background-color 0.3s;
}

.faq-question:hover {
  background-color: #e69c00;
}

.faq-answer {
  padding: 15px;
  background-color: #fff;
  border-left: 4px solid #f8b400;
  border-radius: 0 0 8px 8px;
}
</style>
