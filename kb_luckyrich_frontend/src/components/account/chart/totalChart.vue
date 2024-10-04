<template>
  <highcharts :options="chartOptions" />
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
    };
  },
};
</script>

<style></style>
