// Simple requestId generator and middleware without external deps
function generateRequestId(): string {
  const rand = Math.random().toString(36).slice(2, 8);
  const time = Date.now().toString(36);
  return `REQ-${rand}-${time}`;
}

export const requestIdMiddleware = (req: any, res: any, next: any) => {
  const id = req.headers['x-request-id'] || generateRequestId();
  (req as any).requestId = id;
  res.setHeader('X-Request-Id', id);
  next();
};
