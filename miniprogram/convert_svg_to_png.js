const fs = require('fs');
const path = require('path');
const { execSync } = require('child_process');

const iconsDir = path.join(__dirname, 'assets', 'icons');

function convertSvgToPng(svgFile) {
    const svgPath = path.join(iconsDir, svgFile);
    const pngFile = svgFile.replace('.svg', '.png');
    const pngPath = path.join(iconsDir, pngFile);

    try {
        execSync(`magick "${svgPath}" -resize 81x81 "${pngPath}"`, { stdio: 'inherit' });
        console.log(`✓ 转换成功: ${svgFile} -> ${pngFile}`);
        return true;
    } catch (error) {
        console.log(`✗ 转换失败: ${svgFile}`);
        console.log('提示: 请安装ImageMagick');
        return false;
    }
}

function main() {
    if (!fs.existsSync(iconsDir)) {
        console.log(`目录不存在: ${iconsDir}`);
        return;
    }

    const files = fs.readdirSync(iconsDir);
    const svgFiles = files.filter(file => file.endsWith('.svg'));

    if (svgFiles.length === 0) {
        console.log('未找到SVG文件');
        return;
    }

    console.log(`找到 ${svgFiles.length} 个SVG文件\n`);

    let successCount = 0;
    svgFiles.forEach(svgFile => {
        if (convertSvgToPng(svgFile)) {
            successCount++;
        }
    });

    console.log(`\n转换完成: ${successCount}/${svgFiles.length} 个文件`);
}

main();