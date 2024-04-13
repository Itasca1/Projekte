import { createApp } from 'vue'
import App from './App.vue'
import router from "./router";
import {Notify, Quasar} from 'quasar'
import '@quasar/extras/material-icons/material-icons.css'
import 'quasar/src/css/index.sass'
import {createI18n} from "vue-i18n"
import { languages } from './i18n'
import { defaultLocale } from './i18n'
import {createPinia} from "pinia";

const messages = Object.assign(languages)
const app = createApp(App)
app.use(router)
app.use(createI18n({
    locale: defaultLocale,
    fallbackLocale: 'de',
    messages
}))
app.use(createPinia())
app.use(Quasar, {
    plugins: {Notify} // import Quasar plugins and add here
})
app.mount('#app')
