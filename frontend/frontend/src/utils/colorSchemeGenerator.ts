import { ColorConverter, HSL, Color } from './colorUtils';

/**
 * 配色模式类型
 */
export enum ColorSchemeType {
  COMPLEMENTARY = 'complementary',      // 互补色
  ANALOGOUS = 'analogous',              // 类似色
  TRIAD = 'triad',                      // 三角色
  TETRAD_RECTANGLE = 'tetrad_rectangle', // 四角色（矩形）
  TETRAD_SQUARE = 'tetrad_square',       // 四角色（正方形）
  MONOCHROMATIC = 'monochromatic',      // 单色
  SPLIT_COMPLEMENTARY = 'split_complementary', // 分裂互补色
  NEUTRAL = 'neutral'                   // 中性色
}

/**
 * 配色方案接口
 */
export interface ColorScheme {
  type: ColorSchemeType;
  baseColor: Color;
  colors: Color[];
  name: string;
  description: string;
}

/**
 * 配色算法工具类
 */
export class ColorSchemeGenerator {
  /**
   * 生成互补色配色方案
   * @param baseColor 基础颜色
   * @returns 配色方案
   */
  static generateComplementary(baseColor: Color): ColorScheme {
    const hsl = baseColor.hsl;
    
    // 互补色：色相相差180度
    const complementaryHsl: HSL = {
      ...hsl,
      h: (hsl.h + 180) % 360
    };
    
    const complementaryColor = ColorConverter.toColor(complementaryHsl);
    
    return {
      type: ColorSchemeType.COMPLEMENTARY,
      baseColor,
      colors: [baseColor, complementaryColor],
      name: '互补色方案',
      description: '使用色轮上相对的两种颜色，形成强烈对比'
    };
  }

  /**
   * 生成类似色配色方案
   * @param baseColor 基础颜色
   * @param count 颜色数量（默认为3）
   * @param angle 相邻颜色的角度差（默认为30度）
   * @returns 配色方案
   */
  static generateAnalogous(baseColor: Color, count: number = 3, angle: number = 30): ColorScheme {
    const hsl = baseColor.hsl;
    const colors: Color[] = [];
    
    // 计算起始角度，确保基础色在中间
    const startAngle = hsl.h - (angle * Math.floor(count / 2));
    
    for (let i = 0; i < count; i++) {
      const currentHue = (startAngle + (angle * i) + 360) % 360;
      const analogousHsl: HSL = {
        ...hsl,
        h: currentHue
      };
      colors.push(ColorConverter.toColor(analogousHsl));
    }
    
    return {
      type: ColorSchemeType.ANALOGOUS,
      baseColor,
      colors,
      name: '类似色方案',
      description: '使用色轮上相邻的颜色，形成和谐统一的效果'
    };
  }

  /**
   * 生成三角色配色方案
   * @param baseColor 基础颜色
   * @returns 配色方案
   */
  static generateTriad(baseColor: Color): ColorScheme {
    const hsl = baseColor.hsl;
    const colors: Color[] = [baseColor];
    
    // 三角色：色相相差120度
    for (let i = 1; i < 3; i++) {
      const triadHsl: HSL = {
        ...hsl,
        h: (hsl.h + (120 * i)) % 360
      };
      colors.push(ColorConverter.toColor(triadHsl));
    }
    
    return {
      type: ColorSchemeType.TRIAD,
      baseColor,
      colors,
      name: '三角色方案',
      description: '使用色轮上均匀分布的三种颜色，形成平衡的对比效果'
    };
  }

  /**
   * 生成矩形四角色配色方案
   * @param baseColor 基础颜色
   * @returns 配色方案
   */
  static generateTetradRectangle(baseColor: Color): ColorScheme {
    const hsl = baseColor.hsl;
    const colors: Color[] = [baseColor];
    
    // 矩形四角色：色相相差90度和180度
    const angles = [90, 180, 270];
    
    for (const angle of angles) {
      const tetradHsl: HSL = {
        ...hsl,
        h: (hsl.h + angle) % 360
      };
      colors.push(ColorConverter.toColor(tetradHsl));
    }
    
    return {
      type: ColorSchemeType.TETRAD_RECTANGLE,
      baseColor,
      colors,
      name: '矩形四角色方案',
      description: '使用色轮上形成矩形的四种颜色，既有对比又有和谐'
    };
  }

  /**
   * 生成正方形四角色配色方案
   * @param baseColor 基础颜色
   * @returns 配色方案
   */
  static generateTetradSquare(baseColor: Color): ColorScheme {
    const hsl = baseColor.hsl;
    const colors: Color[] = [baseColor];
    
    // 正方形四角色：色相相差90度
    for (let i = 1; i < 4; i++) {
      const tetradHsl: HSL = {
        ...hsl,
        h: (hsl.h + (90 * i)) % 360
      };
      colors.push(ColorConverter.toColor(tetradHsl));
    }
    
    return {
      type: ColorSchemeType.TETRAD_SQUARE,
      baseColor,
      colors,
      name: '正方形四角色方案',
      description: '使用色轮上均匀分布的四种颜色，形成稳定的平衡'
    };
  }

  /**
   * 生成单色配色方案
   * @param baseColor 基础颜色
   * @param count 颜色数量（默认为5）
   * @returns 配色方案
   */
  static generateMonochromatic(baseColor: Color, count: number = 5): ColorScheme {
    const hsl = baseColor.hsl;
    const colors: Color[] = [];
    
    // 计算明度变化范围
    const lightnessStep = 100 / (count - 1);
    
    for (let i = 0; i < count; i++) {
      const monochromaticHsl: HSL = {
        ...hsl,
        l: Math.min(100, Math.max(0, lightnessStep * i))
      };
      colors.push(ColorConverter.toColor(monochromaticHsl));
    }
    
    return {
      type: ColorSchemeType.MONOCHROMATIC,
      baseColor,
      colors,
      name: '单色方案',
      description: '使用同一颜色的不同明度变体，形成简洁统一的效果'
    };
  }

  /**
   * 生成分裂互补色配色方案
   * @param baseColor 基础颜色
   * @returns 配色方案
   */
  static generateSplitComplementary(baseColor: Color): ColorScheme {
    const hsl = baseColor.hsl;
    const colors: Color[] = [baseColor];
    
    // 分裂互补色：互补色两侧的颜色（通常相差150度和210度）
    const angles = [150, 210];
    
    for (const angle of angles) {
      const splitComplementaryHsl: HSL = {
        ...hsl,
        h: (hsl.h + angle) % 360
      };
      colors.push(ColorConverter.toColor(splitComplementaryHsl));
    }
    
    return {
      type: ColorSchemeType.SPLIT_COMPLEMENTARY,
      baseColor,
      colors,
      name: '分裂互补色方案',
      description: '使用基础色及其互补色两侧的颜色，既有对比又较和谐'
    };
  }

  /**
   * 生成中性色配色方案
   * @param baseColor 基础颜色
   * @returns 配色方案
   */
  static generateNeutral(baseColor: Color): ColorScheme {
    const hsl = baseColor.hsl;
    const colors: Color[] = [];
    
    // 中性色：低饱和度、中等明度的颜色变体
    const neutralConfigs = [
      { s: 5, l: 95 },   // 极浅灰白
      { s: 8, l: 85 },   // 浅灰
      { s: 10, l: 70 },  // 浅灰粉
      { s: 12, l: 55 },  // 中性灰
      { s: 15, l: 40 },  // 深灰
      { s: 20, l: 25 },  // 深灰褐
      { s: 25, l: 15 }   // 近黑
    ];
    
    for (const config of neutralConfigs) {
      const neutralHsl: HSL = {
        ...hsl,
        s: config.s,
        l: config.l
      };
      colors.push(ColorConverter.toColor(neutralHsl));
    }
    
    return {
      type: ColorSchemeType.NEUTRAL,
      baseColor,
      colors,
      name: '中性色方案',
      description: '基于基础色的低饱和度变体，适合专业、简约的设计风格'
    };
  }

  /**
   * 根据类型生成配色方案
   * @param type 配色方案类型
   * @param baseColor 基础颜色
   * @returns 配色方案
   */
  static generateScheme(type: ColorSchemeType, baseColor: Color): ColorScheme {
    switch (type) {
      case ColorSchemeType.COMPLEMENTARY:
        return this.generateComplementary(baseColor);
      case ColorSchemeType.ANALOGOUS:
        return this.generateAnalogous(baseColor);
      case ColorSchemeType.TRIAD:
        return this.generateTriad(baseColor);
      case ColorSchemeType.TETRAD_RECTANGLE:
        return this.generateTetradRectangle(baseColor);
      case ColorSchemeType.TETRAD_SQUARE:
        return this.generateTetradSquare(baseColor);
      case ColorSchemeType.MONOCHROMATIC:
        return this.generateMonochromatic(baseColor);
      case ColorSchemeType.SPLIT_COMPLEMENTARY:
        return this.generateSplitComplementary(baseColor);
      case ColorSchemeType.NEUTRAL:
        return this.generateNeutral(baseColor);
      default:
        throw new Error(`Unsupported color scheme type: ${type}`);
    }
  }

  /**
   * 获取所有支持的配色模式
   * @returns 配色模式列表
   */
  static getSupportedSchemes(): { type: ColorSchemeType; name: string; description: string }[] {
    return [
      {
        type: ColorSchemeType.COMPLEMENTARY,
        name: '互补色',
        description: '色轮上相对的两种颜色，形成强烈对比'
      },
      {
        type: ColorSchemeType.ANALOGOUS,
        name: '类似色',
        description: '色轮上相邻的颜色，形成和谐统一的效果'
      },
      {
        type: ColorSchemeType.TRIAD,
        name: '三角色',
        description: '色轮上均匀分布的三种颜色，形成平衡的对比'
      },
      {
        type: ColorSchemeType.TETRAD_RECTANGLE,
        name: '四角色（矩形）',
        description: '色轮上形成矩形的四种颜色，既有对比又有和谐'
      },
      {
        type: ColorSchemeType.TETRAD_SQUARE,
        name: '四角色（正方形）',
        description: '色轮上均匀分布的四种颜色，形成稳定的平衡'
      },
      {
        type: ColorSchemeType.MONOCHROMATIC,
        name: '单色',
        description: '同一颜色的不同明度变体，形成简洁统一的效果'
      },
      {
        type: ColorSchemeType.SPLIT_COMPLEMENTARY,
        name: '分裂互补色',
        description: '基础色及其互补色两侧的颜色，既有对比又较和谐'
      },
      {
        type: ColorSchemeType.NEUTRAL,
        name: '中性色',
        description: '基于基础色的低饱和度变体，适合专业、简约的设计风格'
      }
    ];
  }
}
