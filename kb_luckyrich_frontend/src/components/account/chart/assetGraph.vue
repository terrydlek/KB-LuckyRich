<template>
    <div>
        <div id="container" style="width:100%; height:400px;"></div>
    </div>
</template>

<script>
import Highcharts from 'highcharts';
import axios from 'axios';

export default {
    name: 'AssetGrowthChart',
    data() {
        return {
            assetData: {}
        };
    },
    mounted() {
        this.fetchAssetData();
    },
    methods: {
        async fetchAssetData() {
            const token = localStorage.getItem('access_token');
            try {
                const response = await axios.get('http://localhost:8080/myasset/idTrend', {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
                this.assetData = response.data;
                this.renderChart();
            } catch (error) {
                console.error('Error fetching asset data:', error);
            }
        },
        renderChart() {
            const sortedData = Object.entries(this.assetData).sort((a, b) => new Date(a[0]) - new Date(b[0]));

            const categories = sortedData.map(item => item[0]);
            const data = sortedData.map(item => item[1]);

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
                        text: '거래 날짜'
                    },
                    categories: categories
                },
                yAxis: {
                    type: 'linear',
                    title: {
                        text: 'Total Asset (in currency units)'
                    },
                    tickInterval: Math.max(...data) / 10,
                    max: Math.max(...data)
                },
                tooltip: {
                    headerFormat: '<b>{series.name}</b><br />',
                    pointFormat: '{point.y} currency unit(s)'
                },
                series: [{
                    name: 'Asset Growth',
                    data: data,
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
