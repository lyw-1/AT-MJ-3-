import request from '@/utils/request'

/**
 * 获取操作日志列表
 * @param params 查询参数
 * @returns 操作日志列表
 */
export const getOperationLogs = (params: any) => {
  return request({
    url: '/v1/api/log/operation/page',
    method: 'GET',
    params
  })
}

/**
 * 获取操作日志详情
 * @param id 日志ID
 * @returns 操作日志详情
 */
export const getOperationLogDetail = (id: number) => {
  return request({
    url: `/v1/api/log/operation/${id}`,
    method: 'GET'
  })
}

/**
 * 批量删除操作日志
 * @param ids 日志ID数组
 * @returns 删除结果
 */
export const deleteOperationLogs = (ids: number[]) => {
  return request({
    url: '/v1/api/log/operation/batch',
    method: 'DELETE',
    data: ids
  })
}

/**
 * 清理操作日志
 * @returns 清理结果
 */
export const cleanOperationLogs = () => {
  return request({
    url: '/v1/api/log/operation/clean',
    method: 'DELETE'
  })
}
