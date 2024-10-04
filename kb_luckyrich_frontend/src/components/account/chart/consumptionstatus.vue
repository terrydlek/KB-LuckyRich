<template>
  <div>
    <highcharts v-if="!error" :options="chartOptions" />
    <p v-if="error" class="error-message">{{ error }}</p>
  </div>
</template>

<script>
import { defineComponent } from 'vue';
import Highcharts from 'highcharts';
import HighchartsVue from 'highcharts-vue';
import axios from 'axios';

export default defineComponent({
  name: 'ConsumptionStatusChart',
  components: {
    Highcharts: HighchartsVue.Chart,
  },
  data() {
    return {
      chartOptions: {
        chart: {
          type: 'pie',
          plotBackgroundColor: null,
          plotBorderWidth: 0,
          plotShadow: false,
        },
        title: {
          text: '카테고리별<br>소비 현황',
          align: 'center',
          verticalAlign: 'middle',
          y: 60,
        },
        tooltip: {
          pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>',
        },
        accessibility: {
          point: {
            valueSuffix: '%',
          },
        },
        plotOptions: {
          pie: {
            dataLabels: {
              enabled: true,
              distance: 30,
              style: {
                fontWeight: 'bold',
                color: 'black',
              },
            },
            startAngle: -90,
            endAngle: 90,
            center: ['50%', '75%'],
            size: '110%',
            innerSize: '50%',
          },
        },
        series: [
          {
            name: '소비 비율',
            colorByPoint: true,
            data: [],
          },
        ],
      },
      error: null,
    };
  },
  mounted() {
    this.fetchCategoryExpenses();
  },
  methods: {
    async fetchCategoryExpenses() {
      const token = localStorage.getItem('access_token');
      if (!token) {
        this.error = '인증 토큰을 찾을 수 없습니다. 다시 로그인해 주세요.';
        console.error('토큰을 찾을 수 없습니다.');
        return;
      }

      try {
        const response = await axios.get(
          'http://localhost:8080/myasset/getCategoryExpenses',
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        const { categoryExpenses, totalExpense } = response.data;

        if (!categoryExpenses || !totalExpense) {
          throw new Error('Invalid data received from the server');
        }

        this.updateChartData(categoryExpenses, totalExpense);
        this.error = null;
      } catch (error) {
        console.error('Error fetching category expenses:', error);
        if (error.response && error.response.status === 401) {
          this.error = '인증이 만료되었습니다. 다시 로그인해 주세요.';
          // 여기서 로그인 페이지로 리다이렉트하거나 로그인 모달을 표시할 수 있습니다
        } else {
          this.error =
            '데이터를 가져오는 데 실패했습니다. 잠시 후 다시 시도해 주세요.';
        }
      }
    },
    updateChartData(categoryExpenses, totalExpense) {
      const chartData = categoryExpenses.map((item) => ({
        name: item.category,
        y: (item.amount / totalExpense) * 100, // 백분율 계산
      }));

      this.chartOptions.series[0].data = chartData;
    },
  },
});
</script>

<style scoped>
.error-message {
  color: red;
  text-align: center;
}
</style>
