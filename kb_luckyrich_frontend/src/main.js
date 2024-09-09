import { createApp } from 'vue'
import App from './App.vue'

import router from '@/router/index'

import BootstrapVue from 'bootstrap-vue-3'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue-3/dist/bootstrap-vue-3.css'

// createApp(App).mount('#app')

const app = createApp(App)

app.use(router)
app.use(BootstrapVue)
app.mount('#app')
