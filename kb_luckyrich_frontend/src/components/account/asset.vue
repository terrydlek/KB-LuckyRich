<template>
    <div>
        <h1>Asset</h1>
        <!-- 차트를 한 화면에 나란히 표시 -->
        <div class="charts-container">
            <total-chart />
            <goal-chart />
            <asset-graph />
        </div>
        <div class="charts-container">
            <accountBookChart />
            <consumptionstatus />
            <assetcomparison />
        </div>

    </div>
</template>

<script>
import totalChart from './chart/totalChart.vue';
import goalChart from './chart/goalChart.vue';
import assetGraph from './chart/assetGraph.vue';
import accountBookChart from './chart/accountBookChart.vue';
import assetcomparison from './chart/assetComparison.vue';
import consumptionstatus from './chart/consumptionstatus.vue';

export default {
    components: {
        totalChart,
        goalChart,
        assetGraph,
        accountBookChart,
        consumptionstatus,
        assetcomparison
    }
}
</script>

<style>
.charts-container {
    display: flex;
    justify-content: space-around; /* 차트를 수평으로 정렬 */
    padding: 20px;
}

.charts-container > div {
    width: 45%; /* 각각의 차트가 화면의 절반씩 차지하도록 설정 */
}
</style>
