import { createApp } from 'vue';
import App from './App.vue';
import router from '@/router/index';
// import BootstrapVue from 'bootstrap-vue-3';
// import 'bootstrap/dist/css/bootstrap.css';
// import 'bootstrap-vue-3/dist/bootstrap-vue-3.css';
import './assets/global.css';

import HighchartsVue from 'highcharts-vue';
import Highcharts from 'highcharts';
import HighchartsMore from 'highcharts/highcharts-more';
import SolidGauge from 'highcharts/modules/solid-gauge';
import Exporting from 'highcharts/modules/exporting';
import ExportData from 'highcharts/modules/export-data';
import OfflineExporting from 'highcharts/modules/offline-exporting';

import store from '@/store.js'; // Vuex store 가져오기
import axios from 'axios'

// Axios 인터셉터 설정
axios.interceptors.response.use(
    response => {
        // 새로운 토큰이 응답 헤더에 있는지 확인
        const newAccessToken = response.headers['authorization'];
        const newRefreshToken = response.headers['refresh-token'];

        if (newAccessToken) {
            // 새로운 access token 저장
            localStorage.setItem('access_token', newAccessToken);
        }

        if (newRefreshToken) {
            // 새로운 refresh token 저장
            localStorage.setItem('token', newRefreshToken);
        }

        return response;
    },
    error => {
        // Access token 만료시 refresh token으로 새로운 access token 발급 시도
        // const originalRequest = error.config;

        // if (error.response.status === 401 && !originalRequest._retry) {
        //     originalRequest._retry = true;
        //     const refreshToken = localStorage.getItem('refresh-token');

        //     return axios.post('/auth/refresh', { refreshToken })
        //         .then(res => {
        //             if (res.status === 200) {
        //                 localStorage.setItem('access_token', res.data.accessToken);
        //                 axios.defaults.headers.common['Authorization'] = 'Bearer ' + res.data.accessToken;
        //                 originalRequest.headers['Authorization'] = 'Bearer ' + res.data.accessToken;
        //                 return axios(originalRequest);
        //             }
        //         });
        // }

        return Promise.reject(error);
    }
)

// Highcharts 모듈 활성화
Exporting(Highcharts);
ExportData(Highcharts);
OfflineExporting(Highcharts);
HighchartsMore(Highcharts);
SolidGauge(Highcharts);

const app = createApp(App);

// 플러그인 등록
app.use(router);
app.use(store); // Vuex store 사용
// app.use(BootstrapVue);
app.use(HighchartsVue);

// Vue 인스턴스 생성 및 DOM에 마운트
app.mount('#app');
