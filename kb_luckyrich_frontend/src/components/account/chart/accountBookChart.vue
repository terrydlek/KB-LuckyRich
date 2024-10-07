<template>
    <highcharts :options="chartOptions" />
</template>

<script>
import Highcharts from 'highcharts';
import HighchartsVue from 'highcharts-vue';
import { mapGetters, mapActions } from 'vuex';

export default {
    name: 'AccountBookChart',
    data() {
        return {
            chartOptions: {
                chart: {
                    type: 'pie',
                    height: '400px'
                },
                title: {
                    text: '계좌 보유 잔액'
                },
                tooltip: {
                    valueSuffix: ' 원' // 단위를 '원'으로 수정
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
    computed: {
        ...mapGetters(['getAccountData']),
    },
    watch: {
        getAccountData(newData) {
            if (newData && newData.length) {
                this.updateChartData(newData);
            }
        }
    },
    mounted() {
        this.fetchAccountData();
    },
    methods: {
        ...mapActions(['fetchAccountData']),

        updateChartData(assets) {
            const bankNameMap = {
                1: "국민은행",
                2: "카카오뱅크",
                3: "신한은행"
            };

            const chartData = assets.map(account => ({
                name: bankNameMap[account.bankId] || `Unknown Bank`,
                y: account.balance
            }));

            this.chartOptions.series[0].data = chartData;
            this.$forceUpdate();  // 강제로 업데이트해버림
        }
    }
}
</script>


<style></style>