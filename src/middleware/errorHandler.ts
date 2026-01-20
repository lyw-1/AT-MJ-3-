export const errorHandler = (err: any, req: any, res: any, next: any) => {
  const status = err?.statusCode ?? 500
  const message = err?.message ?? 'Internal Server Error'
  res.status(status).json({ success: false, error: { code: status, message } })
}
