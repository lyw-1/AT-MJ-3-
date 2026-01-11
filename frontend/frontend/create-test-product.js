#!/usr/bin/env node

/**
 * 成品测试数据创建脚本
 * 通过前端API创建测试用成品数据
 */

import axios from 'axios';
import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

// 获取当前文件的目录路径
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// 读取环境变量文件
const envPath = path.join(__dirname, '.env.development');
const envContent = fs.readFileSync(envPath, 'utf8');

// 解析环境变量
const envVars = {}
envContent.split('\n').forEach(line => {
  const match = line.match(/^([^=]+)=(.+)$/);
  if (match) {
    envVars[match[1]] = match[2];
  }
});

// API基础URL
const API_BASE_URL = envVars.VITE_API_BASE_URL || 'http://localhost:3000/api';

// 创建axios实例
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
});

/**
 * 创建测试用成品数据
 */
async function createTestProduct() {
  try {
    console.log('=== 开始创建测试成品数据 ===');
    
    // 测试数据
    const testProduct = {
      productCategory: '测试类别',
      productSpec: 'Φ150×100',
      densityRequirement: {
        min: 500,
        max: 800
      },
      slotWidthRequirement: {
        min: 0.5,
        max: 1.5
      },
      otherRequirements: '这是一条测试数据，用于功能测试'
    };
    
    console.log('准备创建的数据:', JSON.stringify(testProduct, null, 2));
    
    // 调用创建成品API
    const response = await apiClient.post('/products', testProduct);
    
    console.log('=== 数据创建成功 ===');
    console.log('创建时间:', new Date().toISOString());
    console.log('创建人: 测试脚本');
    console.log('数据ID:', response.data.data.id);
    console.log('数据内容:', JSON.stringify(response.data.data, null, 2));
    
    // 验证数据是否成功写入
    console.log('\n=== 开始验证数据 ===');
    const verifyResponse = await apiClient.get(`/products/${response.data.data.id}`);
    
    console.log('=== 数据验证成功 ===');
    console.log('验证时间:', new Date().toISOString());
    console.log('验证结果:', JSON.stringify(verifyResponse.data.data, null, 2));
    
    // 记录创建信息到日志文件
    const logContent = {
      timestamp: new Date().toISOString(),
      creator: '测试脚本',
      action: '创建测试成品数据',
      data: response.data.data,
      verifyResult: verifyResponse.data.data
    };
    
    fs.writeFileSync(
      path.join(__dirname, 'test-product-log.json'),
      JSON.stringify(logContent, null, 2),
      'utf8'
    );
    
    console.log('\n=== 日志记录成功 ===');
    console.log('日志文件: test-product-log.json');
    
  } catch (error) {
    console.error('=== 操作失败 ===');
    if (error.response) {
      console.error('HTTP状态码:', error.response.status);
      console.error('响应数据:', error.response.data);
    } else if (error.request) {
      console.error('请求发送失败，没有收到响应');
      console.error('错误信息:', error.message);
    } else {
      console.error('请求配置错误:', error.message);
    }
    process.exit(1);
  }
}

// 执行创建操作
createTestProduct();
