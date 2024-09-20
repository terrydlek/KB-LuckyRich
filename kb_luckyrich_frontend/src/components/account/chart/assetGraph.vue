<template>
    <div>
        <div id="container" style="width:100%; height:400px;"></div>
    </div>
</template>

<script>
import Highcharts from 'highcharts';

export default {
    name: 'AssetGrowthChart',
    mounted() {
        this.renderChart();
    },
    methods: {
        renderChart() {
            // const goalAsset = Math.random() * (1000000 - 500000) + 500000; // 목표 자산 랜덤 생성
            const goalAsset = 100000000
            const totalDays = 120;  // 목표 기간 설정
            const categories = Array.from({ length: 10 }, (_, i) => Math.floor(totalDays / 10 * (i + 1))); // 목표 설정 기간 10등분

            Highcharts.chart('container', {
                title: {
                    text: '자산 증감 추이'
                },
                accessibility: {
                    point: {
                        valueDescriptionFormat:
                            '{xDescription}{separator}{value} currency unit(s)'
                    }
                },
                xAxis: {
                    title: {
                        text: '진행 기간'
                    },
                    categories: categories // 목표 기간 10등분
                },
                yAxis: {
                    type: 'linear',
                    title: {
                        text: 'Total Asset (in currency units)'
                    },
                    tickInterval: goalAsset / 10, // 목표 자산을 5등분하여 표시
                    max: goalAsset // 최대값을 목표 자산으로 설정
                },
                tooltip: {
                    headerFormat: '<b>{series.name}</b><br />',
                    pointFormat: '{point.y} currency unit(s)'
                },
                series: [{
                    name: 'Asset Growth',
                    data: Array.from({ length: 10 }, () => Math.random() * goalAsset), // 각 기간별 자산 랜덤 생성
                    color: {
                        linearGradient: {
                            x1: 0,
                            x2: 0,
                            y1: 1,
                            y2: 0
                        },
                        stops: [
                            [0, '#0000ff'],
                            [1, '#ff0000']
                        ]
                    }
                }]
            });
        }
    }
};
</script>

<style>
#container {
    width: 100%;
    height: 400px;
}
</style>