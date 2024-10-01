import StockAnalyze from "@/views/analyze/StockAnalyze.vue";
import socket from "@/views/account/Socket.vue";

export default [
    {
        path: '/luckyrich/analyze/stock',
        name: 'stockanalyze',
        component: StockAnalyze
    },
    {
        path: '/luckyrich/socket',
        name: 'socket',
        component: socket
    }
]