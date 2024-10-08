<template>
    <div>
        <div id="container" style="width:300px; height:400px;"></div>
    </div>
</template>

<script>
import Highcharts from 'highcharts';
import html2canvas from 'html2canvas';
import { mapGetters, mapActions } from 'vuex';

export default {
    name: 'AssetGrowthChart',
    computed: {
        ...mapGetters(['getAssetData']),  // Vuex에서 getter로 데이터 가져오기
    },
    methods: {
        ...mapActions(['fetchAssetData']),  // Vuex에서 action 호출하기
        renderChart() {
            // assetData는 Vuex로부터 가져온 데이터
            const sortedData = Object.entries(this.getAssetData).sort((a, b) => new Date(a[0]) - new Date(b[0]));

            const categories = sortedData.map(item => item[0]);
            const data = sortedData.map(item => item[1]);

            Highcharts.chart('container', {
                title: {
                    text: '자본 증감 추이'
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
                    max: Math.max(...data) + 1
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
        },
        saveChartImage() {
            const container = document.getElementById('container');  // 차트의 DOM 요소 선택

            html2canvas(container).then(canvas => {
                const imgData = canvas.toDataURL('image/png');  // 차트를 캡처하여 PNG로 변환

                // 로컬스토리지에 이미지 저장
                localStorage.setItem('assetGrowthChart', imgData);
                console.log('차트 이미지가 로컬스토리지에 저장되었습니다.');
            }).catch(error => {
                console.error('차트 이미지를 저장하는 중 오류 발생:', error);
            });
        }
    },
    mounted() {
        this.fetchAssetData();  // 데이터 요청
    },
    watch: {
        getAssetData(newData) {
            if (Object.keys(newData).length > 0) {
                this.renderChart();  // 데이터가 업데이트되면 차트 렌더링
                this.$nextTick(() => {
                    // 2초 후에 차트 이미지를 로컬스토리지에 저장
                    setTimeout(() => {
                        this.saveChartImage();
                    }, 2000);
                });
            }
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
