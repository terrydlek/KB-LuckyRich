<template>
    <div v-if="compareData">
        <h4>동종업종 비교</h4>
        <table>
            <thead>
                <tr>
                    <th>종목명</th>
                    <th v-for="(name, index) in compareData.stockNames" :key="index">{{ name }}</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <th>현재가</th>
                    <td v-for="(price, index) in compareData.currentPrices" :key="index">{{ price }}</td>
                </tr>
                <tr>
                    <th>전일대비</th>
                    <td v-for="(change, index) in compareData.priceChanges" :key="index">{{ change }}</td>
                </tr>
                <tr>
                    <th>등락률</th>
                    <td v-for="(rate, index) in compareData.fluctuationRates" :key="index">{{ rate }}</td>
                </tr>
                <tr>
                    <th>시가총액 (억)</th>
                    <td v-for="(marketCap, index) in compareData.marketCaps" :key="index">{{ marketCap }}</td>
                </tr>
                <tr>
                    <th>외국인 비율 (%)</th>
                    <td v-for="(foreignOwnership, index) in compareData.foreignOwnerships" :key="index">{{
                        foreignOwnership }}</td>
                </tr>
                <tr>
                    <th>매출액 (억)</th>
                    <td v-for="(sale, index) in compareData.sales" :key="index">{{ sale }}</td>
                </tr>
                <tr>
                    <th>영업이익 (억)</th>
                    <td v-for="(operatingProfit, index) in compareData.operatingProfits" :key="index">{{ operatingProfit
                        }}</td>
                </tr>
                <tr>
                    <th>당기순이익 (억)</th>
                    <td v-for="(netProfit, index) in compareData.netProfits" :key="index">{{ netProfit }}</td>
                </tr>
                <tr>
                    <th>주당순이익 (EPS)</th>
                    <td v-for="(eps, index) in compareData.eps" :key="index">{{ eps }}</td>
                </tr>
                <tr>
                    <th>ROE (%)</th>
                    <td v-for="(roe, index) in compareData.roes" :key="index">{{ roe }}</td>
                </tr>
                <tr>
                    <th>PER</th>
                    <td v-for="(per, index) in compareData.pers" :key="index">{{ per }}</td>
                </tr>
                <tr>
                    <th>PBR</th>
                    <td v-for="(pbr, index) in compareData.pbrs" :key="index">{{ pbr }}</td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            compareData: null,
        };
    },
    mounted() {
        const stockCode = this.$route.params.stockCode;
        axios.get(`http://localhost:8080/asset/stock/${stockCode}/compare`)
            .then(response => {
                this.compareData = response.data;
            })
            .catch(error => {
                console.error(error);
            });
    }
};
</script>
