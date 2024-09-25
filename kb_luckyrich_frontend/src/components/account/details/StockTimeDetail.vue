<template>
    <div v-if="timeData" class="container my-4">
        <div class="row">
            <!-- 첫 번째 테이블 -->
            <div class="col-md-6">
                <table class="table table-bordered table-hover">
                    <thead class="thead-light">
                        <tr>
                            <th>항목</th>
                            <th>정보</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <th>현재가</th>
                            <td>{{ timeData.nowVal }}</td>
                        </tr>
                        <tr>
                            <th>전일대비</th>
                            <td>{{ timeData.diff }}</td>
                        </tr>
                        <tr>
                            <th>등락률(%)</th>
                            <td>{{ timeData.rate }}</td>
                        </tr>
                        <tr>
                            <th>거래량</th>
                            <td>{{ timeData.quant }}</td>
                        </tr>
                        <tr>
                            <th>거래대금(백만)</th>
                            <td>{{ timeData.amount }}</td>
                        </tr>
                        <tr>
                            <th>액면가</th>
                            <td>{{ timeData.faceVal }}</td>
                        </tr>
                        <tr>
                            <th>상한가</th>
                            <td>{{ timeData.upperLimit }}</td>
                        </tr>
                        <tr>
                            <th>하한가</th>
                            <td>{{ timeData.lowerLimit }}</td>
                        </tr>
                        <tr>
                            <th>PER</th>
                            <td>{{ timeData.per }}</td>
                        </tr>
                        <tr>
                            <th>EPS</th>
                            <td>{{ timeData.eps }}</td>
                        </tr>
                        <tr>
                            <th>52주 최고</th>
                            <td>{{ timeData.high52Week }}</td>
                        </tr>
                        <tr>
                            <th>52주 최저</th>
                            <td>{{ timeData.low52Week }}</td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <!-- 두 번째 테이블 -->
            <div class="col-md-6">
                <table class="table table-bordered table-hover">
                    <thead class="thead-light">
                        <tr>
                            <th>항목</th>
                            <th>정보</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <th>시가총액</th>
                            <td>{{ timeData.marketCap }}억원</td>
                        </tr>
                        <tr>
                            <th>외국인 현재</th>
                            <td>{{ timeData.foreignShares }}</td>
                        </tr>
                        <tr>
                            <th>매도호가</th>
                            <td>{{ timeData.sellingPrice }}</td>
                        </tr>
                        <tr>
                            <th>매수호가</th>
                            <td>{{ timeData.buyingPrice }}</td>
                        </tr>
                        <tr>
                            <th>전일가</th>
                            <td>{{ timeData.previousPrice }}</td>
                        </tr>
                        <tr>
                            <th>시가</th>
                            <td>{{ timeData.openingPrice }}</td>
                        </tr>
                        <tr>
                            <th>고가</th>
                            <td>{{ timeData.highPrice }}</td>
                        </tr>
                        <tr>
                            <th>저가</th>
                            <td>{{ timeData.lowPrice }}</td>
                        </tr>
                        <tr>
                            <th>전일상한</th>
                            <td>{{ timeData.previousUpperLimit }}</td>
                        </tr>
                        <tr>
                            <th>전일하한</th>
                            <td>{{ timeData.previousLowerLimit }}</td>
                        </tr>
                        <tr>
                            <th>상장주식수</th>
                            <td>{{ timeData.listedShares }}</td>
                        </tr>
                        <tr>
                            <th>자본금</th>
                            <td>{{ timeData.capital }}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <p v-else>데이터를 불러오는 중입니다...</p>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            timeData: null,
            stockCode: this.$route.params.stockCode
        };
    },
    mounted() {
        axios.get(`http://localhost:8080/recommend/active/time/${this.stockCode}`)
            .then(response => {
                this.timeData = response.data;
            })
            .catch(error => {
                console.error(error);
            });
    }
};
</script>

<style scoped>
.table {
    margin-top: 20px;
    font-size: 1rem;
}

.table th {
    background-color: #f8f9fa;
}

.table-hover tbody tr:hover {
    background-color: #f1f3f5;
}

.container {
    max-width: 1200px;
}
</style>
