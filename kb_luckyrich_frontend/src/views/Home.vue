<template>
    <div class="home">
        <ServiceComponent serviceName="한눈에 내 자산 확인하기" route="asset">
            <totalChart />
        </ServiceComponent>
        <ServiceComponent serviceName="나에게 맞는 투자 상품은?" route="test">
            <ProductTable />
        </ServiceComponent>
        <ServiceComponent serviceName="가계부" route="accountbook" />
        <ServiceComponent serviceName="부동산" route="realestate">
            <img src="../assets/images/realEstate.png"  alt="Real Estate" class="service-image">
        </ServiceComponent>
    </div>
</template>

<script setup>
import ServiceComponent from '@/components/ServiceComponent.vue';
import totalChart from '@/components/account/chart/totalChart.vue';
import { handleKakaoLoginCallback } from '@/components/buttons/HandleKakaoLogin';
import { handleNaverLoginCallback } from '@/components/buttons/HandleNaverLogin';
import ProductTable from '@/components/tables/ProductTable.vue';
import { onMounted } from 'vue';
import { useRouter } from 'vue-router';
const router = useRouter();

onMounted(() => {
    handleNaverLoginCallback();
    handleKakaoLoginCallback();
})
</script>

<style>
.home {
    display: flex;
    flex-direction: row;
    align-items: flex-start;
    justify-content: center;
    /* flex-wrap: wrap; */
}
img {
    max-width: 100%; 
    height: auto; 
    object-fit: cover; 
    border-radius: 8px; 
}
</style>