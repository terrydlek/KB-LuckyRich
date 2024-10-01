import { createApp } from 'vue';
import App from './App.vue';
// import './assets/main.css';

import router from '@/router/index';
import BootstrapVue from 'bootstrap-vue-3';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-vue-3/dist/bootstrap-vue-3.css';

import HighchartsVue from 'highcharts-vue';
import Highcharts from 'highcharts';
import HighchartsMore from 'highcharts/highcharts-more';
import SolidGauge from 'highcharts/modules/solid-gauge';

import { createPinia } from 'pinia';
import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client'

window.global = window;

HighchartsMore(Highcharts);
SolidGauge(Highcharts);

const app = createApp(App);

const serverURL = "http://localhost:8080/ws";
const token = localStorage.getItem('access_token');

const socket = new SockJS(serverURL, null, {
    headers: {
        Authorization: `Bearer ${token}`, // JWT 토큰을 헤더에 추가
        'STOMP-Version': '1.2'
    }
});

const stompClient = Stomp.over(socket);

// Stomp 연결 설정
stompClient.connect({}, (frame) => {
    console.log('Connected to WebSocket server:', frame);

    // 메시지 구독 설정
    stompClient.subscribe('/topic/notifications', (notification) => {
        console.log('Notification received:', notification.body);
        alert('Notification received:', notification.body);
        // 알림을 처리하는 로직 추가
    });

}, (error) => {
    console.error('Error connecting to WebSocket server:', error);
});

// Vue 인스턴스에 stompClient 추가
app.config.globalProperties.$stompClient = stompClient;


app.use(router);
app.use(createPinia());
app.use(BootstrapVue);
app.use(HighchartsVue);

app.mount('#app');
