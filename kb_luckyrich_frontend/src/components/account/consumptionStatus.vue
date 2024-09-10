<template>
  <h1>Consumption Status by Category</h1>
  <highcharts :options="chartOptions" />
</template>

<script>
import Highcharts from 'highcharts';
import HighchartsVue from 'highcharts-vue';

export default {
  name: 'ConsumptionStatusChart',
  methods: {
    input_function() {
      function getRandomNumber(min, max) {
        return (Math.random() * (max - min) + min).toFixed(2);
      }
      return [
        {
          name: '보험/세금/기타 금융',
          y: parseFloat(getRandomNumber(10000, 500000)),
        },
        { name: '쇼핑', y: parseFloat(getRandomNumber(20000, 1000000)) },
        {
          name: '편의점/마트/잡화',
          y: parseFloat(getRandomNumber(20000, 1000000)),
        },
        {
          name: '카테고리 없음',
          y: parseFloat(getRandomNumber(20000, 1000000)),
        },
      ];
    },
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
          text: 'Browser<br>shares<br>January<br>2022',
          align: 'center',
          verticalAlign: 'middle',
          y: 60,
          style: {
            fontSize: '1.1em',
          },
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
            data: this.input_function(),
          },
        ],
      },
    };
  },
};
</script>
<style></style>
