// 测试脚本：模拟模具初始参数列表页面的API请求
const axios = require('axios');

async function testMoldParamsPageApi() {
    try {
        // 1. 先登录获取token
        console.log('1. 尝试登录...');
        const loginResponse = await axios.post('http://localhost:8080/api/auth/login', {
            username: 'admin',
            password: '123456'
        });
        
        console.log('登录响应:', loginResponse.status, loginResponse.data);
        
        if (loginResponse.data.code !== 200) {
            console.error('登录失败');
            return;
        }
        
        const token = loginResponse.data.data.token;
        console.log('获取到token:', token);
        
        // 2. 测试获取用户信息API
        console.log('\n2. 测试获取用户信息API...');
        const userInfoResponse = await axios.get('http://localhost:8080/api/auth/userinfo', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        console.log('获取用户信息响应:', userInfoResponse.status, userInfoResponse.data);
        
        // 3. 测试模具初始参数列表API
        console.log('\n3. 测试模具初始参数列表API...');
        const paramsResponse = await axios.get('http://localhost:8080/api/mold-initial-parameters', {
            headers: {
                'Authorization': `Bearer ${token}`
            },
            params: {
                page: 1,
                size: 10
            }
        });
        
        console.log('模具初始参数列表API响应:', paramsResponse.status, paramsResponse.data);
        
        // 4. 测试成品列表API
        console.log('\n4. 测试成品列表API...');
        try {
            const productsResponse = await axios.get('http://localhost:8080/api/products', {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            
            console.log('成品列表API响应:', productsResponse.status, productsResponse.data);
        } catch (error) {
            console.error('成品列表API请求失败:', error.response ? error.response.status : error.message);
            if (error.response) {
                console.error('响应数据:', error.response.data);
            }
        }
        
        // 5. 测试耗材列表API
        console.log('\n5. 测试耗材列表API...');
        try {
            const consumablesResponse = await axios.get('http://localhost:8080/api/consumables', {
                headers: {
                    'Authorization': `Bearer ${token}`
                },
                params: {
                    page: 1,
                    pageSize: 100
                }
            });
            
            console.log('耗材列表API响应:', consumablesResponse.status, consumablesResponse.data);
        } catch (error) {
            console.error('耗材列表API请求失败:', error.response ? error.response.status : error.message);
            if (error.response) {
                console.error('响应数据:', error.response.data);
            }
        }
        
        // 6. 测试获取员工列表API
        console.log('\n6. 测试获取员工列表API...');
        try {
            const employeesResponse = await axios.get('http://localhost:8080/api/employees', {
                headers: {
                    'Authorization': `Bearer ${token}`
                },
                params: {
                    role: '模具加工组'
                }
            });
            
            console.log('员工列表API响应:', employeesResponse.status, employeesResponse.data);
        } catch (error) {
            console.error('员工列表API请求失败:', error.response ? error.response.status : error.message);
            if (error.response) {
                console.error('响应数据:', error.response.data);
            }
        }
        
    } catch (error) {
        console.error('测试失败:', error.response ? error.response.status : error.message);
        if (error.response) {
            console.error('响应数据:', error.response.data);
        }
    }
}

testMoldParamsPageApi();