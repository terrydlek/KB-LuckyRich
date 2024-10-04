<template>
  <div class="charts-wrapper">
    <div class="chart">
      <highcharts :options="theirChartOptions"></highcharts>
    </div>
  </div>
</template>

<script>
import HighchartsVue from 'highcharts-vue';
import axios from 'axios';

export default {
  name: 'AssetComparisonChart',
  components: {
    highcharts: HighchartsVue.Chart,
  },
  data() {
    return {
      theirChartOptions: {
        chart: {
          type: 'pie',
        },
        title: {
          text: '내 또래의 자산 분포',
        },
        tooltip: {
          pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>',
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
            name: 'Asset Rate',
            colorByPoint: true,
            data: [],
          },
        ],
      },
    };
  },
  mounted() {
    this.fetchTheirData();
  },
  methods: {
    fetchTheirData() {
      const token = localStorage.getItem('access_token');
      if (!token) {
        console.error('토큰을 찾을 수 없습니다.');
        return;
      }
      axios
        .get('http://localhost:8080/myasset/their-assets', {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })
        .then((response) => {
          console.log('Their Assets Data:', response.data);
          const theirAssets = response.data;
          const chartData = Object.entries(theirAssets).map(([key, value]) => ({
            name: key,
            y: value,
          }));
          this.theirChartOptions.series[0].data = chartData;
        })
        .catch((error) => {
          console.error('Error fetching their assets:', error);
          if (error.response) {
            console.error('Error data:', error.response.data);
            console.error('Error status:', error.response.status);
            console.error('Error headers:', error.response.headers);
          } else if (error.request) {
            console.error('Error request:', error.request);
          } else {
            console.error('Error message:', error.message);
          }
        });
    },
  },
};
</script>

<style scoped></style>
