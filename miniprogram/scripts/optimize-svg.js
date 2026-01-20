const fs = require('fs');
const path = require('path');
const { execSync } = require('child_process');

const iconsDir = path.join(__dirname, '../assets/icons');
const optimizedDir = path.join(__dirname, '../assets/icons/optimized');

// 确保优化目录存在
if (!fs.existsSync(optimizedDir)) {
  fs.mkdirSync(optimizedDir, { recursive: true });
  console.log('创建优化目录:', optimizedDir);
}

// 获取所有SVG文件
const svgFiles = fs.readdirSync(iconsDir).filter(file => file.endsWith('.svg'));

console.log(`找到 ${svgFiles.length} 个SVG文件`);

// SVGO优化配置
const svgoConfig = {
  plugins: [
    {
      name: 'preset-default',
      params: {
        // 移除注释
        removeComments: true,
        
        // 移除不使用的定义
        removeUnusedNS: true,
        
        // 移除编辑器数据
        removeEditorsNSData: true,
        
        // 移除空属性
        removeEmptyAttrs: true,
        
        // 移除隐藏元素
        removeHiddenElems: true,
        
        // 移除空文本节点
        removeEmptyText: true,
        
        // 移除空容器
        removeEmptyContainers: true,
        
        // 合并路径
        mergePaths: true,
        
        // 移除重复的属性
        removeUselessStrokeAndFill: true,
        
        // 移除默认属性
        removeUnknownsAndDefaults: true,
        
        // 清理属性值
        cleanupIDs: true,
        
        // 移除viewBox
        removeViewBox: false,
        
        // 清理数值
        cleanupNumericValues: true,
        
        // 转换形状
        convertColors: true,
        
        // 转换路径数据
        convertPathData: true,
        
        // 转换变换
        convertTransform: true,
        
        // 移除非继承的组属性
        removeNonInheritableGroupAttrs: true,
        
        // 移除title元素
        removeTitle: true,
        
        // 移除desc元素
        removeDesc: true
      }
    }
  ]
};

// 压缩SVG文件
let totalOriginalSize = 0;
let totalOptimizedSize = 0;
let successCount = 0;
let failCount = 0;

svgFiles.forEach(file => {
  const inputPath = path.join(iconsDir, file);
  const outputPath = path.join(optimizedDir, file);
  
  try {
    // 获取原始文件大小
    const originalStats = fs.statSync(inputPath);
    const originalSize = originalStats.size;
    totalOriginalSize += originalSize;
    
    // 执行SVGO优化
    const command = `npx svgo "${inputPath}" -o "${outputPath}" --config svgo.config.js`;
    execSync(command, { stdio: 'inherit' });
    
    // 获取优化后文件大小
    const optimizedStats = fs.statSync(outputPath);
    const optimizedSize = optimizedStats.size;
    totalOptimizedSize += optimizedSize;
    
    // 计算压缩率
    const reduction = ((originalSize - optimizedSize) / originalSize * 100).toFixed(2);
    
    console.log(`✓ ${file}: ${originalSize}B → ${optimizedSize}B (${reduction}% 减少)`);
    successCount++;
    
  } catch (error) {
    console.error(`✗ ${file}: 优化失败`, error.message);
    failCount++;
  }
});

// 输出统计信息
console.log('\n========== 优化统计 ==========');
console.log(`处理文件数: ${svgFiles.length}`);
console.log(`成功: ${successCount}`);
console.log(`失败: ${failCount}`);
console.log(`原始大小: ${(totalOriginalSize / 1024).toFixed(2)} KB`);
console.log(`优化大小: ${(totalOptimizedSize / 1024).toFixed(2)} KB`);
console.log(`节省空间: ${((totalOriginalSize - totalOptimizedSize) / 1024).toFixed(2)} KB`);
console.log(`压缩率: ${((totalOriginalSize - totalOptimizedSize) / totalOriginalSize * 100).toFixed(2)}%`);

// 检查是否需要替换原始文件
console.log('\n========== 下一步 ==========');
console.log('1. 检查优化后的图标是否正常显示');
console.log('2. 如果正常，运行以下命令替换原始文件：');
console.log('   mv assets/icons/*.svg assets/icons/backup/');
console.log('   mv assets/icons/optimized/*.svg assets/icons/');
console.log('3. 如果出现问题，可以从备份恢复：');
console.log('   mv assets/icons/backup/*.svg assets/icons/');
