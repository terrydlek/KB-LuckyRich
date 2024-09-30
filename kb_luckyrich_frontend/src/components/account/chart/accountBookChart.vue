<template>
    <highcharts :options="chartOptions" />
</template>


<script>
import Highcharts from 'highcharts'

export default {
    name: 'AccountBookChart',
    methods: {
        input_function() {
            function getRandomNumber(min, max) {
                return (Math.random() * (max - min) + min).toFixed(2);
            }

            return [
                { name: 'A 은행', y: parseFloat(getRandomNumber(100000, 500000)) },
                { name: 'B 은행', y: parseFloat(getRandomNumber(100000, 500000)) },
                { name: 'C 은행', y: parseFloat(getRandomNumber(100000, 500000)) },
                { name: 'D 은행', y: parseFloat(getRandomNumber(100000, 500000)) },
                { name: 'E 은행', y: parseFloat(getRandomNumber(100000, 500000)) },
                { name: 'F 은행', y: parseFloat(getRandomNumber(100000, 500000)) },
                { name: 'G 은행', y: parseFloat(getRandomNumber(100000, 500000)) }
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
                    data: this.input_function()
                }]
            }
        };
    },
    // mounted() {
    //     this.fetchAccountData();
    // },
    // methods: {
    //     fetchAccountData() {
    //         const token = localStorage.getItem('access_token');
    //         axios.get('http://localhost:8080/myasset/account', {
    //             headers: {
    //                 Authorization: `Bearer ${token}`,
    //             },
    //         })
    //             .then(response => {
    //                 const assets = response.data; // 백엔드에서 받은 데이터
    //                 const chartData = Object.keys(assets).map(key => ({
    //                     name: key,
    //                     y: assets[key],
    //                 }));
    //                 this.chartOptions.series[0].data = chartData; // 차트 데이터 업데이트
    //             })
    //             .catch(error => {
    //                 console.error('Error fetching asset data: ', error);
    //             });
    //     }
    // }
}

</script>

<style>
</style>