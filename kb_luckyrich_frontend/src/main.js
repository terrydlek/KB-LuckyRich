import { createApp } from 'vue'
import App from './App.vue'

import router from '@/router/index'

import BootstrapVue from 'bootstrap-vue-3'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue-3/dist/bootstrap-vue-3.css'

import HighchartsVue from 'highcharts-vue';
import Highcharts from 'highcharts';
import HighchartsMore from 'highcharts/highcharts-more';
import SolidGauge from 'highcharts/modules/solid-gauge';

import { createPinia } from 'pinia'

HighchartsMore(Highcharts);
SolidGauge(Highcharts);

// createApp(App).mount('#app')

const app = createApp(App)

app.use(router)
app.use(createPinia())
app.use(BootstrapVue)
app.use(HighchartsVue)

app.mount('#app')
