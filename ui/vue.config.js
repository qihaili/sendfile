module.exports = {
  publicPath: '/ui/',
  devServer: {
    // 设置主机地址
    // host: 'localhost',
    host: '0.0.0.0',
    // 设置默认端口
    port: 8070,
    // 设置代理
    proxy: {
      '/api': {
        // 目标 API 地址
        target: 'http://localhost:30080/',
        // 如果要代理 websockets
        ws: false,
        // 将主机标头的原点更改为目标URL
        changeOrigin: true
      },
      '/public': {
        // 目标 API 地址
        target: 'http://localhost:30080/',
        // 如果要代理 websockets
        ws: false,
        // 将主机标头的原点更改为目标URL
        changeOrigin: true
      }
    }
  }
}