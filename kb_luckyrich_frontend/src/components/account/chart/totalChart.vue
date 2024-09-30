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
                    data: [],
                }]
            }
        };
    },
    mounted() {
        this.fetchAssetData();
    },
    methods: {
        fetchAssetData() {
            const token = localStorage.getItem('access_token');
            axios.get('http://localhost:8080/myasset/total', {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            })
                .then(response => {
                    const assets = response.data; // 백엔드에서 받은 데이터
                    const chartData = Object.keys(assets).map(key => ({
                        name: key,
                        y: assets[key],
                    }));
                    this.chartOptions.series[0].data = chartData; // 차트 데이터 업데이트
                })
                .catch(error => {
                    console.error('Error fetching asset data: ', error);
                });
        }
    }
}
</script>

<style></style>
