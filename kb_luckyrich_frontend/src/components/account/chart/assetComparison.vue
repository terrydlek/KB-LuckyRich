<template>
  <div class="charts-wrapper">
    <div class="chart">
      <highcharts :options="theirChartOptions"></highcharts>
    </div>
    <vr class="vr" />
    <div class="chart">
      <highcharts :options="myChartOptions"></highcharts>
    </div>
  </div>
  <hr />
  <table class="assetPercentage-wrapper">
    <thead>
      <tr>
        <th>또래</th>
        <th>자산 유형</th>
        <th>나</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="item in comparisonData" :key="item.name">
        <td>{{ item.theirPercentage.toFixed(1) }}%</td>
        <td>{{ item.name }}</td>
        <td>{{ item.myPercentage.toFixed(1) }}%</td>
      </tr>
    </tbody>
  </table>
</template>

<script>
import Highcharts from 'highcharts';
import HighchartsVue from 'highcharts-vue';

export default {
  name: 'AssetComparisonChart',
  components: {
    highcharts: HighchartsVue.Chart,
  },
  methods: {
    input_function() {
      function getRandomNumber(min, max) {
        return (Math.random() * (max - min) + min).toFixed(2);
      }

      return [
        { name: 'Bank Balance', y: parseFloat(getRandomNumber(10000, 500000)) },
        { name: 'Stock Total', y: parseFloat(getRandomNumber(20000, 1000000)) },
        { name: 'Car', y: parseFloat(getRandomNumber(20000, 1000000)) },
        { name: 'Real Estate', y: parseFloat(getRandomNumber(20000, 1000000)) },
        { name: 'Cash', y: parseFloat(getRandomNumber(20000, 1000000)) },
      ];
    },

    calculatePercentages(data) {
      const total = data.reduce((sum, item) => sum + item.y, 0);
      return data.map((item) => ({
        ...item,
        percentage: (item.y / total) * 100,
      }));
    },
  },
  data() {
    return {
      theirChartOptions: {
        chart: {
          type: 'pie',
        },
        title: {
          text: '또래',
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
            name: 'Asset Rate',
            colorByPoint: true,
            data: this.input_function(),
          },
        ],
      },
      myChartOptions: {
        chart: {
          type: 'pie',
        },
        title: {
          text: '나',
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
            name: 'Asset Rate',
            colorByPoint: true,
            data: this.input_function(),
          },
        ],
      },
    };
  },
  computed: {
    comparisonData() {
      const myData = this.calculatePercentages(
        this.myChartOptions.series[0].data
      );
      const theirData = this.calculatePercentages(
        this.theirChartOptions.series[0].data
      );

      return myData.map((item, index) => ({
        name: item.name,
        myPercentage: item.percentage,
        theirPercentage: theirData[index].percentage,
      }));
    },
  },
};
</script>

<style scoped>
.charts-wrapper {
  display: flex;
  justify-content: space-between;
}

.chart {
  flex: 1; /* 각 차트가 동일한 비율로 공간 차지 */
  margin: 0 5px;
}

.vr {
  display: block;
  width: 3px;
  background-color: #000;
  position: center;
  top: 0;
  bottom: 0;
}

.assetPercentage-wrapper {
  border-collapse: separate;
  width: 80%;
  max-width: 600px;
  margin: 0 auto;
}

.assetPercentage-wrapper th,
.assetPercentage-wrapper td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: center;
}

@media (max-width: 768px) {
  .charts-wrapper {
    flex-direction: column;
  }

  .chart {
    margin-bottom: 20px;
  }
}
</style>
