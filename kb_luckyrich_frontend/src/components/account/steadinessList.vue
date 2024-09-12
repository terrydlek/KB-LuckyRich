<template>
    <div>
        <h2>당신의 투자 성향은 안정형입니다. 예적금 상품을 추천해드릴게요.</h2>
        <table border="1">
            <thead>
                <tr>
                    <th>상품명</th>
                    <th>가입 목적</th>
                    <th @click="sortTable('hitIrtCndCone')">
                        최고 금리
                        <span v-if="sortKey === 'hitIrtCndCone'">
                            {{ sortOrder === 'asc' ? '▲' : '▼' }}
                        </span>
                    </th>
                    <th @click="sortTable('prdJinTrmCone')">
                        가입 기간
                        <span v-if="sortKey === 'prdJinTrmCone'">
                            {{ sortOrder === 'asc' ? '▲' : '▼' }}
                        </span>
                    </th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="(item, index) in sortedProducts" :key="index">
                    <td>{{ formatText(item.prdNm) }}</td>
                    <td>{{ formatText(item.prdJinPpo) }}</td>
                    <td>{{ formatText(item.hitIrtCndCone) }}</td>
                    <td>{{ formatText(item.prdJinTrmCone) }}</td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue';
import { xml2json } from 'xml-js';

export default {
    setup() {
        const depositProducts = ref([]);
        const sortKey = ref('');
        const sortOrder = ref('asc');
        const totalPages = 10; // 가져오고 싶은 페이지 수

        const fetchDepositProducts = async (pageNo) => {
            const url = 'http://apis.data.go.kr/B190030/GetDepositProductInfoService/getDepositProductList';
            const serviceKey = 'Z1DCmdUJF3GRM2FNN3Ci3mdKuyR4tTw1cGg0qfk%2BKK0grrDBXjFQVTmy9Opr6nNlt8nCIQse8b8qS1RslSDE%2Bg%3D%3D';
            const queryParams = `?serviceKey=${serviceKey}&pageNo=${pageNo}&numOfRows=10&sBseDt=20200621&eBseDt=20200630`;
            const response = await fetch(url + queryParams);
            const text = await response.text();
            const result = xml2json(text, { compact: true, spaces: 4 });
            const jsonResult = JSON.parse(result);
            const items = jsonResult.response.body.items.item || [];
            return Array.isArray(items) ? items : [items];
        };

        const fetchAllPages = async () => {
            let allItems = [];
            for (let pageNo = 1; pageNo <= totalPages; pageNo++) {
                const items = await fetchDepositProducts(pageNo);
                allItems = allItems.concat(items);
            }
            depositProducts.value = allItems;
        };

        const formatText = (text) => {
            if (text && text._text) {
                let formattedText = text._text.trim();
                if (formattedText.startsWith('[') && formattedText.endsWith(']')) {
                    formattedText = formattedText.slice(1, -1).trim();
                }
                return formattedText;
            }
            return '';
        };

        // 정렬
        const sortTable = (key) => {
            if (sortKey.value === key) {
                sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc';
            } else {
                sortKey.value = key;
                sortOrder.value = 'asc';
            }
        };

        const sortedProducts = computed(() => {
            const sorted = [...depositProducts.value];
            if (sortKey.value) {
                sorted.sort((a, b) => {
                    let valA = formatText(a[sortKey.value]);
                    let valB = formatText(b[sortKey.value]);

                    // If sorting numbers, convert the text to a number
                    if (sortKey.value === 'hitIrtCndCone' || sortKey.value === 'prdJinTrmCone') {
                        valA = parseFloat(valA) || 0;
                        valB = parseFloat(valB) || 0;
                    }

                    if (valA < valB) return sortOrder.value === 'asc' ? -1 : 1;
                    if (valA > valB) return sortOrder.value === 'asc' ? 1 : -1;
                    return 0;
                });
            }
            return sorted;
        });

        onMounted(fetchAllPages);

        return {
            depositProducts,
            sortKey,
            sortOrder,
            sortedProducts,
            sortTable,
            formatText,
        };
    }
};
</script>

<!-- <style>
table {
    width: 100%;
    border-collapse: collapse;
    cursor: pointer;
}

th, td {
    padding: 10px;
    text-align: left;
}

th {
    background-color: #f4f4f4;
}

th span {
    font-size: 0.8em;
    margin-left: 5px;
}
</style> -->
