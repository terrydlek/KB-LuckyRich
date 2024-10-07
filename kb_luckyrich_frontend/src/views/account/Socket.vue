<template>
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
              <td>{{ portfolioData.assetTotal.assetTotal.totalAccount.toLocaleString() }}</td>
            </tr>
            <tr>
              <td>총 부동산 자산</td>
              <td>{{ portfolioData.assetTotal.assetTotal.totalRealestate.toLocaleString() }}</td>
            </tr>
            <tr>
              <td>총 차량 자산</td>
              <td>{{ portfolioData.assetTotal.assetTotal.totalCar.toLocaleString() }}</td>
            </tr>
            <tr>
              <td>총 주식 자산</td>
              <td>{{ portfolioData.assetTotal.assetTotal.totalStock.toLocaleString() }}</td>
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
              <td>{{ portfolioData.detailAsset.detailAsset.userCar.carPrice.toLocaleString() }}</td>
            </tr>
          </tbody>
        </table>

        <!-- 계좌 정보 -->
        <h4>계좌 정보</h4>
        <table
          v-if="portfolioData.detailAsset && portfolioData.detailAsset.detailAsset && portfolioData.detailAsset.detailAsset.userAccount">
          <thead>
            <tr>
              <th>계좌번호</th>
              <th>잔액 (₩)</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="account in portfolioData.detailAsset.detailAsset.userAccount" :key="account.accountId">
              <td>{{ account.accountNumber }}</td>
              <td>{{ account.balance.toLocaleString() }}</td>
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
              <td>{{ stock.revenue }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 오른쪽 기존 차트 -->
      <div class="right-charts">
        <div class="chart-container">
          <totalChart ref="totalChart" />
        </div>
        <div class="chart-container">
          <assetGraph ref="assetGraph" />
        </div>
        <div class="chart-container">
          <consumptionstatus ref="consumptionstatus"/>
        </div>
      </div>
    </div>
  </div>
</template>



<script>
import axios from 'axios';
import { ref, onMounted } from 'vue';
import html2pdf from 'html2pdf.js';
import totalChart from '@/components/account/chart/totalChart.vue';
import goalChart from '@/components/account/chart/goalChart.vue';
import assetGraph from '@/components/account/chart/assetGraph.vue';
import accountBookChart from '@/components/account/chart/accountBookChart.vue';
import assetcomparison from '@/components/account/chart/assetComparison.vue';
import consumptionstatus from '@/components/account/chart/consumptionstatus.vue';

import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';

// JWT 토큰을 로컬 스토리지에서 가져오는 함수
function getToken() {
  return localStorage.getItem('access_token');
}

export default {
  components: {
    totalChart,
    goalChart,
    assetGraph,
    accountBookChart,
    assetcomparison,
    consumptionstatus,
  },
  setup(props, { root }) {
    const username = ref('');
    const email = ref('');
    const age = ref('');
    const gender = ref('');
    const portfolioData = ref({});
    const isDataLoaded = ref(false);  // 데이터를 다 받아왔는지 상태를 저장

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
            alert(portfolioData.value);
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
        .get('http://localhost:8080/user/inf', {
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
    }

    onMounted(() => {
      connectWebSocket();
      fetchUserInfo();
    });

    return {
      username,
      email,
      age,
      gender,
      portfolioData,
      isDataLoaded,
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
            jsPDF: { unit: 'in', format: 'a1', orientation: 'landscape' },
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
  },
};
</script>

<style scoped>
#element-to-print {
  font-family: 'Noto Sans KR', sans-serif;
  background-color: #f9f9f9;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
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
  width: 60%;
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
  width: 35%;
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

/* @media (max-width: 1200px) {
  .content-wrapper {
    flex-direction: column;
  }

  .left-table,
  .right-charts {
    width: 100%;
  }
} */
</style>
