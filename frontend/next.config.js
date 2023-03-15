const removeImports = require('next-remove-imports')();

module.exports =   removeImports({
    async redirects() {
      return [
        {
          source: '/about',
          destination: '/',
          permanent: true // triggers 308
        }
      ];
    },
    images: {
      dangerouslyAllowSVG: true,
      contentSecurityPolicy: "default-src 'self'; script-src 'none'; sandbox;"
    },
    staticPageGenerationTimeout: 1000,
    output: 'standalone',
    productionBrowserSourceMaps: false,
    experimental: {
      esmExternals: true,
      swcTraceProfiling: true
    }
  }
);
