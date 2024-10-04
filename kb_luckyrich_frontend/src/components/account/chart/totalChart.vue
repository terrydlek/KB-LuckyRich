<template>
  <div ref="chartContainer" class="chart-container">
    <highcharts v-if="chartVisible" :options="chartOptions" />
    <div v-else>Loading chart...</div>
    <!-- 데이터 로딩 중 메시지 표시 -->
  </div>
</template>

<script>
import Highcharts from 'highcharts';
import HighchartsVue from 'highcharts-vue';
import axios from 'axios';

export default {
  name: 'AssetChart',
  data() {
    return {
      chartOptions: {
        chart: {
          type: 'pie',
          height: '70%',
        },
        title: {
          text: '자산 분포',
        },
        tooltip: {
          valueSuffix: '$',
        },
        plotOptions: {
          pie: {
            allowPointSelect: true,
            cursor: 'pointer',
            dataLabels: {
              enabled: true,
              format: '<b>{point.name}</b>: {point.percentage:.1f} %',
              style: {
                color: 'black',
                fontSize: '14px',
              },
            },
          },
        },
        series: [
          {
            name: 'Total Assets',
            colorByPoint: true,
            data: [],
          },
        ],
      },
      chartVisible: false, // 차트 표시 여부
    };
  },
  mounted() {
    this.fetchAssetData();
  },
  methods: {
    async fetchAssetData() {
      const token = localStorage.getItem('access_token');
      try {
        const response = await axios.get(
          'http://localhost:8080/myasset/total',
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        const assets = response.data; // 백엔드에서 받은 데이터
        const chartData = Object.keys(assets).map((key) => ({
          name: key,
          y: assets[key],
        }));

        this.chartOptions.series[0].data = chartData; // 차트 데이터 업데이트
        this.chartVisible = true; // 차트가 표시되도록 설정
      } catch (error) {
        console.error('Error fetching asset data: ', error);
      }
    },
  },
};
</script>

<style>
.chart-container {
  /* 필요에 따라 스타일 추가 */
}
</style>
