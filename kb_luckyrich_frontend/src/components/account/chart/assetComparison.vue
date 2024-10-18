<template>
  <div class="charts-wrapper">
    <div class="chart">
      <highcharts :options="theirChartOptions"></highcharts>
    </div>
  </div>
</template>

<script>
import HighchartsVue from 'highcharts-vue';
import { mapGetters, mapActions } from 'vuex';

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
          height: '400px',
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
              distance: -70, // 레이블을 차트 안쪽으로 이동
            },
            size: '100%',
            innerSize: '30%'
          },
        },
        colors: ['#1EAAAA', '#FFC7AD', '#93DAFF', '#FFE150', '#FFAFE6', '#96C7ED', '#FF5675'],
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
  computed: {
    ...mapGetters(['getTheirAssets']),
  },
  watch: {
    getTheirAssets(newData) {
      if (Object.keys(newData).length) {
        this.updateChartData(newData);
      }
    }
  },
  mounted() {
    this.fetchTheirAssets();
  },
  methods: {
    ...mapActions(['fetchTheirAssets']),

    updateChartData(theirAssets) {
      const chartData = Object.entries(theirAssets).map(([key, value]) => ({
        name: key,
        y: value,
      }));

      this.theirChartOptions.series[0].data = chartData;  // 차트 데이터 업데이트
      this.$forceUpdate();  // 강제로 업데이트해버림
    },
  },
};
</script>

<style scoped></style>
