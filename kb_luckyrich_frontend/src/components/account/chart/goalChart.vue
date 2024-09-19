<template>
    <highcharts :options="chartOptions" />
</template>

<script>
import Highcharts from 'highcharts';

export default {
    name: 'GoalChart',
    methods: {
        generateRandomAmount(min, max) {
            return Math.floor(Math.random() * (max - min + 1)) + min;
        }
    },
    data() {
        const goalAsset = 100000000; // 목표 자산: 1억
        const currentAsset = this.generateRandomAmount(0, goalAsset); // 현재 자산: 0 ~ 목표 자산 내에서 랜덤
        const max = goalAsset;
        const tickInterval = Math.floor(max / 8); // 8부분으로 나눠서 표시

        return {
            chartOptions: {
                chart: {
                    type: 'gauge',
                    plotBackgroundColor: null,
                    plotBackgroundImage: null,
                    plotBorderWidth: 0,
                    plotShadow: false,
                    height: '100%'
                },
                title: {
                    text: '목표 자산까지!!!'
                },
                pane: {
                    startAngle: -90,
                    endAngle: 90,
                    background: null,
                    center: ['50%', '75%'],
                    size: '110%'
                },
                yAxis: {
                    min: 0,
                    max: max, // 목표 자산이 최대값
                    tickPixelInterval: 72,
                    tickPosition: 'inside',
                    tickLength: 20,
                    tickWidth: 1,
                    minorTickInterval: null,
                    tickInterval: tickInterval, // 8단계로 나눠서 표현
                    labels: {
                        distance: 20,
                        style: {
                            fontSize: '14px'
                        }
                    },
                    lineWidth: 0,
                    plotBands: [{
                        from: 0,
                        to: max * 0.6,
                        color: '#55BF3B', // Green for lower part
                        thickness: 20
                    }, {
                        from: max * 0.6,
                        to: max * 0.8,
                        color: '#DDDF0D', // Yellow for medium
                        thickness: 20
                    }, {
                        from: max * 0.8,
                        to: max,
                        color: '#DF5353', // Red for upper part
                        thickness: 20
                    }]
                },
                series: [{
                    name: 'Total Asset',
                    data: [currentAsset], // 현재 자산이 화살표로 표시됨
                    tooltip: {
                        valueSuffix: ' ₩'
                    },
                    dataLabels: {
                        format: '{y} ₩',
                        borderWidth: 0,
                        color: '#333333',
                        style: {
                            fontSize: '16px'
                        }
                    },
                    dial: {
                        radius: '100%',
                        backgroundColor: 'gray',
                        baseWidth: 12,
                        baseLength: '0%',
                        rearLength: '0%'
                    },
                    pivot: {
                        backgroundColor: 'orange',
                        radius: 6
                    }
                }]
            }
        }
    }
}
</script>

<style></style>
