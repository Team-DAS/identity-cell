const fastify = require('fastify')({ logger: true });

// Importar las librerías necesarias
const httpProxy = require('@fastify/http-proxy');
const metrics = require('fastify-metrics'); 

fastify.register(metrics, { 
  endpoint: '/metrics',
});

// --- 1. Configuración (Leída de variables de entorno) ---
const ACCOUNT_SERVICE_URL = process.env.ACCOUNT_SERVICE_URL;
const AUTH_SERVICE_URL = process.env.AUTH_SERVICE_URL;

// --- 3. Registro de Rutas (El Proxy) ---

fastify.register(httpProxy, {
  upstream: AUTH_SERVICE_URL,
  prefix: 'identity-cell/api/v1/auth', 
  rewritePrefix: '/api/v1/auth', 
});


fastify.register(httpProxy, {
  upstream: ACCOUNT_SERVICE_URL,
  prefix: 'identity-cell/api/v1/accounts', 
  rewritePrefix: '/api/v1/accounts', 
});
// --- 4. Iniciar el Servidor ---
const start = async () => {
  try {
    // Escuchamos en 0.0.0.0 (para Docker) y en el puerto 8080
    await fastify.listen({ port: 8080, host: '0.0.0.0' });
  } catch (err) {
    fastify.log.error(err);
    process.exit(1);
  }
};
start();