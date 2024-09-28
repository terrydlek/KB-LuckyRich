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

import SummernoteEditor from 'vue3-summernote-editor';

import { createPinia } from 'pinia'
import $ from 'jquery';
// import 'summernote/dist/summernote-lite.js';
// import 'summernote/dist/summernote-lite.css';

// window.$ = window.jQuery = $;


HighchartsMore(Highcharts);
SolidGauge(Highcharts);

// createApp(App).mount('#app')

const app = createApp(App)

app.use(router)
app.component('SummernoteEditor', SummernoteEditor);
app.use(createPinia())
app.use(BootstrapVue)
app.use(HighchartsVue)

app.mount('#app')