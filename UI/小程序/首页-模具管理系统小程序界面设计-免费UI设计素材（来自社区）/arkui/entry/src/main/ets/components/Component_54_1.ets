import { PixArkUIInterface } from "../entity"
import { screenAdapter, toPx, toPy } from "../utils/screenAdapter"


@Component
export struct Component_54_1 {
  private getUserPropsById(id: string): PixArkUIInterface | undefined {
          const matches = this.propList?.filter(n => n.componentId === id)
          if (matches.length === 0) {
            return undefined;
          }
          const merged: PixArkUIInterface = {
            componentId: ""
          };
          for (const item of matches) {
            for (const key of Object.keys(item)) {
              Reflect.set(merged, key, Reflect.get(item, key))
            }
          }
          return merged
        }
  private getOverridePropsById(id: string): PixArkUIInterface[] {
          if(!this.propList){
            return [];
          }
          const prefix = new RegExp( `${id}(/)?`);
          const overrideProps = this.propList.filter(prop => prop.pathString?.startsWith(id));
          overrideProps.forEach(n=>n.pathString = n.pathString?.replace(prefix, ""))
          return overrideProps
        }
  @Prop propList: PixArkUIInterface[];
  build() {
    Column() {
      Row()
      .id('53:3')
      .visibility(this.getUserPropsById('53:3')?.visibility ?? Visibility.Visible)
      .width(this.getUserPropsById('53:3')?.width ?? toPx(375))
      .height(this.getUserPropsById('53:3')?.height ?? toPy(79))
      .opacity(this.getUserPropsById('53:3')?.opacity ?? 1)
      .position(this.getUserPropsById('53:3')?.autoLayoutByParent ?? false ? 
              undefined : {
              top: this.getUserPropsById('53:3')?.top ?? toPy(0),
              left: this.getUserPropsById('53:3')?.left ?? toPx(0)
              })
      .zIndex(1)
      .backgroundColor(this.getUserPropsById('53:3')?.fillPaints?.[0]?.backgroundColor ?? 'rgba(196, 196, 196, 0.7299999594688416)')
      Row()
      .id('53:4')
      .visibility(this.getUserPropsById('53:4')?.visibility ?? Visibility.Visible)
      .width(this.getUserPropsById('53:4')?.width ?? toPx(60))
      .height(this.getUserPropsById('53:4')?.height ?? toPy(60))
      .opacity(this.getUserPropsById('53:4')?.opacity ?? 1)
      .position(this.getUserPropsById('53:4')?.autoLayoutByParent ?? false ? 
              undefined : {
              top: this.getUserPropsById('53:4')?.top ?? toPy(9),
              left: this.getUserPropsById('53:4')?.left ?? toPx(11)
              })
      .zIndex(1)
      .borderRadius(this.getUserPropsById('53:4')?.cornerRadius ?? toPx(13))
      .backgroundColor(this.getUserPropsById('53:4')?.fillPaints?.[0]?.backgroundColor ?? 'rgba(116, 202, 0, 1)')
      Row()
      .id('53:5')
      .visibility(this.getUserPropsById('53:5')?.visibility ?? Visibility.Visible)
      .width(this.getUserPropsById('53:5')?.width ?? toPx(60))
      .height(this.getUserPropsById('53:5')?.height ?? toPy(60))
      .opacity(this.getUserPropsById('53:5')?.opacity ?? 1)
      .position(this.getUserPropsById('53:5')?.autoLayoutByParent ?? false ? 
              undefined : {
              top: this.getUserPropsById('53:5')?.top ?? toPy(9),
              left: this.getUserPropsById('53:5')?.left ?? toPx(85)
              })
      .zIndex(1)
      .borderRadius(this.getUserPropsById('53:5')?.cornerRadius ?? toPx(13))
      .backgroundColor(this.getUserPropsById('53:5')?.fillPaints?.[0]?.backgroundColor ?? 'rgba(116, 202, 0, 1)')
      Column() {
        Text() {
          Span(this.getUserPropsById('53:15_0_1')?.nodeText ?? '初始')
          .visibility(this.getUserPropsById('53:15_0_1')?.visibility ?? Visibility.Visible)
          .fontColor(this.getUserPropsById('53:15_0_1')?.fontColor ?? 'rgba(0, 0, 0, 0.5)')
          .fontFamily(this.getUserPropsById('53:15_0_1')?.fontFamily ?? 'Noto Sans SC')
          .fontWeight(this.getUserPropsById('53:15_0_1')?.fontWeight ?? 'Regular')
          .fontSize(this.getUserPropsById('53:15_0_1')?.fontSize ?? toPx(14))
          .zIndex(1)
        }
        .visibility(this.getUserPropsById('53:15_0')?.visibility ?? Visibility.Visible)
        .width('100%')
        .margin({ left: toPx(0), bottom: toPy(0), right: toPx(0), top: toPy(0) })
        Text() {
          Span(this.getUserPropsById('53:15_1_1')?.nodeText ?? '参数')
          .visibility(this.getUserPropsById('53:15_1_1')?.visibility ?? Visibility.Visible)
          .fontColor(this.getUserPropsById('53:15_1_1')?.fontColor ?? 'rgba(0, 0, 0, 0.5)')
          .fontFamily(this.getUserPropsById('53:15_1_1')?.fontFamily ?? 'Noto Sans SC')
          .fontWeight(this.getUserPropsById('53:15_1_1')?.fontWeight ?? 'Regular')
          .fontSize(this.getUserPropsById('53:15_1_1')?.fontSize ?? toPx(14))
          .zIndex(1)
        }
        .visibility(this.getUserPropsById('53:15_1')?.visibility ?? Visibility.Visible)
        .width('100%')
        .margin({ left: toPx(0), bottom: toPy(0), right: toPx(0), top: toPy(0) })
      }
      .visibility(this.getUserPropsById('53:15')?.visibility ?? Visibility.Visible)
      .width(this.getUserPropsById('53:15')?.width ?? toPx(59))
      .height(this.getUserPropsById('53:15')?.height ?? toPy(51))
      .opacity(this.getUserPropsById('53:15')?.opacity ?? 1)
      .position(this.getUserPropsById('53:15')?.autoLayoutByParent ?? false ? 
              undefined : {
              top: this.getUserPropsById('53:15')?.top ?? toPy(11),
              left: this.getUserPropsById('53:15')?.left ?? toPx(87)
              })
      .zIndex(1)
      Row()
      .id('53:6')
      .visibility(this.getUserPropsById('53:6')?.visibility ?? Visibility.Visible)
      .width(this.getUserPropsById('53:6')?.width ?? toPx(60))
      .height(this.getUserPropsById('53:6')?.height ?? toPy(60))
      .opacity(this.getUserPropsById('53:6')?.opacity ?? 1)
      .position(this.getUserPropsById('53:6')?.autoLayoutByParent ?? false ? 
              undefined : {
              top: this.getUserPropsById('53:6')?.top ?? toPy(9),
              left: this.getUserPropsById('53:6')?.left ?? toPx(159)
              })
      .zIndex(1)
      .borderRadius(this.getUserPropsById('53:6')?.cornerRadius ?? toPx(13))
      .backgroundColor(this.getUserPropsById('53:6')?.fillPaints?.[0]?.backgroundColor ?? 'rgba(116, 202, 0, 1)')
      Row()
      .id('53:7')
      .visibility(this.getUserPropsById('53:7')?.visibility ?? Visibility.Visible)
      .width(this.getUserPropsById('53:7')?.width ?? toPx(60))
      .height(this.getUserPropsById('53:7')?.height ?? toPy(60))
      .opacity(this.getUserPropsById('53:7')?.opacity ?? 1)
      .position(this.getUserPropsById('53:7')?.autoLayoutByParent ?? false ? 
              undefined : {
              top: this.getUserPropsById('53:7')?.top ?? toPy(9),
              left: this.getUserPropsById('53:7')?.left ?? toPx(233)
              })
      .zIndex(1)
      .borderRadius(this.getUserPropsById('53:7')?.cornerRadius ?? toPx(13))
      .backgroundColor(this.getUserPropsById('53:7')?.fillPaints?.[0]?.backgroundColor ?? 'rgba(116, 202, 0, 1)')
      Row()
      .id('53:8')
      .visibility(this.getUserPropsById('53:8')?.visibility ?? Visibility.Visible)
      .width(this.getUserPropsById('53:8')?.width ?? toPx(60))
      .height(this.getUserPropsById('53:8')?.height ?? toPy(60))
      .opacity(this.getUserPropsById('53:8')?.opacity ?? 1)
      .position(this.getUserPropsById('53:8')?.autoLayoutByParent ?? false ? 
              undefined : {
              top: this.getUserPropsById('53:8')?.top ?? toPy(9),
              left: this.getUserPropsById('53:8')?.left ?? toPx(307)
              })
      .zIndex(1)
      .borderRadius(this.getUserPropsById('53:8')?.cornerRadius ?? toPx(13))
      .backgroundColor(this.getUserPropsById('53:8')?.fillPaints?.[0]?.backgroundColor ?? 'rgba(116, 202, 0, 1)')
      Column() {
        Text() {
          Span(this.getUserPropsById('53:14_0_1')?.nodeText ?? '加工')
          .visibility(this.getUserPropsById('53:14_0_1')?.visibility ?? Visibility.Visible)
          .fontColor(this.getUserPropsById('53:14_0_1')?.fontColor ?? 'rgba(0, 0, 0, 0.5)')
          .fontFamily(this.getUserPropsById('53:14_0_1')?.fontFamily ?? 'Noto Sans SC')
          .fontWeight(this.getUserPropsById('53:14_0_1')?.fontWeight ?? 'Regular')
          .fontSize(this.getUserPropsById('53:14_0_1')?.fontSize ?? toPx(14))
          .zIndex(1)
        }
        .visibility(this.getUserPropsById('53:14_0')?.visibility ?? Visibility.Visible)
        .width('100%')
        .margin({ left: toPx(0), bottom: toPy(0), right: toPx(0), top: toPy(0) })
        Text() {
          Span(this.getUserPropsById('53:14_1_1')?.nodeText ?? '申请')
          .visibility(this.getUserPropsById('53:14_1_1')?.visibility ?? Visibility.Visible)
          .fontColor(this.getUserPropsById('53:14_1_1')?.fontColor ?? 'rgba(0, 0, 0, 0.5)')
          .fontFamily(this.getUserPropsById('53:14_1_1')?.fontFamily ?? 'Noto Sans SC')
          .fontWeight(this.getUserPropsById('53:14_1_1')?.fontWeight ?? 'Regular')
          .fontSize(this.getUserPropsById('53:14_1_1')?.fontSize ?? toPx(14))
          .zIndex(1)
        }
        .visibility(this.getUserPropsById('53:14_1')?.visibility ?? Visibility.Visible)
        .width('100%')
        .margin({ left: toPx(0), bottom: toPy(0), right: toPx(0), top: toPy(0) })
      }
      .visibility(this.getUserPropsById('53:14')?.visibility ?? Visibility.Visible)
      .width(this.getUserPropsById('53:14')?.width ?? toPx(59))
      .height(this.getUserPropsById('53:14')?.height ?? toPy(51))
      .opacity(this.getUserPropsById('53:14')?.opacity ?? 1)
      .position(this.getUserPropsById('53:14')?.autoLayoutByParent ?? false ? 
              undefined : {
              top: this.getUserPropsById('53:14')?.top ?? toPy(12),
              left: this.getUserPropsById('53:14')?.left ?? toPx(13)
              })
      .zIndex(1)
      Column() {
        Text() {
          Span(this.getUserPropsById('53:16_0_1')?.nodeText ?? '新模')
          .visibility(this.getUserPropsById('53:16_0_1')?.visibility ?? Visibility.Visible)
          .fontColor(this.getUserPropsById('53:16_0_1')?.fontColor ?? 'rgba(0, 0, 0, 0.5)')
          .fontFamily(this.getUserPropsById('53:16_0_1')?.fontFamily ?? 'Noto Sans SC')
          .fontWeight(this.getUserPropsById('53:16_0_1')?.fontWeight ?? 'Regular')
          .fontSize(this.getUserPropsById('53:16_0_1')?.fontSize ?? toPx(14))
          .zIndex(1)
        }
        .visibility(this.getUserPropsById('53:16_0')?.visibility ?? Visibility.Visible)
        .width('100%')
        .margin({ left: toPx(0), bottom: toPy(0), right: toPx(0), top: toPy(0) })
        Text() {
          Span(this.getUserPropsById('53:16_1_1')?.nodeText ?? '建档')
          .visibility(this.getUserPropsById('53:16_1_1')?.visibility ?? Visibility.Visible)
          .fontColor(this.getUserPropsById('53:16_1_1')?.fontColor ?? 'rgba(0, 0, 0, 0.5)')
          .fontFamily(this.getUserPropsById('53:16_1_1')?.fontFamily ?? 'Noto Sans SC')
          .fontWeight(this.getUserPropsById('53:16_1_1')?.fontWeight ?? 'Regular')
          .fontSize(this.getUserPropsById('53:16_1_1')?.fontSize ?? toPx(14))
          .zIndex(1)
        }
        .visibility(this.getUserPropsById('53:16_1')?.visibility ?? Visibility.Visible)
        .width('100%')
        .margin({ left: toPx(0), bottom: toPy(0), right: toPx(0), top: toPy(0) })
      }
      .visibility(this.getUserPropsById('53:16')?.visibility ?? Visibility.Visible)
      .width(this.getUserPropsById('53:16')?.width ?? toPx(59))
      .height(this.getUserPropsById('53:16')?.height ?? toPy(51))
      .opacity(this.getUserPropsById('53:16')?.opacity ?? 1)
      .position(this.getUserPropsById('53:16')?.autoLayoutByParent ?? false ? 
              undefined : {
              top: this.getUserPropsById('53:16')?.top ?? toPy(11),
              left: this.getUserPropsById('53:16')?.left ?? toPx(161)
              })
      .zIndex(1)
      Column() {
        Text() {
          Span(this.getUserPropsById('53:17_0_1')?.nodeText ?? '模具')
          .visibility(this.getUserPropsById('53:17_0_1')?.visibility ?? Visibility.Visible)
          .fontColor(this.getUserPropsById('53:17_0_1')?.fontColor ?? 'rgba(0, 0, 0, 0.5)')
          .fontFamily(this.getUserPropsById('53:17_0_1')?.fontFamily ?? 'Noto Sans SC')
          .fontWeight(this.getUserPropsById('53:17_0_1')?.fontWeight ?? 'Regular')
          .fontSize(this.getUserPropsById('53:17_0_1')?.fontSize ?? toPx(14))
          .zIndex(1)
        }
        .visibility(this.getUserPropsById('53:17_0')?.visibility ?? Visibility.Visible)
        .width('100%')
        .margin({ left: toPx(0), bottom: toPy(0), right: toPx(0), top: toPy(0) })
        Text() {
          Span(this.getUserPropsById('53:17_1_1')?.nodeText ?? '领用')
          .visibility(this.getUserPropsById('53:17_1_1')?.visibility ?? Visibility.Visible)
          .fontColor(this.getUserPropsById('53:17_1_1')?.fontColor ?? 'rgba(0, 0, 0, 0.5)')
          .fontFamily(this.getUserPropsById('53:17_1_1')?.fontFamily ?? 'Noto Sans SC')
          .fontWeight(this.getUserPropsById('53:17_1_1')?.fontWeight ?? 'Regular')
          .fontSize(this.getUserPropsById('53:17_1_1')?.fontSize ?? toPx(14))
          .zIndex(1)
        }
        .visibility(this.getUserPropsById('53:17_1')?.visibility ?? Visibility.Visible)
        .width('100%')
        .margin({ left: toPx(0), bottom: toPy(0), right: toPx(0), top: toPy(0) })
      }
      .visibility(this.getUserPropsById('53:17')?.visibility ?? Visibility.Visible)
      .width(this.getUserPropsById('53:17')?.width ?? toPx(59))
      .height(this.getUserPropsById('53:17')?.height ?? toPy(51))
      .opacity(this.getUserPropsById('53:17')?.opacity ?? 1)
      .position(this.getUserPropsById('53:17')?.autoLayoutByParent ?? false ? 
              undefined : {
              top: this.getUserPropsById('53:17')?.top ?? toPy(11),
              left: this.getUserPropsById('53:17')?.left ?? toPx(235)
              })
      .zIndex(1)
      Column() {
        Text() {
          Span(this.getUserPropsById('53:18_0_1')?.nodeText ?? '模具')
          .visibility(this.getUserPropsById('53:18_0_1')?.visibility ?? Visibility.Visible)
          .fontColor(this.getUserPropsById('53:18_0_1')?.fontColor ?? 'rgba(0, 0, 0, 0.5)')
          .fontFamily(this.getUserPropsById('53:18_0_1')?.fontFamily ?? 'Noto Sans SC')
          .fontWeight(this.getUserPropsById('53:18_0_1')?.fontWeight ?? 'Regular')
          .fontSize(this.getUserPropsById('53:18_0_1')?.fontSize ?? toPx(14))
          .zIndex(1)
        }
        .visibility(this.getUserPropsById('53:18_0')?.visibility ?? Visibility.Visible)
        .width('100%')
        .margin({ left: toPx(0), bottom: toPy(0), right: toPx(0), top: toPy(0) })
        Text() {
          Span(this.getUserPropsById('53:18_1_1')?.nodeText ?? '归还')
          .visibility(this.getUserPropsById('53:18_1_1')?.visibility ?? Visibility.Visible)
          .fontColor(this.getUserPropsById('53:18_1_1')?.fontColor ?? 'rgba(0, 0, 0, 0.5)')
          .fontFamily(this.getUserPropsById('53:18_1_1')?.fontFamily ?? 'Noto Sans SC')
          .fontWeight(this.getUserPropsById('53:18_1_1')?.fontWeight ?? 'Regular')
          .fontSize(this.getUserPropsById('53:18_1_1')?.fontSize ?? toPx(14))
          .zIndex(1)
        }
        .visibility(this.getUserPropsById('53:18_1')?.visibility ?? Visibility.Visible)
        .width('100%')
        .margin({ left: toPx(0), bottom: toPy(0), right: toPx(0), top: toPy(0) })
      }
      .visibility(this.getUserPropsById('53:18')?.visibility ?? Visibility.Visible)
      .width(this.getUserPropsById('53:18')?.width ?? toPx(59))
      .height(this.getUserPropsById('53:18')?.height ?? toPy(51))
      .opacity(this.getUserPropsById('53:18')?.opacity ?? 1)
      .position(this.getUserPropsById('53:18')?.autoLayoutByParent ?? false ? 
              undefined : {
              top: this.getUserPropsById('53:18')?.top ?? toPy(11),
              left: this.getUserPropsById('53:18')?.left ?? toPx(307)
              })
      .zIndex(1)
    }
    .visibility(this.getUserPropsById('54:1')?.visibility ?? Visibility.Visible)
    .width(this.getUserPropsById('54:1')?.width ?? toPx(375))
    .height(this.getUserPropsById('54:1')?.height ?? toPy(79))
    .opacity(this.getUserPropsById('54:1')?.opacity ?? 1)
    .position(this.getUserPropsById('54:1')?.autoLayoutByParent ?? false ? 
              undefined : {
              top: this.getUserPropsById('54:1')?.top ?? toPy(198),
              left: this.getUserPropsById('54:1')?.left ?? toPx(0)
              })
    .zIndex(1)
    .backgroundColor(this.getUserPropsById('54:1')?.fillPaints?.[0]?.backgroundColor ?? 'rgba(255, 255, 255, 0)')
  }
  
}