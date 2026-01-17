import os
import subprocess
import sys

def convert_svg_to_png(svg_file, png_file, size=81):
    try:
        from cairosvg import svg2png
        with open(svg_file, 'rb') as f:
            svg_data = f.read()
        svg2png(bytestring=svg_data, write_to=png_file, output_width=size, output_height=size)
        print(f"成功转换: {svg_file} -> {png_file}")
        return True
    except ImportError:
        print("cairosvg库未安装，尝试使用其他方法...")
        return False
    except Exception as e:
        print(f"转换失败: {svg_file} - {str(e)}")
        return False

def convert_svg_to_png_pillow(svg_file, png_file, size=81):
    try:
        from PIL import Image
        import cairosvg
        
        svg_data = open(svg_file, 'rb').read()
        png_data = cairosvg.svg2png(bytestring=svg_data, output_width=size, output_height=size)
        
        img = Image.open(io.BytesIO(png_data))
        img.save(png_file, 'PNG')
        print(f"成功转换: {svg_file} -> {png_file}")
        return True
    except ImportError:
        print("所需库未安装，请运行: pip install cairosvg pillow")
        return False
    except Exception as e:
        print(f"转换失败: {svg_file} - {str(e)}")
        return False

def main():
    icons_dir = "assets/icons"
    
    if not os.path.exists(icons_dir):
        print(f"目录不存在: {icons_dir}")
        return
    
    svg_files = [f for f in os.listdir(icons_dir) if f.endswith('.svg')]
    
    if not svg_files:
        print("未找到SVG文件")
        return
    
    print(f"找到 {len(svg_files)} 个SVG文件")
    
    success_count = 0
    for svg_file in svg_files:
        svg_path = os.path.join(icons_dir, svg_file)
        png_file = svg_file.replace('.svg', '.png')
        png_path = os.path.join(icons_dir, png_file)
        
        if convert_svg_to_png(svg_path, png_path):
            success_count += 1
    
    print(f"\n转换完成: {success_count}/{len(svg_files)} 个文件")

if __name__ == "__main__":
    main()