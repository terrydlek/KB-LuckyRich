<template>
    <div class="home">
        <ServiceComponent serviceName="한눈에 내 자산 확인하기" route="lookup">
            <totalChart />
        </ServiceComponent>
        <ServiceComponent serviceName="상품 추천" route="recommendation">
            <ProductTable />
        </ServiceComponent>
        <ServiceComponent serviceName="가계부" route="accountbook" />

    </div>
</template>

<script setup>
import ServiceComponent from '@/components/ServiceComponent.vue';
import totalChart from '@/components/account/totalChart.vue';
import { handleNaverLoginCallback } from '@/components/buttons/HandleNaverLogin';
import ProductTable from '@/components/tables/ProductTable.vue';
import { onMounted } from 'vue';
import { useRouter } from 'vue-router';
const router = useRouter();

onMounted(() => {
    handleNaverLoginCallback();
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
</style>