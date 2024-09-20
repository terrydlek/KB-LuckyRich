<template>
    <div>
        <h2>당신의 투자 성향은 안정형입니다. 예적금 상품을 추천해드릴게요.</h2>

        <!-- 데이터가 있을 경우 테이블 표시 -->
        <table v-if="deposits.length">
            <thead>
                <tr>
                    <th>금융회사</th>
                    <th>상품명</th>
                    <th>세전 이자율</th>
                    <th>세후 이자율</th>
                    <th>세후 이자</th>
                    <th>최고 우대 금리</th>
                    <th>가입 제한 여부</th>
                    <th>이자 계산 방식</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="deposit in deposits" :key="deposit.상품명">
                    <td>{{ deposit.company }}</td>
                    <td>{{ deposit.prodname }}</td>
                    <td>{{ deposit.prerate ?? 'N/A' }}</td>
                    <td>{{ deposit.afterrate ?? 'N/A' }}</td>
                    <td>{{ deposit.afterinterest ?? 'N/A' }}</td>
                    <td>{{ deposit.bestinterest ?? 'N/A' }}</td>
                    <td>{{ deposit.limit ?? 'N/A' }}</td>
                    <td>{{ deposit.calmethod }}</td>
                </tr>
            </tbody>
        </table>

        <p v-else>예적금 상품을 불러오고 있어요~</p>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            deposits: []
        };
    },
    mounted() {
        axios.get('http://localhost:8080/asset/getDeposit')
            .then(response => {
                this.deposits = response.data;
            })
            .catch(error => {
                console.error("There was an error fetching the deposits:", error);
            });
    }
}
</script>

<style scoped>
table {
    width: 100%;
    border-collapse: collapse;
}

th, td {
    border: 1px solid #ddd;
    padding: 8px;
    text-align: left;
}

th {
    background-color: #f2f2f2;
}
</style>
