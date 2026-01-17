import { PixArkUIInterface } from "../entity"
import { screenAdapter, toPx, toPy } from "../utils/screenAdapter"


@Component
export struct Component_54_2 {
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
      .id('53:20')
      .visibility(this.getUserPropsById('53:20')?.visibility ?? Visibility.Visible)
      .width(this.getUserPropsById('53:20')?.width ?? toPx(166))
      .height(this.getUserPropsById('53:20')?.height ?? toPy(69))
      .opacity(this.getUserPropsById('53:20')?.opacity ?? 1)
      .position(this.getUserPropsById('53:20')?.autoLayoutByParent ?? false ? 
              undefined : {
              top: this.getUserPropsById('53:20')?.top ?? toPy(0),
              left: this.getUserPropsById('53:20')?.left ?? toPx(0)
              })
      .zIndex(1)
      .borderRadius(this.getUserPropsById('53:20')?.cornerRadius ?? toPx(5))
      .shadow({ radius: 4, offsetX: 0, offsetY: 4, fill: false, color: 'rgba(0, 0, 0, 0.25)' })
      .backgroundColor(this.getUserPropsById('53:20')?.fillPaints?.[0]?.backgroundColor ?? 'rgba(128, 128, 128, 1)')
      Column() {
        Text() {
          Span(this.getUserPropsById('53:21_0_1')?.nodeText ?? '模具合格率')
          .visibility(this.getUserPropsById('53:21_0_1')?.visibility ?? Visibility.Visible)
          .fontColor(this.getUserPropsById('53:21_0_1')?.fontColor ?? 'rgba(0, 0, 0, 0.5)')
          .fontFamily(this.getUserPropsById('53:21_0_1')?.fontFamily ?? 'Noto Sans SC')
          .fontWeight(this.getUserPropsById('53:21_0_1')?.fontWeight ?? 'Regular')
          .fontSize(this.getUserPropsById('53:21_0_1')?.fontSize ?? toPx(16))
          .zIndex(1)
        }
        .visibility(this.getUserPropsById('53:21_0')?.visibility ?? Visibility.Visible)
        .width('100%')
        .margin({ left: toPx(0), bottom: toPy(0), right: toPx(0), top: toPy(0) })
      }
      .visibility(this.getUserPropsById('53:21')?.visibility ?? Visibility.Visible)
      .width(this.getUserPropsById('53:21')?.width ?? toPx(154.85))
      .height(this.getUserPropsById('53:21')?.height ?? toPy(25.33))
      .opacity(this.getUserPropsById('53:21')?.opacity ?? 1)
      .position(this.getUserPropsById('53:21')?.autoLayoutByParent ?? false ? 
              undefined : {
              top: this.getUserPropsById('53:21')?.top ?? toPy(34.94),
              left: this.getUserPropsById('53:21')?.left ?? toPx(6.19)
              })
      .zIndex(1)
      Column() {
        Text() {
          Span(this.getUserPropsById('53:22_0_1')?.nodeText ?? '98.5%')
          .visibility(this.getUserPropsById('53:22_0_1')?.visibility ?? Visibility.Visible)
          .fontColor(this.getUserPropsById('53:22_0_1')?.fontColor ?? 'rgba(246, 245, 255, 1)')
          .fontFamily(this.getUserPropsById('53:22_0_1')?.fontFamily ?? 'Noto Sans SC')
          .fontWeight(this.getUserPropsById('53:22_0_1')?.fontWeight ?? 'Regular')
          .fontSize(this.getUserPropsById('53:22_0_1')?.fontSize ?? toPx(24))
          .zIndex(1)
        }
        .visibility(this.getUserPropsById('53:22_0')?.visibility ?? Visibility.Visible)
        .width('100%')
        .margin({ left: toPx(0), bottom: toPy(0), right: toPx(0), top: toPy(0) })
      }
      .visibility(this.getUserPropsById('53:22')?.visibility ?? Visibility.Visible)
      .width(this.getUserPropsById('53:22')?.width ?? toPx(80.52))
      .height(this.getUserPropsById('53:22')?.height ?? toPy(29.7))
      .opacity(this.getUserPropsById('53:22')?.opacity ?? 1)
      .position(this.getUserPropsById('53:22')?.autoLayoutByParent ?? false ? 
              undefined : {
              top: this.getUserPropsById('53:22')?.top ?? toPy(7.86),
              left: this.getUserPropsById('53:22')?.left ?? toPx(44.6)
              })
      .zIndex(1)
    }
    .visibility(this.getUserPropsById('54:2')?.visibility ?? Visibility.Visible)
    .width(this.getUserPropsById('54:2')?.width ?? toPx(166))
    .height(this.getUserPropsById('54:2')?.height ?? toPy(69))
    .opacity(this.getUserPropsById('54:2')?.opacity ?? 1)
    .position(this.getUserPropsById('54:2')?.autoLayoutByParent ?? false ? 
              undefined : {
              top: this.getUserPropsById('54:2')?.top ?? toPy(0),
              left: this.getUserPropsById('54:2')?.left ?? toPx(0)
              })
    .zIndex(1)
    .borderRadius(this.getUserPropsById('54:2')?.cornerRadius ?? toPx(5))
    .backgroundColor(this.getUserPropsById('54:2')?.fillPaints?.[0]?.backgroundColor ?? 'rgba(255, 120, 117, 1)')
  }
  
}