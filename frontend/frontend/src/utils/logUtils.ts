// 日志工具函数

export const logApiResponse = (name: string, response: any) => {
  console.log(`\n=== ${name} API响应 ===`);
  console.log('响应数据:', response);
  console.log('响应类型:', typeof response);
  console.log('响应是否为对象:', typeof response === 'object' && response !== null);
  
  if (typeof response === 'object' && response !== null) {
    console.log('响应对象的键:', Object.keys(response));
    
    if ('records' in response) {
      console.log('records属性:', response.records);
      console.log('records类型:', typeof response.records);
      console.log('records长度:', Array.isArray(response.records) ? response.records.length : '不是数组');
      
      if (Array.isArray(response.records)) {
        console.log('records前3项:', response.records.slice(0, 3));
      }
    }
    
    if ('total' in response) {
      console.log('total属性:', response.total);
      console.log('total类型:', typeof response.total);
    }
  }
  
  console.log(`=== ${name} API响应结束 ===\n`);
};
