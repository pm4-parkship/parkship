const isProduction = process.env.NODE_ENV === 'production';

module.exports = {
  async rewrites() {
    return isProduction
      ? []
      : [
          {
            source: '/backend/:path*',
            destination: 'http://localhost:8080/backend/:path*'
          }
        ];
  }
};
