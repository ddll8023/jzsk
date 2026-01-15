import vue from '@vitejs/plugin-vue2';
import { resolve } from 'path';

export default {
    plugins: [
        vue()
    ],
    resolve: {
        alias: {
            '@': resolve(__dirname, 'src'),
        },
        extensions: ['.mjs', '.js', '.ts', '.jsx', '.tsx', '.json', '.vue'],
    },
    server: {
        port: 8084,
        proxy: {
            // Configuration from your original config/index.js proxyTable would go here
            // Example:
            // '/api': {
            //   target: 'http://backend-server',
            //   changeOrigin: true
            // }
        }
    }
};
