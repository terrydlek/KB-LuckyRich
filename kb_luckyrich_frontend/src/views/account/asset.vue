<template>
  <div class="dashboard-container">
    <!-- 상단 타이틀과 설명 -->
    <div class="header">
      <h1>내 자산을 쉽고 똑똑하게 관리하세요.</h1>
      <router-link to="/asset/accountupdate">자산 등록 또는 수정</router-link>
    </div>

    <div id="app">
      <div>
        <button @click="generatePortfolioPDF">자산 포트폴리오 다운로드</button>
      </div>
    </div>

    <!-- 카드형 데이터 박스 -->
    <div class="data-cards">
      <div class="card">
        <p>나의 총 자산</p>
        <p class="amount">{{ formatCurrency(totalAsset) }}원</p>
      </div>
      <div class="card">
        <p>나의 투자 금액</p>
        <p class="amount">{{ formatCurrency(investmentAmount) }}원</p>
      </div>
      <div class="card">
        <p>투자 성과 순위</p>
        <p class="highlight">상위 16%</p>
      </div>
    </div>

    <!-- 차트 섹션 -->
    <div>
      <div class="chart-row">
        <section>
          <totalChart />
        </section>
        <section>
          <goalChart />
        </section>
        <section>
          <assetGraph />
        </section>
      </div>
    </div>
    <div>
      <div class="chart-row">
        <section>
          <accountBookChart />
        </section>
        <section>
          <consumptionstatus />
        </section>
        <section>
          <assetcomparison />
        </section>
      </div>
    </div>

    <!-- 숨겨진 Socket.vue 컴포넌트 (화면에서 보이지 않음) -->
    <div v-if="showSocketComponent" style="display: none">
      <Socket ref="socketComponent" />
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import totalChart from '@/components/account/chart/totalChart.vue';
import goalChart from '@/components/account/chart/goalChart.vue';
import assetGraph from '@/components/account/chart/assetGraph.vue';
import accountBookChart from '@/components/account/chart/accountBookChart.vue';
import assetcomparison from '@/components/account/chart/assetComparison.vue';
import consumptionstatus from '@/components/account/chart/consumptionstatus.vue';
import Socket from '@/views/account/Socket.vue';
import axios from 'axios';

export default {
  components: {
    totalChart,
    goalChart,
    assetGraph,
    accountBookChart,
    assetcomparison,
    consumptionstatus,
    Socket,
  },
  data() {
    return {
      showSocketComponent: false,
      totalAsset: 0,
      investmentAmount: 0,
      investmentRank: 0,
    };
  },
  mounted() {
    this.fetchAssetData();
  },
  methods: {
    async generatePortfolioPDF() {
      try {
        const token = localStorage.getItem('access_token');

        // RabbitMQ에 메시지 발행 요청 (http://localhost:8080/rabbit/publish)
        await axios
          .post(
            'http://localhost:8080/rabbit/publish',
            {},
            {
              headers: {
                Authorization: `Bearer ${token}`, // 헤더에 토큰 추가
              },
            }
          )
          .then((response) => {
            console.log('RabbitMQ에 발행 요청 성공:', response.data);
          })
          .catch((error) => {
            console.error('RabbitMQ 발행 요청 실패:', error);
            return; // 요청 실패 시 더 진행하지 않음
          });

        // Socket 컴포넌트를 DOM에 추가하여 WebSocket 데이터 수신
        this.showSocketComponent = true;
        this.$nextTick(() => {
          const socketComponent = this.$refs.socketComponent;

          // WebSocket 데이터가 로드될 때까지 대기
          const checkDataLoaded = setInterval(() => {
            // 데이터가 로드되었는지 확인
            if (
              socketComponent.portfolioData &&
              Object.keys(socketComponent.portfolioData).length > 0
            ) {
              console.log('포트폴리오 데이터: ', socketComponent.portfolioData);

              // 필요한 데이터가 모두 있는지 확인
              const hasAssetData =
                socketComponent.portfolioData.assetTotal &&
                socketComponent.portfolioData.stockRevenue;

              if (hasAssetData) {
                clearInterval(checkDataLoaded); // 데이터 로딩이 완료되면 반복 종료
                // PDF 생성 시도
                socketComponent
                  .generatePDF()
                  .then(() => {
                    console.log('포트폴리오 다운로드 완료!');
                    this.showSocketComponent = false; // PDF 생성 후 Socket 컴포넌트 제거
                  })
                  .catch((error) => {
                    console.error('PDF 생성 중 오류 발생:', error);
                    alert('PDF 생성 중 오류가 발생했습니다.'); // 사용자에게 알림
                  });
              }
            }
          }, 1000); // 1초마다 데이터 로드 상태 체크
        });
      } catch (error) {
        console.error('포트폴리오 생성 중 오류 발생:', error);
      }
    },
    async fetchAssetData() {
      try {
        const token = localStorage.getItem('access_token');
        const totalResponse = await axios.get(
          'http://localhost:8080/myasset/total',
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        const investmentResponse = await axios.get(
          'http://localhost:8080/myasset/gettotalinvestment',
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        // 총 자산 계산
        this.totalAsset =
          totalResponse.data['Bank Balance'] +
          totalResponse.data['Stock Total'] +
          totalResponse.data['Car'] +
          totalResponse.data['real estate'];

        this.investmentAmount = investmentResponse.data.currentTotalStockValue;

        // 투자 순위는 아직 구현되지 않았으므로 임시로 고정값 사용
        this.investmentRank = 16;
      } catch (error) {
        console.error('자산 데이터 가져오기 실패: ', error);
        if (error.response) {
          console.error('Error status:', error.response.status);
          console.error('Error data:', error.response.data);
        } else if (error.request) {
          console.error('No response received:', error.request);
        } else {
          console.error('Error setting up request:', error.message);
        }
      }
    },
    formatCurrency(value) {
      return new Intl.NumberFormat('ko-KR').format(value);
    },
  },
};
</script>

<style scoped>
/* 상단 타이틀 */
.header {
  text-align: center;
  margin-bottom: 30px;
}

.header h1 {
  font-size: 2.5em;
  margin-bottom: 10px;
}

.header a {
  display: inline-block;
  margin-top: 10px;
  font-size: 1.2em;
  color: #007bff;
}

/* 데이터 카드 */
.data-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 40px;
}

.card {
  background-color: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  text-align: center;
  margin-right: 1px;
  width: 500px;
}

.card .amount {
  font-size: 1.5em;
  font-weight: bold;
  color: #343a40;
}

.card .highlight {
  font-size: 1.2em;
  color: #28a745;
}

/* 차트 섹션 */
.chart-section {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.chart-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.chart-row section {
  flex: 1;
  background-color: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.chart-row section:not(:last-child) {
  margin-right: 20px;
}
</style>
