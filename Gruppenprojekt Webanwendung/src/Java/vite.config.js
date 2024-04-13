// noinspection NodeCoreCodingAssistance
import {fileURLToPath, URL} from 'url'

import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import { quasar, transformAssetUrls } from '@quasar/vite-plugin'

// https://vitejs.dev/config/
// noinspection JSCheckFunctionSignatures
export default defineConfig({
    plugins: [vue({
            template: transformAssetUrls
        }
    ),
        quasar({
            sassVariables: 'src/quasar-variables.sass'
        })], resolve: {
        alias: {
            '@': fileURLToPath(new URL('./src', import.meta.url))
        }
    }, server: {
        proxy: {
            /*
                proxy all webpack dev-server requests starting with /api
                to our Spring Boot backend (localhost:8088) using http-proxy-middleware
                see https://vitejs.dev/config/#server-proxy
            */
            '/api': {
                target: 'http://localhost:8088', ws: true, changeOrigin: true
            }
        }
    }, build: {
        outDir: 'target/classes/public', assetsDir: 'static'
    }
})
