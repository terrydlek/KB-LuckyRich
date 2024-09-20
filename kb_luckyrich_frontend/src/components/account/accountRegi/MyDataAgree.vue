<template>
  <div class="consent-page">
    <div class="consent-header">
      <h3>LuckyRich에게 마이데이터 분석을 맡기세요!</h3>
      <img src="/src/assets/images/assetImage1.png" alt="정보 이미지" class="consent-image" style="height: 200px; width: 200px;" /> &nbsp &nbsp &nbsp
      <img src="/src/assets/images/assetImage2.png" alt="정보 이미지" class="consent-image" style="height: 200px; width: 200px;" />
    </div>

    <div class="consent-form">
      <label>
        <input type="checkbox" v-model="allChecked" @change="toggleAll" /> 전체 동의
      </label>

      <div class="individual-consent">
        <label>
          <input type="checkbox" v-model="consents[0]" /> [필수] Lucky Rich 이용약관
        </label>
        <button @click="openModal('terms')">보기</button><br>

        <label>
          <input type="checkbox" v-model="consents[1]" /> [필수] Lucky Rich 설명서
        </label>
        <button @click="openModal('guide')">보기</button><br>

        <label>
          <input type="checkbox" v-model="consents[2]" /> [필수] 개인신용정보 수집·이용 동의서 (Lucky Rich 통합자산관리서비스)
        </label>
        <button @click="openModal('privacy')">보기</button>
      </div>
    </div>

    <div class="consent-footer">
      <p>내 정보를 안전하게 지키는 마이데이터 사용법</p>
      <ul>
        <li>꼭 필요한 서비스만 가입해주세요.</li>
        <li>고객의 자산 관리는 Lucky Rich에게 맡겨주세요. 혁신적인 자산 관리를 통해 삶의 질을 향상시켜줍니다.</li>
      </ul>
    </div>

    <!-- Modal -->
    <div v-if="isModalOpen" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <h3>{{ modalTitle }}</h3>
        <p>{{ modalText }}</p>
        <button @click="closeModal">닫기</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      allChecked: false,
      consents: [false, false, false],
      isModalOpen: false,
      modalTitle: '',
      modalText: '',
    };
  },
  methods: {
    toggleAll() {
      this.consents = this.consents.map(() => this.allChecked);
    },
    openModal(type) {
      this.isModalOpen = true;
      switch (type) {
        case 'terms':
          this.modalTitle = 'Lucky Rich 이용약관';
          this.modalText = `Lucky Rich의 이용약관은 간단합니다. 1. 우리와 함께하면 당신의 자산이 마치 복권 당첨처럼 쑥쑥 늘어납니다! 2. 돈은 우리가 관리하고, 당신은 편안하게 삶을 즐기시면 됩니다. 3. 돈 문제로 스트레스? 그런 거 몰라요! 우리와 함께라면 걱정 뚝!`;
          break;
        case 'guide':
          this.modalTitle = 'Lucky Rich 설명서';
          this.modalText = `자산 관리? 그거 복잡하게 생각하지 마세요. Lucky Rich가 알아서 다 해줍니다! 설명서는 단순해요: 1. 앱을 켠다. 2. 클릭 몇 번. 3. 돈이 관리된다. 진짜예요! 직접 해보시면 압니다. "어? 이렇게 쉬운 거였어?"라는 감탄사를 준비해두세요!`;
          break;
        case 'privacy':
          this.modalTitle = '개인신용정보 수집·이용 동의서';
          this.modalText = `개인신용정보는 당연히 소중하죠. 걱정 마세요, Lucky Rich는 그걸 소중하게 여기며, 신용 정보를 절대 막 쓰지 않습니다. 그래서 우리는 최소한의 정보만 수집하고, 그걸로 고객님의 자산을 지키고, 늘리고, 행복하게 합니다. 걱정하지 마세요. 당신의 정보는 안전한 곳에 있습니다!`;
          break;
      }
    },
    closeModal() {
      this.isModalOpen = false;
    },
  },
  watch: {
    consents: {
      handler(newValues) {
        this.allChecked = newValues.every(Boolean);
      },
      deep: true,
    },
  },
};
</script>

<style scoped>
.consent-page {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #e0e0e0;
  border-radius: 10px;
  background-color: #f9f9f9;
}

.consent-header {
  text-align: center;
}

.consent-image {
  width: 100px;
  height: auto;
}

.consent-form {
  margin-top: 20px;
}

.individual-consent {
  margin-left: 20px;
}

button {
  margin-left: 10px;
  padding: 5px 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}

.consent-footer {
  margin-top: 20px;
  font-size: 12px;
  color: #7a7a7a;
}

.consent-footer ul {
  padding-left: 20px;
}

.consent-footer li {
  margin-top: 5px;
}

/* Modal styles */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 10px;
  max-width: 500px;
  width: 100%;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.modal-content h3 {
  margin-top: 0;
}

.modal-content p {
  margin: 20px 0;
}

.modal-content button {
  display: block;
  margin: 0 auto;
}
</style>