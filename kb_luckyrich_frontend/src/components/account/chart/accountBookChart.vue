<template>
    <highcharts :options="chartOptions" />
</template>

<script>
import Highcharts from 'highcharts'
import HighchartsVue from 'highcharts-vue';
import axios from 'axios';

export default {
    name: 'AccountBookChart',
    data() {
        return {
            chartOptions: {
                chart: {
                    type: 'pie',
                    height: '70%'
                },
                title: {
                    text: '계좌 보유 잔액'
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
                    data: []
                }]
            }
        };
    },
    mounted() {
        this.fetchAccountData();
    },
    methods: {
        fetchAccountData() {
            const token = localStorage.getItem('access_token');
            axios.get('http://localhost:8080/myasset/accounts', {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            })
                .then(response => {
                    const assets = response.data;
                    console.log(assets);
                    const chartData = Object.keys(assets).map(key => ({
                        name: key,
                        y: assets[key],
                    }));
                    this.chartOptions.series[0].data = chartData;
                })
                .catch(error => {
                    console.error('Error fetching asset data: ', error);
                });
        }
    }
}
</script>


<style></style>