import { Color, RGB } from './colorUtils';
import { ColorScheme, ColorSchemeType } from './colorSchemeGenerator';

/**
 * 对比度级别
 */
export enum ContrastLevel {
  FAIL = 'fail',          // 不通过
  AA_LARGE = 'aa_large',  // 仅AA级大文本通过
  AA = 'aa',              // AA级通过
  AAA = 'aaa'             // AAA级通过
}

/**
 * 色彩情感类型
 */
export enum ColorEmotion {
  WARM = 'warm',          // 温暖
  COOL = 'cool',          // 凉爽
  NEUTRAL = 'neutral',    // 中性
  ENERGETIC = 'energetic',// 充满活力
  CALM = 'calm',          // 平静
  CHEERFUL = 'cheerful',  // 愉快
  SERIOUS = 'serious',    // 严肃
  FRIENDLY = 'friendly'   // 友好
}

/**
 * 色彩理论工具类
 */
export class ColorTheoryUtils {
  /**
   * 计算两个颜色之间的对比度（WCAG标准）
   * @param color1 第一个颜色
   * @param color2 第二个颜色
   * @returns 对比度比值（1-21）
   */
  static calculateContrast(color1: Color, color2: Color): number {
    const lum1 = this.getRelativeLuminance(color1.rgb);
    const lum2 = this.getRelativeLuminance(color2.rgb);
    
    const lighter = Math.max(lum1, lum2);
    const darker = Math.min(lum1, lum2);
    
    return (lighter + 0.05) / (darker + 0.05);
  }

  /**
   * 计算颜色的相对亮度（用于对比度计算）
   * @param rgb RGB颜色
   * @returns 相对亮度（0-1）
   */
  private static getRelativeLuminance(rgb: RGB): number {
    const { r, g, b } = rgb;
    
    // 将RGB值转换为0-1范围
    const [R, G, B] = [r, g, b].map(value => {
      const v = value / 255;
      return v <= 0.03928 ? v / 12.92 : Math.pow((v + 0.055) / 1.055, 2.4);
    });
    
    // 计算相对亮度
    return 0.2126 * R + 0.7152 * G + 0.0722 * B;
  }

  /**
   * 获取对比度级别
   * @param contrast 对比度比值
   * @returns 对比度级别
   */
  static getContrastLevel(contrast: number): ContrastLevel {
    if (contrast >= 7) {
      return ContrastLevel.AAA;
    } else if (contrast >= 4.5) {
      return ContrastLevel.AA;
    } else if (contrast >= 3) {
      return ContrastLevel.AA_LARGE;
    } else {
      return ContrastLevel.FAIL;
    }
  }

  /**
   * 检查对比度是否符合WCAG标准
   * @param color1 第一个颜色
   * @param color2 第二个颜色
   * @returns 符合标准的信息
   */
  static checkWcagContrast(color1: Color, color2: Color): {
    contrast: number;
    level: ContrastLevel;
    aaPass: boolean;
    aaaPass: boolean;
    aaLargePass: boolean;
  } {
    const contrast = this.calculateContrast(color1, color2);
    const level = this.getContrastLevel(contrast);
    
    return {
      contrast: parseFloat(contrast.toFixed(2)),
      level,
      aaPass: contrast >= 4.5,
      aaaPass: contrast >= 7,
      aaLargePass: contrast >= 3
    };
  }

  /**
   * 评估配色方案的和谐度
   * @param scheme 配色方案
   * @returns 和谐度评分（0-100）
   */
  static evaluateHarmony(scheme: ColorScheme): number {
    switch (scheme.type) {
      case ColorSchemeType.COMPLEMENTARY:
        // 互补色通常具有较高的对比度和和谐度
        return 85;
      case ColorSchemeType.ANALOGOUS:
        // 类似色具有很高的和谐度
        return 95;
      case ColorSchemeType.TRIAD:
        // 三角色具有良好的平衡和谐度
        return 80;
      case ColorSchemeType.TETRAD_RECTANGLE:
        // 矩形四角色和谐度适中
        return 75;
      case ColorSchemeType.TETRAD_SQUARE:
        // 正方形四角色和谐度适中
        return 70;
      case ColorSchemeType.MONOCHROMATIC:
        // 单色方案和谐度最高
        return 98;
      case ColorSchemeType.SPLIT_COMPLEMENTARY:
        // 分裂互补色和谐度较高
        return 88;
      default:
        return 50;
    }
  }

  /**
   * 判断颜色的情感倾向
   * @param color 颜色
   * @returns 情感类型列表
   */
  static getColorEmotions(color: Color): ColorEmotion[] {
    const hsl = color.hsl;
    const emotions: ColorEmotion[] = [];
    
    // 根据色相判断冷暖
    if (hsl.h >= 0 && hsl.h < 60 || hsl.h >= 300 && hsl.h <= 360) {
      emotions.push(ColorEmotion.WARM, ColorEmotion.ENERGETIC);
    } else if (hsl.h >= 60 && hsl.h < 180) {
      emotions.push(ColorEmotion.CHEERFUL, ColorEmotion.FRIENDLY);
    } else {
      emotions.push(ColorEmotion.COOL, ColorEmotion.CALM);
    }
    
    // 根据明度和饱和度调整
    if (hsl.l <= 30) {
      emotions.push(ColorEmotion.SERIOUS);
    } else if (hsl.l >= 70) {
      emotions.push(ColorEmotion.CHEERFUL);
    }
    
    if (hsl.s <= 20) {
      emotions.push(ColorEmotion.NEUTRAL, ColorEmotion.CALM);
    }
    
    return [...new Set(emotions)];
  }

  /**
   * 获取颜色的温度描述
   * @param color 颜色
   * @returns 温度描述
   */
  static getColorTemperature(color: Color): string {
    const hsl = color.hsl;
    
    if (hsl.h >= 0 && hsl.h < 60 || hsl.h >= 300 && hsl.h <= 360) {
      return '暖色';
    } else if (hsl.h >= 60 && hsl.h < 180) {
      return '中性色';
    } else {
      return '冷色';
    }
  }

  /**
   * 获取配色方案的专业建议
   * @param scheme 配色方案
   * @returns 建议文本
   */
  static getSchemeAdvice(scheme: ColorScheme): string[] {
    const advice: string[] = [];
    
    // 基础建议
    advice.push(`这是一个${scheme.name}，${scheme.description}`);
    
    // 根据配色类型给出具体建议
    switch (scheme.type) {
      case ColorSchemeType.COMPLEMENTARY:
        advice.push('互补色方案对比强烈，建议将其中一种颜色作为主色，另一种作为强调色使用');
        advice.push('适合需要突出重点的设计，但要注意避免视觉疲劳');
        break;
      case ColorSchemeType.ANALOGOUS:
        advice.push('类似色方案和谐统一，适合需要营造特定氛围的设计');
        advice.push('建议选择其中一种颜色作为主色，其他作为辅助色，以增加层次感');
        break;
      case ColorSchemeType.TRIAD:
        advice.push('三角色方案色彩丰富，平衡和谐，适合活泼、现代的设计');
        advice.push('建议将三种颜色按60-30-10的比例使用，以保持视觉平衡');
        break;
      case ColorSchemeType.MONOCHROMATIC:
        advice.push('单色方案简洁优雅，适合需要专业感和一致性的设计');
        advice.push('可以通过调整明度和饱和度来创建层次感，避免单调');
        break;
      case ColorSchemeType.SPLIT_COMPLEMENTARY:
        advice.push('分裂互补色方案既有对比又较和谐，适合需要活力但又不想太刺眼的设计');
        advice.push('建议将基础色作为主色，分裂互补色作为辅助色和强调色');
        break;
    }
    
    // 对比度检查建议
    const contrast = this.calculateContrast(scheme.colors[0], scheme.colors[scheme.colors.length - 1]);
    const contrastLevel = this.getContrastLevel(contrast);
    
    if (contrastLevel === ContrastLevel.FAIL) {
      advice.push('⚠️ 警告：该配色方案的对比度较低，可能影响文本可读性，不建议用于正文文本');
    } else if (contrastLevel === ContrastLevel.AA_LARGE) {
      advice.push('ℹ️ 提示：该配色方案仅适合大文本（18pt+），正文文本建议使用更高对比度的配色');
    } else {
      advice.push('✅ 该配色方案的对比度符合无障碍标准，可以放心使用');
    }
    
    // 情感建议
    const emotions = this.getColorEmotions(scheme.baseColor);
    const emotionDescriptions = {
      [ColorEmotion.WARM]: '温暖',
      [ColorEmotion.COOL]: '凉爽',
      [ColorEmotion.NEUTRAL]: '中性',
      [ColorEmotion.ENERGETIC]: '充满活力',
      [ColorEmotion.CALM]: '平静',
      [ColorEmotion.CHEERFUL]: '愉快',
      [ColorEmotion.SERIOUS]: '严肃',
      [ColorEmotion.FRIENDLY]: '友好'
    };
    
    const emotionText = emotions.map(emotion => emotionDescriptions[emotion]).join('、');
    advice.push(`该配色方案传达${emotionText}的情感，适合相应风格的设计`);
    
    return advice;
  }

  /**
   * 获取无障碍设计建议
   * @param color1 背景色
   * @param color2 前景色
   * @returns 无障碍建议
   */
  static getAccessibilityAdvice(color1: Color, color2: Color): string[] {
    const { contrast, aaPass, aaaPass, aaLargePass } = this.checkWcagContrast(color1, color2);
    const advice: string[] = [];
    
    advice.push(`当前对比度：${contrast.toFixed(2)}:1`);
    
    if (aaaPass) {
      advice.push('✅ 符合WCAG AAA级标准（最高级别），适合所有文本大小');
    } else if (aaPass) {
      advice.push('✅ 符合WCAG AA级标准，适合正常大小文本');
      advice.push('ℹ️ 提示：若要达到AAA级，建议提高对比度至7:1以上');
    } else if (aaLargePass) {
      advice.push('⚠️ 仅符合WCAG AA级大文本标准（18pt+或14pt+粗体）');
      advice.push('建议提高对比度至4.5:1以上，以支持所有文本大小');
    } else {
      advice.push('❌ 不符合WCAG标准，不建议用于文本内容');
      advice.push('建议调整颜色，将对比度提高至3:1以上');
    }
    
    return advice;
  }

  /**
   * 检查配色方案的可访问性
   * @param scheme 配色方案
   * @returns 可访问性报告
   */
  static checkSchemeAccessibility(scheme: ColorScheme): {
    contrastChecks: Array<{
      color1: Color;
      color2: Color;
      contrast: number;
      level: ContrastLevel;
    }>;
    overallScore: number;
    issues: string[];
  } {
    const contrastChecks = [];
    const issues: string[] = [];
    let passedChecks = 0;
    const totalChecks = scheme.colors.length - 1;
    
    // 检查相邻颜色的对比度
    for (let i = 0; i < scheme.colors.length - 1; i++) {
      const color1 = scheme.colors[i];
      const color2 = scheme.colors[i + 1];
      const contrast = this.calculateContrast(color1, color2);
      const level = this.getContrastLevel(contrast);
      
      contrastChecks.push({ color1, color2, contrast, level });
      
      if (level !== ContrastLevel.FAIL) {
        passedChecks++;
      } else {
        issues.push(`颜色 ${i + 1} 和 ${i + 2} 的对比度不足（${contrast.toFixed(2)}:1）`);
      }
    }
    
    const overallScore = Math.round((passedChecks / totalChecks) * 100);
    
    return {
      contrastChecks,
      overallScore,
      issues
    };
  }
}
