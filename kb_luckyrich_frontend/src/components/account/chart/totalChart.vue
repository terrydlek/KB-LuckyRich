<template>
  <div ref="chartContainer" class="chart-container">
    <highcharts :options="chartOptions" />
  </div>
</template>

<script>
import Highcharts from 'highcharts';
import HighchartsVue from 'highcharts-vue';
import { mapGetters, mapActions } from 'vuex';

export default {
  name: 'AssetChart',
  data() {
    return {
      chartOptions: {
        chart: {
          type: 'pie',
          height: '400px',
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
  computed: {
    ...mapGetters(['getTotalAssets']),
  },
  watch: {
    getTotalAssets(newData) {
      if (Object.keys(newData).length) {
        this.updateChartData(newData);
      }
    }
  },
  mounted() {
    this.fetchTotalAssets();
  },
  methods: {
    ...mapActions(['fetchTotalAssets']),

    updateChartData(assets) {
      const chartData = Object.keys(assets).map((key) => ({
        name: key,
        y: assets[key],
      }));

      this.chartOptions.series[0].data = chartData;
      this.chartVisible = true; // 차트 표시
      this.$forceUpdate(); // 강제로 업데이트해버림
    },
  },
};
</script>

<style></style>
