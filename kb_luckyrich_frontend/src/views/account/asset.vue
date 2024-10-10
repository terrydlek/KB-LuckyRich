<template>
  <div class="dashboard-container">
    <!-- 상단 타이틀과 설명 -->
    <div class="header">
      <h1>내 자산을 쉽고 똑똑하게 관리하세요.</h1>
    </div>
    <center>
      <div id="app">
        <div>
          <button @click="generatePortfolioPDF">자산 포트폴리오 다운로드</button>
        </div>
      </div>
    </center>

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


    <!-- 자산 관련 데이터 테이블 -->
    <div class="data-tables" style="display: flex;">
      <!-- 왼쪽 섹션 -->
      <div class="left-section" style="flex: 3; margin-right: 10px;">
        <h5>총 자산 정보</h5>
        <table>
          <thead>
            <tr>
              <th>항목</th>
              <th>금액 (원)</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>은행 잔고</td>
              <td>{{ formatCurrency(totalAssets['Bank Balance']) }}</td>
            </tr>
            <tr>
              <td>주식 총액</td>
              <td>{{ formatCurrency(totalAssets['Stock Total']) }}</td>
            </tr>
            <tr>
              <td>자동차</td>
              <td>{{ formatCurrency(totalAssets['Car']) }}</td>
            </tr>
            <tr>
              <td>부동산</td>
              <td>{{ formatCurrency(totalAssets['real estate']) }}</td>
            </tr>
          </tbody>
        </table>

        <h5>계좌 보유 잔액</h5>
        <table>
          <thead>
            <tr>
              <th>은행명</th>
              <th>계좌 번호</th>
              <th>잔액 (원)</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="account in accountData" :key="account.accountNumber">
              <td>
                <span v-if="account.bankId === 1">국민은행</span>
                <span v-else-if="account.bankId === 2">카카오뱅크</span>
                <span v-else-if="account.bankId === 3">신한은행</span>
                <span v-else>알 수 없음</span>
              </td>
              <td>{{ account.accountNumber }}</td>
              <td>{{ formatCurrency(account.balance) }}</td>
            </tr>
          </tbody>
        </table>
        <h5>자본 증감 추이 (주식 + 은행 잔고)</h5>
        <table>
          <thead>
            <tr>
              <th>날짜</th>
              <th>자산 변동 (원)</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="([key, value], index) in sortedAssetData" :key="index">
              <td>{{ key }}</td>
              <td>{{ formatCurrency(value) }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 오른쪽 섹션 -->
      <div class="right-section" style="flex: 2; margin-left: 20px;">
        <!-- <p>.</p> <br>
        <p>.</p> <br> -->
        <!-- {{ products }} -->
        <!-- <p>은행 잔고 + 총 보유 주식: {{ totalAssets['Bank Balance'] + totalAssets['Stock Total'] }}</p> -->
        <h5>고객님의 은행 잔고와 주식 총액을 계산하여 상품을 추천해드릴게요.</h5>
        <table v-if="products.length" style="width: 400px;">
          <thead>
            <tr>
              <th style="width: 200px;">상품명</th>
              <th style="width: 100px;">{{ totalAssetsValue < 5000000 ? '이자' : '가격' }}</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(product, index) in products" :key="index">
              <td><a :href="`${product.link}`">{{ truncatedName(product.name) }}</a></td>
              <td>{{ product.performance }}</td>
            </tr>
          </tbody>
        </table>
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
import { mapState, mapActions } from 'vuex';
import axios from 'axios';
import totalChart from '@/components/account/chart/totalChart.vue';
import goalChart from '@/components/account/chart/goalChart.vue';
import assetGraph from '@/components/account/chart/assetGraph.vue';
import accountBookChart from '@/components/account/chart/accountBookChart.vue';
import assetcomparison from '@/components/account/chart/assetComparison.vue';
import consumptionstatus from '@/components/account/chart/consumptionstatus.vue';
import Socket from '@/views/account/Socket.vue';

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
  computed: {
    ...mapState(['totalAssets', 'assetData', 'accountData']),
    sortedAssetData() {
      return Object.entries(this.assetData).sort((a, b) => {
        const dateA = new Date(a[0]);
        const dateB = new Date(b[0]);
        return dateB - dateA;  // 내림차순 정렬
      });
    },
    totalAssetsValue() {
      return 20000000;
      // return this.totalAssets['Bank Balance'] + this.totalAssets['Stock Total'];
    },
  },
  methods: {
    ...mapActions(['fetchAssetData', 'fetchAccountData', 'fetchTotalAssets']),
    formatCurrency(value) {
      return new Intl.NumberFormat('ko-KR').format(value);
    },
  },
  mounted() {
    this.fetchAssetData();
    this.fetchAccountData();
    this.fetchTotalAssets();
    this.fetchTotalAssets().then(() => {
      this.fetchProducts();
    });
  },
  data() {
    return {
      showSocketComponent: false,
      totalAsset: 0,
      investmentAmount: 0,
      investmentRank: 0,
      products: [],
      loading: false,
      error: null,
    };
  },
  mounted() {
    this.fetchAssetData();
    this.fetchTotalAssets().then(() => {
      this.fetchProducts();
    });
  },
  methods: {
    ...mapActions(['fetchTotalAssets']),
    truncatedName(name) {
      return name.length > 25 ? name.slice(0, 25) + '...' : name;
    },
    async fetchProducts() {
      try {
        this.loading = true;
        const token = localStorage.getItem('access_token');

        if (this.totalAssetsValue < 5000000) {
          // 예적금 상품 20개
          const response = await axios.get('http://localhost:8080/recommend/steadiness', {
            headers: { Authorization: `Bearer ${token}` },
          });
          this.products = response.data.slice(0, 20).map((item) => ({
            name: item.prodname,
            category: '예적금',
            performance: item.bestinterest,
            link: "/luckyrich/recommend/steadiness/" + item.prodname,
          }));
        } else if (this.totalAssetsValue >= 5000000 && this.totalAssetsValue < 20000000) {
          // 재간접 펀드 20개
          const response = await axios.get('http://localhost:8080/api/funds?riskRating=2');
          this.products = response.data.slice(0, 20).map((item) => ({
            name: item.name,
            category: '재간접 펀드',
            performance: item.lastPrice,
            link: "/luckyrich/recommend/funds/" + encodeURIComponent(item.url),
          }));
        } else if (this.totalAssetsValue >= 20000000 && this.totalAssetsValue < 50000000) {
          // 채권형 펀드 20개
          const response = await axios.get('http://localhost:8080/api/funds?riskRating=3');
          this.products = response.data.slice(0, 20).map((item) => ({
            name: item.name,
            category: '채권형 펀드',
            performance: item.lastPrice,
            link: "/luckyrich/recommend/funds/" + encodeURIComponent(item.url),
          }));
        } else if (this.totalAssetsValue >= 50000000 && this.totalAssetsValue < 100000000) {
          // 주식 20개
          const response = await axios.get('http://localhost:8080/recommend/active', {
            headers: { Authorization: `Bearer ${token}` },
          });
          this.products = response.data.slice(0, 20).map((item) => ({
            name: item.stockName,
            category: '주식',
            performance: item.stockPrice,
            link: "/luckyrich/recommend/active/" + item.stockCode,
          }));
        } else {
          // 암호화폐 20개
          const response = await fetch('https://api.coinpaprika.com/v1/tickers?quotes=KRW');
          const data = await response.json();
          this.products = data.slice(0, 20).map((item) => ({
            name: item.name,
            category: '암호화폐',
            performance: item.quotes.KRW.percent_change_24h + '%',
            link: '/luckyrich/coin/' + item.id,
          }));
        }
      } catch (error) {
        console.error('상품 데이터를 불러오는 데 실패했습니다.', error);
        this.error = '상품 데이터를 불러오는 데 실패했습니다.';
      } finally {
        this.loading = false;
      }
      console.log(this.products);
    },





    async generatePortfolioPDF() {
      try {
        const token = localStorage.getItem('access_token');

        // RabbitMQ에 메시지 발행 요청 (http://localhost:8080/rabbit/portfolios)
        await axios.post(
          'http://localhost:8080/rabbit/portfolios',
          {},
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        console.log('RabbitMQ에 발행 요청 성공');

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
          'http://localhost:8080/myasset/totalInvestment',
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
  margin-top: 20px;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
  margin-bottom: 20px;
  width: 1px;
}

.card {
  background-color: #f8f9fa;
  padding: 45px;
  border-radius: 16px;
  text-align: center;
  margin-right: 1px;
  width: 300px;
  margin-left: 10px;
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

.data-tables table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

.data-tables th,
.data-tables td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

.data-tables th {
  background-color: #f4f4f4;
}

/* .dashboard-container {
  width: 1000px;
} */
</style>
