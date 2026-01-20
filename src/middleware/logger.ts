export const loggerMiddleware = (req: any, res: any, next: any) => {
  const start = Date.now();
  res.on('finish', () => {
    const duration = Date.now() - start;
    const id = (req as any).requestId || 'unknown';
    const log = `[${id}] ${req.method} ${req.originalUrl} ${res.statusCode} ${duration}ms`;
    // Structured logging can be substituted with a real logger later
    console.info(log);
  });
  next();
};
