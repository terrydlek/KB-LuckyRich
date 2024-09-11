<template>
    <div class="home">
        <ServiceComponent serviceName="한눈에 내 자산 확인하기" route="lookup" />
        <ServiceComponent serviceName="상품 추천" route="recommendation" />
        <ServiceComponent serviceName="가계부" route="accountbook" />
    </div>
</template>

<script setup>
import ServiceComponent from '@/components/ServiceComponent.vue';
import { useRouter } from 'vue-router';

const router = useRouter();

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