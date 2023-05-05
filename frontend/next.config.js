const isProduction = process.env.NODE_ENV === 'production';

module.exports = {
  reactStrictMode: true,
  images: {
    dangerouslyAllowSVG: true,
    contentSecurityPolicy: "default-src 'self'; script-src 'none'; sandbox;"
  },
  productionBrowserSourceMaps: false,
  experimental: {
    esmExternals: true,
    swcTraceProfiling: true
  },
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
