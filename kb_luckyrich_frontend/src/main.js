import { createApp } from 'vue';
import App from './App.vue';
import router from '@/router/index';
import BootstrapVue from 'bootstrap-vue-3';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-vue-3/dist/bootstrap-vue-3.css';
import './assets/global.css';

import HighchartsVue from 'highcharts-vue';
import Highcharts from 'highcharts';
import HighchartsMore from 'highcharts/highcharts-more';
import SolidGauge from 'highcharts/modules/solid-gauge';
import Exporting from 'highcharts/modules/exporting';
import ExportData from 'highcharts/modules/export-data';
import OfflineExporting from 'highcharts/modules/offline-exporting';

import store from '@/store.js'; // Vuex store 가져오기

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
app.use(BootstrapVue);
app.use(HighchartsVue);

// Vue 인스턴스 생성 및 DOM에 마운트
app.mount('#app');
