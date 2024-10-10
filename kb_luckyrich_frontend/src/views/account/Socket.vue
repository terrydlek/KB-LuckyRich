<template>
  <center>
    <div id="element-to-print" style="padding: 20px; border: 1px solid #ccc">
      <h3>자산 포트폴리오</h3>
      <div class="accountInfo" v-if="username && age && email && gender">
        이름: {{ username }} 나이: {{ age }} 이메일: {{ email }} 성별: {{ gender }}
      </div>

      <!-- 왼쪽 자산 표 -->
      <div class="content-wrapper">
        <div class="left-table">
          <!-- 총 자산 -->
          <h4>총 자산</h4>
          <table v-if="portfolioData.assetTotal && portfolioData.assetTotal.assetTotal">
            <thead>
              <tr>
                <th>항목</th>
                <th>금액 (₩)</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>총 계좌 자산</td>
                <td>{{ portfolioData.assetTotal.assetTotal.totalAccount.toLocaleString() }} 원</td>
              </tr>
              <tr>
                <td>총 부동산 자산</td>
                <td>{{ portfolioData.assetTotal.assetTotal.totalRealestate.toLocaleString() }} 원</td>
              </tr>
              <tr>
                <td>총 차량 자산</td>
                <td>{{ portfolioData.assetTotal.assetTotal.totalCar.toLocaleString() }} 원</td>
              </tr>
              <tr>
                <td>총 주식 자산</td>
                <td>{{ portfolioData.assetTotal.assetTotal.totalStock.toLocaleString() }} 원</td>
              </tr>
            </tbody>
          </table>

          <!-- 부동산 정보 -->
          <h4>부동산 정보</h4>
          <table
            v-if="portfolioData.detailAsset && portfolioData.detailAsset.detailAsset && portfolioData.detailAsset.detailAsset.userRealestate">
            <thead>
              <tr>
                <th>항목</th>
                <th>세부 정보</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>부동산 위치</td>
                <td>{{ portfolioData.detailAsset.detailAsset.userRealestate.city }}, {{
                  portfolioData.detailAsset.detailAsset.userRealestate.streetName }}</td>
              </tr>
              <tr>
                <td>부동산 이름</td>
                <td>{{ portfolioData.detailAsset.detailAsset.userRealestate.estateName }}</td>
              </tr>
              <tr>
                <td>거래 금액</td>
                <td>{{ portfolioData.detailAsset.detailAsset.userRealestate.transactionAmount.toLocaleString() }}</td>
              </tr>
              <tr>
                <td>전용 면적 (㎡)</td>
                <td>{{ portfolioData.detailAsset.detailAsset.userRealestate.exclusiveArea }}</td>
              </tr>
            </tbody>
          </table>

          <!-- 차량 정보 -->
          <h4>차량 정보</h4>
          <table
            v-if="portfolioData.detailAsset && portfolioData.detailAsset.detailAsset && portfolioData.detailAsset.detailAsset.userCar">
            <thead>
              <tr>
                <th>항목</th>
                <th>세부 정보</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>차량 모델</td>
                <td>{{ portfolioData.detailAsset.detailAsset.userCar.carModel }}</td>
              </tr>
              <tr>
                <td>차량 가격</td>
                <td>{{ portfolioData.detailAsset.detailAsset.userCar.carPrice.toLocaleString() }} 원</td>
              </tr>
            </tbody>
          </table>

          <!-- 계좌 정보 -->
          <h4>계좌 정보</h4>
          <table
            v-if="portfolioData.detailAsset && portfolioData.detailAsset.detailAsset && portfolioData.detailAsset.detailAsset.userAccount">
            <thead>
              <tr>
                <th>은행명</th>
                <th>계좌 유형</th>
                <th>계좌번호</th>
                <th>잔액 (₩)</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="account in portfolioData.detailAsset.detailAsset.userAccount" :key="account.accountId">
                <td>
                  <span v-if="account.bankId === 1">국민은행</span>
                  <span v-else-if="account.bankId === 2">카카오뱅크</span>
                  <span v-else-if="account.bankId === 3">신한은행</span>
                  <span v-else>알 수 없음</span> <!-- 다른 bankId의 경우 -->
                </td>
                <td>
                  <span v-if="account.accountTypeId === 1">청년도약</span>
                  <span v-if="account.accountTypeId === 2">주택청약</span>
                  <span v-if="account.accountTypeId === 3">자유적금</span>
                  <span v-if="account.accountTypeId === 4">입출금통장</span>
                  <span v-if="account.accountTypeId === 5">비상금통장</span>
                </td>
                <td>{{ account.accountNumber }}</td>
                <td>{{ account.balance.toLocaleString() }} 원</td>
              </tr>
            </tbody>
          </table>

          <!-- 주식 보유 현황 -->
          <h4>주식 보유 현황</h4>
          <table v-if="portfolioData.stockRevenue && portfolioData.stockRevenue.stockRevenue">
            <thead>
              <tr>
                <th>종목명</th>
                <th>보유수량</th>
                <th>매입가 (₩)</th>
                <th>수익률</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(stock, index) in portfolioData.stockRevenue.stockRevenue" :key="index">
                <td>{{ stock.stockName }}</td>
                <td>{{ stock.quantity }}</td>
                <td>{{ stock.purchasePrice.toLocaleString() }}</td>
                <td>
                  <span v-if="stock.revenue.startsWith('-')" style="color: red;">
                    ▼ {{ stock.revenue }}
                  </span>
                  <span v-else style="color: green;">
                    ▲ {{ stock.revenue }}
                  </span>
                </td>
              </tr>

            </tbody>
          </table>

          <!-- 자산 변동 내역 -->
          <h4>자산 변동 내역</h4>
          <table v-if="portfolioData.idTrend">
            <thead>
              <tr>
                <th>날짜</th>
                <th>자산 금액 (₩)</th>
                <th>변동</th>
                <th>변동률 (%)</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(date, index) in sortedDates" :key="date">
                <td>{{ date }}</td>
                <td>{{ portfolioData.idTrend[date].toLocaleString() }}</td>
                <td>
                  <span v-if="index > 0">
                    <span v-if="portfolioData.idTrend[date] - portfolioData.idTrend[sortedDates[index + 1]] > 0"
                      style="color: green;">▲ {{ (portfolioData.idTrend[date] - portfolioData.idTrend[sortedDates[index
                        +
                        1]]).toLocaleString() }}</span>
                    <span v-else-if="portfolioData.idTrend[date] - portfolioData.idTrend[sortedDates[index + 1]] < 0"
                      style="color: red;">▼ {{ (portfolioData.idTrend[sortedDates[index + 1]] -
                        portfolioData.idTrend[date]).toLocaleString() }}</span>
                    <span v-else>= 0</span>
                  </span>
                </td>
                <td>
                  <span v-if="index > 0">
                    <span v-if="portfolioData.idTrend[date] - portfolioData.idTrend[sortedDates[index + 1]] > 0"
                      style="color: green;">+{{ (((portfolioData.idTrend[date] - portfolioData.idTrend[sortedDates[index
                        +
                        1]]) / portfolioData.idTrend[sortedDates[index + 1]]) * 100).toFixed(2) }}%</span>
                    <span v-else-if="portfolioData.idTrend[date] - portfolioData.idTrend[sortedDates[index + 1]] < 0"
                      style="color: red;">{{ (((portfolioData.idTrend[date] - portfolioData.idTrend[sortedDates[index +
                        1]]) / portfolioData.idTrend[sortedDates[index + 1]]) * 100).toFixed(2) }}%</span>
                    <span v-else>0%</span>
                  </span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- <div class="advice-section">
          <div class="advice-box">
            <h4>Lucky Rich AI's 재정 조언</h4>
            <p>현재 자산 총액은 6,970,000원이며, 이 중 주식에 2,364,240원이 투자되어있습니다. 부동산과 차량에는 자산이 없는 상태입니다. 재무 계획은 다음과 같습니다. 먼저, 투자
              포트폴리오
              다변화를 위해 부동산 또는 투자 신탁 상품에 일부 자산을 배분하는 것을 고려하세요. 또한, 주식 시장의 변동성에 대비해 안전 자산인 채권이나 예금 상품에 분산 투자하는 방법을 추천합니다.
              중장기적으로는 재무 목표를 설정하고, 매월 일정 금액을 적립하여 비상 자금을 마련하는 것이 중요합니다. 이를 통해 안정적인 재무 기반을 구축할 수 있을 것입니다. 전문가의 조언을 받아
              정기적으로
              포트폴리오를 점검하고 조정하는 것도 잊지 마세요.</p>
          </div>
          <div class="advice-box">
            <h4>Lucky Rich AI's 투자 조언</h4>
            <p>현재 데이터는 특정 날짜에 따른 투자 지표를 보여줍니다. 특히 2024년 10월 4일의 급격한 하락(2789440)은 주목할 필요가 있으며, 이는 시장의 변동성을 나타냅니다. 반면, 10월
              7일에는 다시 상승세(3421440)를 보였습니다. 이는 일시적인 조정일 가능성이 있으므로 투자 포지션을 점검하고, 리스크 관리를 강화하는 것이 중요합니다. 또한, 안정적인 기업이나
              자산으로의
              분산
              투자 전략을 고려해보시는 것이 좋습니다. 시장 트렌드와 경제 지표에 주의하며, 적절한 시점에 추가 매수 또는 매도를 결정하는 것이 바람직합니다.</p>
          </div>

          <div class="advice-box">
            <h4>Lucky Rich AI's MZ 세대 평균 자산 분석</h4>
            <p>MZ 세대(밀레니얼+Z세대)의 평균 자산 현황은 세대 내 경제적 격차가 크다는 특징을 보입니다. 20대는 학자금 대출, 저축 부족 등으로 평균 자산이 낮은 반면, 30대는 직장 경력 축적과
              주택 마련 등으로 자산이 증가하는 추세입니다. 통계청 자료에 따르면, 30대 가구의 평균 자산은 약 4억 원대이며, 주요 자산 구성 요소는 부동산과 금융 자산입니다. 하지만, 높은 주거
              비용과 불안정한 고용 상황은 자산 축적의 주요 어려움으로 작용하고 있습니다. MZ 세대는 자산 증대를 위해 주식, 암호화폐 등 고위험 투자에 관심이 높습니다.</p>
          </div>

          <div class="advice-box">
            <h4>Lucky Rich AI's 투자 조언</h4>
            <p>현재 데이터는 특정 날짜에 따른 투자 지표를 보여줍니다. 특히 2024년 10월 4일의 급격한 하락(2789440)은 주목할 필요가 있으며, 이는 시장의 변동성을 나타냅니다. 반면, 10월
              7일에는 다시 상승세(3421440)를 보였습니다. 이는 일시적인 조정일 가능성이 있으므로 투자 포지션을 점검하고, 리스크 관리를 강화하는 것이 중요합니다. 또한, 안정적인 기업이나
              자산으로의
              분산
              투자 전략을 고려해보시는 것이 좋습니다. 시장 트렌드와 경제 지표에 주의하며, 적절한 시점에 추가 매수 또는 매도를 결정하는 것이 바람직합니다.</p>
          </div>

          <div class="advice-box">
            <h4>Lucky Rich AI's 투자 조언</h4>
            <p>현재 데이터는 특정 날짜에 따른 투자 지표를 보여줍니다. 특히 2024년 10월 4일의 급격한 하락(2789440)은 주목할 필요가 있으며, 이는 시장의 변동성을 나타냅니다. 반면, 10월
              7일에는 다시 상승세(3421440)를 보였습니다. 이는 일시적인 조정일 가능성이 있으므로 투자 포지션을 점검하고, 리스크 관리를 강화하는 것이 중요합니다. 또한, 안정적인 기업이나
              자산으로의
              분산
              투자 전략을 고려해보시는 것이 좋습니다. 시장 트렌드와 경제 지표에 주의하며, 적절한 시점에 추가 매수 또는 매도를 결정하는 것이 바람직합니다.</p>
          </div>

          <div class="advice-box">
            <h4>Lucky Rich AI's 투자 조언</h4>
            <p>현재 데이터는 특정 날짜에 따른 투자 지표를 보여줍니다. 특히 2024년 10월 4일의 급격한 하락(2789440)은 주목할 필요가 있으며, 이는 시장의 변동성을 나타냅니다. 반면, 10월
              7일에는 다시 상승세(3421440)를 보였습니다. 이는 일시적인 조정일 가능성이 있으므로 투자 포지션을 점검하고, 리스크 관리를 강화하는 것이 중요합니다. 또한, 안정적인 기업이나
              자산으로의
              분산
              투자 전략을 고려해보시는 것이 좋습니다. 시장 트렌드와 경제 지표에 주의하며, 적절한 시점에 추가 매수 또는 매도를 결정하는 것이 바람직합니다.</p>
          </div>

          <div class="advice-box">
            <h4>Lucky Rich AI's 투자 조언</h4>
            <p>현재 데이터는 특정 날짜에 따른 투자 지표를 보여줍니다. 특히 2024년 10월 4일의 급격한 하락(2789440)은 주목할 필요가 있으며, 이는 시장의 변동성을 나타냅니다. 반면, 10월
              7일에는 다시 상승세(3421440)를 보였습니다. 이는 일시적인 조정일 가능성이 있으므로 투자 포지션을 점검하고, 리스크 관리를 강화하는 것이 중요합니다. 또한, 안정적인 기업이나
              자산으로의
              분산
              투자 전략을 고려해보시는 것이 좋습니다. 시장 트렌드와 경제 지표에 주의하며, 적절한 시점에 추가 매수 또는 매도를 결정하는 것이 바람직합니다.</p>
          </div>

        </div> -->

        <div class="advice-section" v-if="portfolioData.advice">

          <div class="advice-box">
            <h4>Lucky Rich AI's 재정 조언</h4>
            <p>{{ portfolioData.advice.financePlan }}</p>
          </div>

          <div class="advice-box">
            <h4>Lucky Rich AI's 투자 조언</h4>
            <p>{{ portfolioData.advice.investPlan }}</p>
          </div>

          <div class="advice-box">
            <h4>Lucky Rich AI's 주식 조언</h4>
            <p>{{ portfolioData.advice.stockPlan }}</p>
          </div>

          <div class="advice-box">
            <h4>MZ 자산 관리 트렌드</h4>
            <p>{{ portfolioData.advice.mzTrend }}</p>
          </div>

          <div class="advice-box">
            <h4>MZ 세대 평균 자산 분석</h4>
            <p>MZ 세대(밀레니얼+Z세대)의 평균 자산 현황은 세대 내 경제적 격차가 크다는 특징을 보입니다. 20대는 학자금 대출, 저축 부족 등으로 평균 자산이 낮은 반면, 30대는 직장 경력 축적과
              주택 마련 등으로 자산이 증가하는 추세입니다. 통계청 자료에 따르면, 30대 가구의 평균 자산은 약 4억 원대이며, 주요 자산 구성 요소는 부동산과 금융 자산입니다. 하지만, 높은 주거
              비용과 불안정한 고용 상황은 자산 축적의 주요 어려움으로 작용하고 있습니다. MZ 세대는 자산 증대를 위해 주식, 암호화폐 등 고위험 투자에 관심이 높습니다.</p>
          </div>

          <div class="advice-box">
            <h4>금융 시장 트렌드 분석</h4>
            <p>{{ portfolioData.advice.financeTrend }}</p>
          </div>

          <div class="advice-box">
            <h4>투자 시장 트렌드 분석</h4>
            <p>{{ portfolioData.advice.investAdvice }}</p>
          </div>

        </div>

        <!-- 오른쪽 기존 차트 -->
        <div class="right-charts">
          <div class="chart-container">
            <totalChart ref="totalChart" />
          </div>
          <div class="chart-container">
            <img :src="savedChartImage" alt="Saved Chart Image" v-if="savedChartImage" />
            <p v-else>저장된 차트 이미지가 없습니다.</p>
          </div>
          <div class="chart-container">
            <consumptionstatus ref="consumptionstatus" />
          </div>
          <div class="chart-container">
            <accountBookChart ref="accountBookChart" />
          </div>
        </div>
      </div>

    </div>
  </center>
</template>



<script>
import axios from 'axios';
import { ref, onMounted, watch } from 'vue';
import html2pdf from 'html2pdf.js';
import totalChart from '@/components/account/chart/totalChart.vue';
import assetGraph from '@/components/account/chart/assetGraph.vue';
import goalChart from '@/components/account/chart/goalChart.vue';
import consumptionstatus from '@/components/account/chart/consumptionstatus.vue';
import accountBookChart from '@/components/account/chart/accountBookChart.vue';

import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';

// JWT 토큰을 로컬 스토리지에서 가져오는 함수
function getToken() {
  return localStorage.getItem('access_token');
}

export default {
  components: {
    totalChart,
    assetGraph,
    consumptionstatus,
    goalChart,
    accountBookChart
  },
  setup(props, { root }) {
    const username = ref('');
    const email = ref('');
    const age = ref('');
    const gender = ref('');
    const portfolioData = ref({});
    const isDataLoaded = ref(false);  // 데이터를 다 받아왔는지 상태를 저장
    const savedChartImage = ref(null);    // 로컬스토리지에서 불러온 이미지를 저장할 ref

    // WebSocket 메시지 수신 처리 함수
    function connectWebSocket() {

      const serverURL = 'http://localhost:8080/ws';
      const token = localStorage.getItem('access_token');

      const socket = new SockJS(serverURL, null, {
        headers: {
          Authorization: `Bearer ${token}`,
          'STOMP-Version': '1.2',
        },
      });
      const stompClient = Stomp.over(socket);

      stompClient.connect(
        {},
        (frame) => {
          console.log('Connected to WebSocket server:', frame);

          stompClient.subscribe('/topic/portfolio/luckyrich', (notification) => {
            const json = JSON.parse(notification.body);
            console.log('Notification received:', notification.body);
            console.log(json);

            portfolioData.value = json;
            alert("포트폴리오가 생성되었습니다. 다운로드 버튼을 한번 더 클릭해주세요.");
            isDataLoaded.value = true;  // 데이터 로딩 완료
          });
        },
        (error) => {
          console.error('Error connecting to WebSocket server:', error);
        }
      );
    }

    function fetchUserInfo() {
      axios
        .get('http://localhost:8080/user', {
          headers: {
            Authorization: `Bearer ${getToken()}`,
          },
        })
        .then((response) => {
          const data = response.data;
          username.value = data.nickName;
          email.value = data.email;
          age.value = data.age;
          gender.value = data.gender;
        })
        .catch((err) => {
          console.log(err);
        });
    };

    function loadChartImage() {
      const savedImage = localStorage.getItem('assetGrowthChart');  // 로컬스토리지에서 이미지 가져오기
      if (savedImage) {
        savedChartImage.value = savedImage;  // 가져온 이미지를 chartImage에 저장
      } else {
        console.log('저장된 차트 이미지가 없습니다.');
      }
    };


    onMounted(() => {
      connectWebSocket();
      fetchUserInfo();
      loadChartImage();
    });

    return {
      username,
      email,
      age,
      gender,
      portfolioData,
      isDataLoaded,
      savedChartImage
    };
  },
  methods: {
    generatePDF() {
      return new Promise((resolve, reject) => {
        setTimeout(() => {
          const element = document.getElementById('element-to-print');
          if (!element) {
            console.error('포트폴리오를 생성할 요소를 찾을 수 없습니다.');
            reject('포트폴리오를 생성할 요소를 찾을 수 없습니다.');
            return;
          }
          const options = {
            filename: 'asset_portfolio.pdf',
            image: { type: 'jpeg', quality: 0.98 },
            html2canvas: { scale: 2 },
            jsPDF: { unit: 'in', format: 'a1', orientation: 'landscape', margin: { right: 1, left: 1 } },
          };
          html2pdf()
            .from(element)
            .set(options)
            .save()
            .then(() => {
              resolve();
            })
            .catch((error) => {
              console.error('포트폴리오 생성 중 오류 발생:', error);
              reject(error);
            });
        }, 1000); // 렌더링 지연을 위한 1초 대기
      });
    },
    previousValue(index) {
      const dates = Object.keys(this.portfolioData.idTrend);
      return this.portfolioData.idTrend[dates[index - 1]];
    }
  },
  computed: {
    sortedDates() {
      return Object.keys(this.portfolioData.idTrend).sort((a, b) => new Date(b) - new Date(a));
    }
  }
};
</script>

<style scoped>
#element-to-print {
  font-family: 'Noto Sans KR', sans-serif;
  background-color: #f9f9f9;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  width: 100%;
  /* 또는 원하는 비율 또는 고정 넓이(px)로 설정 */
  /* max-width: 966px; */
  margin: 0;
}

h3 {
  text-align: center;
  font-size: 2rem;
  color: #2c3e50;
  margin-bottom: 20px;
}

h4 {
  font-size: 1.5rem;
  color: #34495e;
  margin-top: 20px;
  margin-bottom: 10px;
  border-bottom: 2px solid #2c3e50;
  padding-bottom: 5px;
}

.accountInfo {
  background-color: #ecf0f1;
  border: 1px solid #bdc3c7;
  padding: 15px;
  border-radius: 5px;
  margin-bottom: 20px;
}

.accountInfo span {
  font-weight: bold;
}

.content-wrapper {
  display: flex;
  justify-content: space-between;
  gap: 20px;
}

.left-table {
  width: 50%;
  background-color: #fff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;
}

table th,
table td {
  border: 1px solid #bdc3c7;
  padding: 10px;
  text-align: left;
}

table th {
  background-color: #3498db;
  color: white;
  font-weight: bold;
}

table td {
  background-color: #f5f5f5;
  color: #2c3e50;
}

table tr:hover td {
  background-color: #ecf0f1;
}

.chart-container {
  width: 100%;
  margin-bottom: 20px;
  border: 1px solid #e1e1e1;
  padding: 15px;
  background-color: #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
  border-radius: 10px;
}

.right-charts {
  width: 25%;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.right-charts .chart-container {
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #fff;
}

button {
  background-color: #2980b9;
  border: none;
  padding: 10px 20px;
  color: white;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1rem;
  transition: background-color 0.3s;
}

button:hover {
  background-color: #3498db;
}

.gpt {
  margin-top: 20px;
}

/* @media (max-width: 1200px) {
  .content-wrapper {
    flex-direction: column;
  }

  .left-table,
  .right-charts {
    width: 100%;
  }
} */

.advice-section {
  /* display: flex; */
  width: 25%;
  gap: 20px;
  margin-top: 20px;

}

.advice-box {
  flex: 1;
  background-color: #f0f0f0;
  padding: 15px;
  border-radius: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.advice-box h4 {
  font-size: 18px;
  color: #007bff;
  margin-bottom: 10px;
}

.advice-box p {
  font-size: 16px;
  color: #555;
  line-height: 1.5;
}
</style>
