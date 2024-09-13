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

    <button @click="jwtFilterTest">filter test</button>
</template>

<script setup>
import ServiceComponent from '@/components/ServiceComponent.vue';
import totalChart from '@/components/account/totalChart.vue';
import ProductTable from '@/components/tables/ProductTable.vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();

function jwtFilterTest() {
    const accessToken = localStorage.getItem('access_token');

    axios.get("http://localhost:8080/asset/test", {
        headers: {
            'Authorization': `Bearer ${accessToken}`
        }
    })
        .then(res => {
            alert(res.data);
        })
        .catch(err => {
            alert(err);
        });
}

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