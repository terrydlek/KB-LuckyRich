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
            assetData: {},
            dataLoaded: false // 데이터 로드 상태 추가
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
                this.dataLoaded = true; // 데이터 로드 완료 시 true로 설정
            } catch (error) {
                console.error('Error fetching asset data:', error);
                this.dataLoaded = false; // 오류 발생 시 false로 설정
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


<style scoped>
.content-wrapper {
    display: flex;
    justify-content: space-between;
}

.left-table {
    flex: 1;
    margin-right: 20px;
}

.right-charts {
    flex: 1;
}

.accountInfo {
    margin-bottom: 20px;
}

.chart-container {
    margin-bottom: 20px;
}

.chart-container>* {
    width: 100%;
}
</style>
