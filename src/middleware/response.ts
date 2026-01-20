export type ApiError = { code: number; message: string }
export type ApiResponse<T> = { success: true; data: T } | { success: false; error: ApiError }

export const respondSuccess = <T,>(res: any, data: T) => res.json({ success: true, data })
export const respondError = (res: any, code: number, message: string) => res.status(code).json({ success: false, error: { code, message } })
