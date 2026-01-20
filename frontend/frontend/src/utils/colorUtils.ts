// 颜色类型定义
export interface RGB {
  r: number; // 0-255
  g: number; // 0-255
  b: number; // 0-255
}

export interface HSL {
  h: number; // 0-360
  s: number; // 0-100
  l: number; // 0-100
}

export interface CMYK {
  c: number; // 0-100
  m: number; // 0-100
  y: number; // 0-100
  k: number; // 0-100
}

export interface Color {
  hex: string;
  rgb: RGB;
  hsl: HSL;
  cmyk: CMYK;
}

/**
 * 颜色转换工具类
 */
export class ColorConverter {
  /**
   * 将HEX颜色转换为RGB
   * @param hex HEX颜色值（支持#RRGGBB和#RGB格式）
   * @returns RGB对象
   */
  static hexToRgb(hex: string): RGB {
    // 移除#号
    hex = hex.replace('#', '');
    
    // 处理缩写格式（#RGB）
    if (hex.length === 3) {
      hex = hex.split('').map(char => char + char).join('');
    }
    
    const r = parseInt(hex.substring(0, 2), 16);
    const g = parseInt(hex.substring(2, 4), 16);
    const b = parseInt(hex.substring(4, 6), 16);
    
    return { r, g, b };
  }

  /**
   * 将RGB颜色转换为HEX
   * @param rgb RGB对象
   * @returns HEX颜色值
   */
  static rgbToHex(rgb: RGB): string {
    const { r, g, b } = rgb;
    const toHex = (value: number) => {
      const hex = Math.round(value).toString(16);
      return hex.length === 1 ? '0' + hex : hex;
    };
    
    return `#${toHex(r)}${toHex(g)}${toHex(b)}`;
  }

  /**
   * 将RGB颜色转换为HSL
   * @param rgb RGB对象
   * @returns HSL对象
   */
  static rgbToHsl(rgb: RGB): HSL {
    let { r, g, b } = rgb;
    
    // 归一化到0-1范围
    r /= 255;
    g /= 255;
    b /= 255;
    
    const max = Math.max(r, g, b);
    const min = Math.min(r, g, b);
    let h = 0;
    let s = 0;
    const l = (max + min) / 2;
    
    if (max !== min) {
      const d = max - min;
      s = l > 0.5 ? d / (2 - max - min) : d / (max + min);
      
      switch (max) {
        case r:
          h = (g - b) / d + (g < b ? 6 : 0);
          break;
        case g:
          h = (b - r) / d + 2;
          break;
        case b:
          h = (r - g) / d + 4;
          break;
      }
      
      h /= 6;
    }
    
    return {
      h: Math.round(h * 360),
      s: Math.round(s * 100),
      l: Math.round(l * 100)
    };
  }

  /**
   * 将HSL颜色转换为RGB
   * @param hsl HSL对象
   * @returns RGB对象
   */
  static hslToRgb(hsl: HSL): RGB {
    let { h, s, l } = hsl;
    
    h /= 360;
    s /= 100;
    l /= 100;
    
    const hue2rgb = (p: number, q: number, t: number) => {
      if (t < 0) t += 1;
      if (t > 1) t -= 1;
      if (t < 1/6) return p + (q - p) * 6 * t;
      if (t < 1/2) return q;
      if (t < 2/3) return p + (q - p) * (2/3 - t) * 6;
      return p;
    };
    
    let r, g, b;
    
    if (s === 0) {
      r = g = b = l; // 灰色
    } else {
      const q = l < 0.5 ? l * (1 + s) : l + s - l * s;
      const p = 2 * l - q;
      r = hue2rgb(p, q, h + 1/3);
      g = hue2rgb(p, q, h);
      b = hue2rgb(p, q, h - 1/3);
    }
    
    return {
      r: Math.round(r * 255),
      g: Math.round(g * 255),
      b: Math.round(b * 255)
    };
  }

  /**
   * 将RGB颜色转换为CMYK
   * @param rgb RGB对象
   * @returns CMYK对象
   */
  static rgbToCmyk(rgb: RGB): CMYK {
    let { r, g, b } = rgb;
    
    // 归一化到0-1范围
    r /= 255;
    g /= 255;
    b /= 255;
    
    const k = 1 - Math.max(r, g, b);
    
    if (k === 1) {
      return { c: 0, m: 0, y: 0, k: 100 };
    }
    
    const c = (1 - r - k) / (1 - k);
    const m = (1 - g - k) / (1 - k);
    const y = (1 - b - k) / (1 - k);
    
    return {
      c: Math.round(c * 100),
      m: Math.round(m * 100),
      y: Math.round(y * 100),
      k: Math.round(k * 100)
    };
  }

  /**
   * 将CMYK颜色转换为RGB
   * @param cmyk CMYK对象
   * @returns RGB对象
   */
  static cmykToRgb(cmyk: CMYK): RGB {
    let { c, m, y, k } = cmyk;
    
    // 归一化到0-1范围
    c /= 100;
    m /= 100;
    y /= 100;
    k /= 100;
    
    const r = Math.round(255 * (1 - c) * (1 - k));
    const g = Math.round(255 * (1 - m) * (1 - k));
    const b = Math.round(255 * (1 - y) * (1 - k));
    
    return { r, g, b };
  }

  /**
   * 将HSL颜色转换为HEX
   * @param hsl HSL对象
   * @returns HEX颜色值
   */
  static hslToHex(hsl: HSL): string {
    const rgb = this.hslToRgb(hsl);
    return this.rgbToHex(rgb);
  }

  /**
   * 将HEX颜色转换为HSL
   * @param hex HEX颜色值
   * @returns HSL对象
   */
  static hexToHsl(hex: string): HSL {
    const rgb = this.hexToRgb(hex);
    return this.rgbToHsl(rgb);
  }

  /**
   * 将HEX颜色转换为CMYK
   * @param hex HEX颜色值
   * @returns CMYK对象
   */
  static hexToCmyk(hex: string): CMYK {
    const rgb = this.hexToRgb(hex);
    return this.rgbToCmyk(rgb);
  }

  /**
   * 将CMYK颜色转换为HEX
   * @param cmyk CMYK对象
   * @returns HEX颜色值
   */
  static cmykToHex(cmyk: CMYK): string {
    const rgb = this.cmykToRgb(cmyk);
    return this.rgbToHex(rgb);
  }

  /**
   * 将任意颜色格式转换为完整的Color对象
   * @param color 颜色值（HEX字符串、RGB对象、HSL对象或CMYK对象）
   * @returns 完整的Color对象
   */
  static toColor(color: string | RGB | HSL | CMYK): Color {
    let hex: string;
    let rgb: RGB;
    let hsl: HSL;
    let cmyk: CMYK;
    
    if (typeof color === 'string') {
      // HEX字符串
      hex = color;
      rgb = this.hexToRgb(hex);
      hsl = this.rgbToHsl(rgb);
      cmyk = this.rgbToCmyk(rgb);
    } else if ('r' in color && 'g' in color && 'b' in color) {
      // RGB对象
      rgb = color;
      hex = this.rgbToHex(rgb);
      hsl = this.rgbToHsl(rgb);
      cmyk = this.rgbToCmyk(rgb);
    } else if ('h' in color && 's' in color && 'l' in color) {
      // HSL对象
      hsl = color;
      rgb = this.hslToRgb(hsl);
      hex = this.rgbToHex(rgb);
      cmyk = this.rgbToCmyk(rgb);
    } else if ('c' in color && 'm' in color && 'y' in color && 'k' in color) {
      // CMYK对象
      cmyk = color;
      rgb = this.cmykToRgb(cmyk);
      hex = this.rgbToHex(rgb);
      hsl = this.rgbToHsl(rgb);
    } else {
      throw new Error('Invalid color format');
    }
    
    return { hex, rgb, hsl, cmyk };
  }

  /**
   * 生成随机HEX颜色
   * @returns 随机HEX颜色值
   */
  static randomHex(): string {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
  }

  /**
   * 验证HEX颜色格式
   * @param hex 要验证的HEX颜色值
   * @returns 是否为有效的HEX颜色
   */
  static isValidHex(hex: string): boolean {
    const hexRegex = /^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$/;
    return hexRegex.test(hex);
  }
}
