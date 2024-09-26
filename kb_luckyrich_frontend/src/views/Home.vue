<template>
    <div class="home">
        <div class="left-section">
            <div class="chart-container">
                <button @click="prevChart">←</button>
                <component :is="currentChart" />
                <button @click="nextChart">→</button>
            </div>
        </div>

        <!-- 오른쪽 섹션: 2x2 그리드 -->
        <div class="right-section">
            <ServiceComponent serviceName="한눈에 내 자산 확인하기" route="asset">
                <img src="../assets/images/checkAsset.jpg" class="service-image">
            </ServiceComponent>

            <ServiceComponent serviceName="나에게 맞는 투자 상품은?" route="test">
                <img src="../assets/images/test.jpg" class="service-image">
            </ServiceComponent>

            <ServiceComponent serviceName="거래 내역 조회" route="accountbook">
                <img src="../assets/images/credit.jpg" class="service-image">
            </ServiceComponent>

            <ServiceComponent serviceName="부동산" route="realestate">
                <img src="../assets/images/realEstate.png" alt="Real Estate" class="service-image" />
            </ServiceComponent>

            <ServiceComponent serviceName="금융 뉴스" route="financenews">
                <img src="../assets/images/news.jpg" class="service-image">
            </ServiceComponent>

            <ServiceComponent serviceName="Q&A 게시판" route="asset">
                <img src="../assets/images/qa.jpg" class="service-image">
            </ServiceComponent>
        </div>
    </div>
</template>

<script setup>
import ServiceComponent from '@/components/ServiceComponent.vue';
import { handleKakaoLoginCallback } from '@/components/buttons/HandleKakaoLogin';
import { handleNaverLoginCallback } from '@/components/buttons/HandleNaverLogin';
import ProductTable from '@/components/tables/ProductTable.vue';
import { onMounted } from 'vue';
import { useRouter } from 'vue-router';

import { ref } from 'vue';
import totalChart from '@/components/account/chart/totalChart.vue';
import goalChart from "@/components/account/chart/goalChart.vue";
import assetGraph from "@/components/account/chart/assetGraph.vue";
import accountBookChart from "@/components/account/chart/accountBookChart.vue";
import assetcomparison from "@/components/account/chart/assetComparison.vue";
import consumptionstatus from "@/components/account/chart/consumptionstatus.vue";

const charts = [totalChart, goalChart, assetGraph, accountBookChart, assetcomparison, consumptionstatus];
const currentIndex = ref(0);
const currentChart = ref(charts[currentIndex.value]);

const nextChart = () => {
    currentIndex.value = (currentIndex.value + 1) % charts.length;
    currentChart.value = charts[currentIndex.value];
};

const prevChart = () => {
    currentIndex.value = (currentIndex.value - 1 + charts.length) % charts.length;
    currentChart.value = charts[currentIndex.value];
};


const router = useRouter();
onMounted(() => {
    handleNaverLoginCallback();
    handleKakaoLoginCallback();
});
</script>

<style>
.home {
    display: flex;
    justify-content: space-between;
    gap: 20px;
    /* 좌우 섹션 간격 */
    padding: 20px;
}

.left-section {
    margin-top: 100px;
    /* 왼쪽 섹션이 더 넓게 */
    margin-left: 70px;
}

.right-section {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    grid-template-rows: repeat(2, 1fr);
    gap: 1px;
}

.service-image {
    height: auto;
    max-width: 100%;
    object-fit: cover;
    border-radius: 8px;
}

.chart-container {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 20px;
}
</style>