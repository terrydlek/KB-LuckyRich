<template>
    <highcharts :options="chartOptions" />
</template>

<script>
import Highcharts from 'highcharts'
import HighchartsVue from 'highcharts-vue'

export default {
    name: 'AssetChart',
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
                { name: 'Cash', y: parseFloat(getRandomNumber(20000, 1000000)) }
            ];
        }
    },
    data() {
        return {
            chartOptions: {
                chart: {
                    type: 'pie',
                    height: '100%'
                },
                title: {
                    text: '자산 분포'
                },
                tooltip: {
                    valueSuffix: '$'
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
                            }
                        }
                    }
                },
                series: [{
                    name: 'Total Assets',
                    colorByPoint: true,
                    data: this.input_function()
                }]
            }
        };
    }
}
</script>

<style></style>
