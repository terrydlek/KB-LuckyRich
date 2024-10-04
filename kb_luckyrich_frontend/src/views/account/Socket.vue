<template>
  <div id="element-to-print" style="padding: 20px; border: 1px solid #ccc">
    <h3>자산 포트폴리오</h3>
    <div class="accountInfo">
      이름: {{ username }} 나이: {{ age }} 이메일: {{ email }} 성별:
      {{ gender }}
    </div>
    <div class="content-wrapper">
      <!-- 왼쪽 자산 표 -->
      <div class="left-table">
        <table>
          <thead>
            <tr>
              <th>컬럼1</th>
              <th>컬럼2</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>내용1</td>
              <td>내용2</td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 오른쪽 기존 차트 -->
      <div class="right-charts">
        <div class="chart-container">
          <totalChart />
        </div>
        <div class="chart-container">
          <assetGraph />
        </div>
        <div class="chart-container">
          <consumptionstatus />
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
  setup() {
    const username = ref('');
    const email = ref('');
    const age = ref('');
    const gender = ref('');

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
      fetchUserInfo();
    });

    return {
      username,
      email,
      age,
      gender,
    };
  },
  methods: {
    generatePDF() {
      return new Promise((resolve, reject) => {
        setTimeout(() => {
          const element = document.getElementById('element-to-print');
          if (!element) {
            console.error('PDF를 생성할 요소를 찾을 수 없습니다.');
            reject('PDF를 생성할 요소를 찾을 수 없습니다.');
            return;
          }
          const options = {
            filename: 'asset_portfolio.pdf',
            image: { type: 'jpeg', quality: 0.98 },
            html2canvas: { scale: 2 },
            jsPDF: { unit: 'in', format: 'a3', orientation: 'landscape' },
          };
          html2pdf()
            .from(element)
            .set(options)
            .save()
            .then(() => {
              console.log('PDF가 가로로 다운로드되었습니다.');
              resolve();
            })
            .catch((error) => {
              console.error('PDF 다운로드 중 오류 발생:', error);
              reject(error);
            });
        }, 1000); // 렌더링 지연을 위한 1초 대기
      });
    },
  },
};
</script>

<style scoped>
.content-wrapper {
  display: flex;
  justify-content: space-between;
}

.left-table {
  flex: 1; /* 왼쪽 섹션을 차지 */
  margin-right: 20px;
}

.right-charts {
  flex: 1; /* 오른쪽 섹션을 차지 */
}

.accountInfo {
  margin-bottom: 20px;
}

.chart-container {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.chart-container > * {
  width: 48%; /* 각 차트를 48%로 설정 */
}
</style>
