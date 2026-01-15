module.exports = {
    devServer: {
        host: '0.0.0.0', // 允许所有IP访问
        port: 8084,
        allowedHosts: 'all', // 允许所有主机访问
        proxy: {
            // 代理内网API请求
            '/api': {
                target: 'http://192.168.20.3:8081',
                changeOrigin: true,
                pathRewrite: {
                    '^/api': ''
                },
                timeout: 10000
            },
            // 代理公网API请求
            '/public-api': {
                target: 'http://111.4.68.108:8081',
                changeOrigin: true,
                pathRewrite: {
                    '^/public-api': ''
                },
                timeout: 10000
            },
            // 代理直接API路径（如 /st-rivers-r/list）
            '/st-rivers-r': {
                target: 'http://192.168.20.3:8081',
                changeOrigin: true,
                timeout: 10000
            },
            '/st-pptn-hour': {
                target: 'http://192.168.20.3:8081',
                changeOrigin: true,
                timeout: 10000
            },
            '/zkxt': {
                target: 'http://192.168.20.3:8081',
                changeOrigin: true,
                timeout: 10000
            }
        },
        headers: {
            'Access-Control-Allow-Origin': '*',
            'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE, PATCH, OPTIONS',
            'Access-Control-Allow-Headers': 'X-Requested-With, content-type, Authorization'
        }
    }
}



