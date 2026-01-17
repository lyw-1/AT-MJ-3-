import { Component_54_2 } from '../components/Component_54_2';
import { LengthMetrics } from '@kit.ArkUI';
import { screenAdapter, toPx, toPy } from "../utils/screenAdapter"
screenAdapter.designWidth = 375;

@Entry
@Component
export struct Frame_1_3 {
  private scrollController = new Scroller()
  build() {
    Scroll(this.scrollController) {
      Stack() {
        Row()
        .id('1:4')
        .height(toPy(1055))
        .width(toPx(375))
        .opacity(1)
        .position({ left: toPx(-5), top: toPy(0) })
        .backgroundColor('rgba(255, 255, 255, 1)')
        Stack() {
          Row()
          .id('1:157')
          .height(toPy(100))
          .width(toPx(375))
          .opacity(1)
          .position({ left: toPx(0), top: toPy(0) })
          .backgroundColor('rgba(255, 255, 255, 1)')
          Stack() {
            Row()
            .id('1:159')
            .height(toPy(100))
            .width(toPx(375))
            .opacity(1)
            .position({ left: toPx(0), top: toPy(0) })
          }
          .height(toPy(100))
          .width(toPx(375))
          .opacity(1)
          .clip(true)
          .position({ left: toPx(0), top: toPy(0) })
          Column() {
            Text() {
              Span(`主页`)
              .fontColor('rgba(35, 34, 39, 1)')
              .fontFamily('Noto Sans SC')
              .fontWeight('Bold')
              .fontSize(toPx(16))
            }
            .width('100%')
            .margin({ left: toPx(0), bottom: toPy(0), right: toPx(0), top: toPy(0) })
          }
          .height(toPy(24))
          .width(toPx(33))
          .opacity(1)
          .position({ left: toPx(172), top: toPy(54) })
          Stack() {
            Stack() {
              Row()
              .id('1:163')
              .height(toPy(11.33))
              .width(toPx(22))
              .opacity(0.3499999940395355)
              .position({ left: toPx(0), top: toPy(0) })
              .borderRadius(toPx(2.6666667461395264))
              .borderWidth({ left: toPx(1), right: toPx(1), top: toPy(1), bottom: toPy(1) })
              .borderColor('rgba(0, 0, 0, 1)')
              Image($r('app.media.Vector_1_164'))
              .height(toPy(4))
              .width(toPx(1.33))
              .opacity(0.4000000059604645)
              .position({ left: toPx(23), top: toPy(3.67) })
              Row()
              .id('1:165')
              .height(toPy(7.33))
              .width(toPx(18))
              .opacity(1)
              .position({ left: toPx(2), top: toPy(2) })
              .borderRadius(toPx(1.3333333730697632))
              .backgroundColor('rgba(0, 0, 0, 1)')
            }
            .height(toPy(11.33))
            .width(toPx(24.33))
            .opacity(1)
            .clip(true)
            .position({ left: toPx(315.33), top: toPy(1.33) })
            Image($r('app.media.Boolean_operation_1_166'))
            .height(toPy(11))
            .width(toPx(15.33))
            .opacity(1)
            .position({ left: toPx(295), top: toPy(1.33) })
            Image($r('app.media.Boolean_operation_1_170'))
            .height(toPy(10.67))
            .width(toPx(17))
            .opacity(1)
            .position({ left: toPx(273), top: toPy(1.67) })
            Stack() {
              Column() {
                Text() {
                  Span(`9:41`)
                  .fontColor('rgba(0, 0, 0, 1)')
                  .fontFamily('Noto Sans SC')
                  .fontWeight('Regular')
                  .fontSize(toPx(15))
                }
                .width('100%')
                .margin({ left: toPx(0), bottom: toPy(0), right: toPx(0), top: toPy(0) })
              }
              .height(toPy(22))
              .width(toPx(53.7))
              .opacity(1)
              .position({ left: toPx(0), top: toPy(0) })
            }
            .height(toPy(22))
            .width(toPx(54))
            .opacity(1)
            .clip(true)
            .position({ left: toPx(0), top: toPy(0) })
          }
          .height(toPy(22))
          .width(toPx(339.66))
          .opacity(1)
          .clip(true)
          .position({ left: toPx(21), top: toPy(16) })
          Stack() {
            Image($r('app.media.Boolean_operation_1_179'))
            .height(toPy(5))
            .width(toPx(9.39))
            .opacity(1)
            .position({ left: toPx(6.74), top: toPy(5.95) })
            Image($r('app.media.Vector_1_182'))
            .height(toPy(19.93))
            .width(toPx(16.49))
            .opacity(1)
            .position({ left: toPx(3.2), top: toPy(1.75) })
            Image($r('app.media.Boolean_operation_1_183'))
            .height(toPy(9.84))
            .width(toPx(9.72))
            .opacity(1)
            .position({ left: toPx(11.09), top: toPy(12.34) })
          }
          .height(toPy(23.98))
          .width(toPx(23.98))
          .opacity(1)
          .clip(true)
          .position({ left: toPx(287), top: toPy(60) })
          Stack() {
            Image($r('app.media.Boolean_operation_1_188'))
            .height(toPy(15.15))
            .width(toPx(17.45))
            .opacity(1)
            .position({ left: toPx(0), top: toPy(5.87) })
            Image($r('app.media.Vector_1_191'))
            .height(toPy(12.6))
            .width(toPx(17.45))
            .opacity(1)
            .position({ left: toPx(4.94), top: toPy(1.08) })
            Image($r('app.media.Boolean_operation_1_192'))
            .height(toPy(4.19))
            .width(toPx(9.06))
            .opacity(1)
            .position({ left: toPx(4.19), top: toPy(9.83) })
            Image($r('app.media.Ellipse_1_195'))
            .height(toPy(6))
            .width(toPx(6))
            .opacity(1)
            .position({ left: toPx(18), top: toPy(0) })
          }
          .height(toPy(22.4))
          .width(toPx(24))
          .opacity(1)
          .clip(true)
          .position({ left: toPx(319), top: toPy(60) })
          Stack() {
            Image($r('app.media.Boolean_operation_1_198'))
            .height(toPy(16.5))
            .width(toPx(16.5))
            .opacity(1)
            .position({ left: toPx(3.75), top: toPy(3.75) })
          }
          .height(toPy(24))
          .width(toPx(24))
          .opacity(1)
          .clip(true)
          .position({ left: toPx(32), top: toPy(60) })
          Stack() {
            Stack() {
              Image($r('app.media.Frame_50_3'))
              .height(toPy(88))
              .width(toPx(375))
              .opacity(1)
              .position({ left: toPx(0), top: toPy(0) })
              Stack() {
                Row()
                .id('50:6')
                .height(toPy(0.5))
                .width(toPx(375))
                .opacity(1)
                .position({ left: toPx(0), top: toPy(0.5) })
                .backgroundColor('rgba(0, 0, 0, 0.09803921729326248)')
              }
              .id('50:5')
              .height(toPy(1))
              .width(toPx(375))
              .opacity(1)
              .position({ left: toPx(0), top: toPy(87) })
              Stack() {
                Stack() {
                  Image($r('app.media.Group_50_12'))
                  .height(toPy(3.5))
                  .width(toPx(17.5))
                  .opacity(1)
                  .clip(true)
                  .position({ left: toPx(3.25), top: toPy(10.25) })
                }
                .id('50:11')
                .height(toPy(24))
                .width(toPx(24))
                .opacity(1)
                .position({ left: toPx(60), top: toPy(10) })
              }
              .id('50:7')
              .height(toPy(44))
              .width(toPx(100))
              .opacity(1)
              .position({ left: toPx(275), top: toPy(44) })
              Stack() {
                Column() {
                  Text() {
                    Span(`主页`)
                    .fontColor('rgba(0, 0, 0, 1)')
                    .fontFamily('Noto Sans SC')
                    .fontWeight('Medium')
                    .fontSize(toPx(17))
                  }
                  .width('100%')
                  .margin({ left: toPx(0), bottom: toPy(0), right: toPx(0), top: toPy(0) })
                }
                .height(toPy(25))
                .width(toPx(34))
                .opacity(1)
                .position({ left: toPx(43), top: toPy(10) })
              }
              .id('50:17')
              .height(toPy(44))
              .width(toPx(120))
              .opacity(1)
              .position({ left: toPx(128), top: toPy(44) })
              Stack() {
                Stack() {
                  Image($r('app.media.Group_50_24'))
                  .height(toPy(17))
                  .width(toPx(9.01))
                  .opacity(1)
                  .clip(true)
                  .position({ left: toPx(1), top: toPy(3.5) })
                }
                .id('50:23')
                .height(toPy(24))
                .width(toPx(12))
                .opacity(1)
                .position({ left: toPx(16), top: toPy(10) })
              }
              .id('50:19')
              .height(toPy(44))
              .width(toPx(120))
              .opacity(1)
              .position({ left: toPx(0), top: toPy(44) })
              Stack() {
                Stack() {
                  Row()
                  .id('50:28')
                  .height(toPy(11.33))
                  .width(toPx(22))
                  .opacity(0.3499999940395355)
                  .position({ left: toPx(0), top: toPy(0) })
                  .borderRadius(toPx(2.6666667461395264))
                  .borderWidth({ left: toPx(1), right: toPx(1), top: toPy(1), bottom: toPy(1) })
                  .borderColor('rgba(0, 0, 0, 1)')
                  Image($r('app.media.Vector_50_29'))
                  .height(toPy(4))
                  .width(toPx(1.33))
                  .opacity(0.4000000059604645)
                  .position({ left: toPx(23), top: toPy(3.67) })
                  Row()
                  .id('50:30')
                  .height(toPy(7.33))
                  .width(toPx(18))
                  .opacity(1)
                  .position({ left: toPx(2), top: toPy(2) })
                  .borderRadius(toPx(1.3333333730697632))
                  .backgroundColor('rgba(0, 0, 0, 1)')
                }
                .height(toPy(11.33))
                .width(toPx(24.33))
                .opacity(1)
                .clip(true)
                .position({ left: toPx(336.33), top: toPy(17.33) })
                Image($r('app.media.Boolean_operation_50_31'))
                .height(toPy(11))
                .width(toPx(15.33))
                .opacity(1)
                .position({ left: toPx(316), top: toPy(17.33) })
                Image($r('app.media.Boolean_operation_50_35'))
                .height(toPy(10.67))
                .width(toPx(17))
                .opacity(1)
                .position({ left: toPx(294), top: toPy(17.67) })
                Stack() {
                  Column() {
                    Text() {
                      Span(`9:41`)
                      .fontColor('rgba(0, 0, 0, 1)')
                      .fontFamily('SF Pro Text')
                      .fontWeight('SemiBold')
                      .fontSize(toPx(15))
                    }
                    .width('100%')
                    .margin({ left: toPx(0), bottom: toPy(0), right: toPx(0), top: toPy(0) })
                  }
                  .height(toPy(18))
                  .width(toPx(53.7))
                  .opacity(1)
                  .position({ left: toPx(0), top: toPy(0) })
                }
                .height(toPy(18))
                .width(toPx(54))
                .opacity(1)
                .clip(true)
                .position({ left: toPx(21), top: toPy(14.33) })
              }
              .id('50:26')
              .height(toPy(44))
              .width(toPx(375))
              .opacity(1)
              .position({ left: toPx(0), top: toPy(0) })
            }
            .id('50:2')
            .height(toPy(88))
            .width(toPx(375))
            .opacity(1)
            .position({ left: toPx(0), top: toPy(0) })
          }
          .id('50:1')
          .height(toPy(88))
          .width(toPx(375))
          .opacity(1)
          .position({ left: toPx(0), top: toPy(0) })
          .backgroundColor('rgba(255, 255, 255, 1)')
        }
        .height(toPy(100))
        .width(toPx(375))
        .opacity(1)
        .clip(true)
        .position({ left: toPx(0), top: toPy(0) })
        Stack() {
          Row()
          .id('1:209')
          .height(toPy(109))
          .width(toPx(375))
          .opacity(1)
          .position({ left: toPx(0), top: toPy(0) })
          .backgroundColor('rgba(255, 255, 255, 0)')
          Row()
          .id('1:211')
          .height(toPy(109))
          .width(toPx(375))
          .opacity(1)
          .position({ left: toPx(0), top: toPy(0) })
          .shadow({ radius: 22, offsetX: 0, offsetY: -8, fill: false, color: 'rgba(39, 24, 71, 0)' })
          .backgroundColor('rgba(255, 255, 255, 1)')
          Stack() {
            Stack() {
              Column() {
                Text() {
                  Span(`首页`)
                  .fontColor('rgba(116, 202, 0, 1)')
                  .fontFamily('Noto Sans SC')
                  .fontWeight('Medium')
                  .fontSize(toPx(10))
                }
                .width('100%')
                .margin({ left: toPx(0), bottom: toPy(0), right: toPx(0), top: toPy(0) })
              }
              .height(toPy(16))
              .width(toPx(32))
              .opacity(1)
              .position({ left: toPx(0), top: toPy(0) })
            }
            .height(toPy(16))
            .width(toPx(32))
            .opacity(1)
            .clip(true)
            .position({ left: toPx(0), top: toPy(0) })
            Stack() {
              Column() {
                Text() {
                  Span(`模库`)
                  .fontColor('rgba(152, 154, 172, 1)')
                  .fontFamily('Noto Sans SC')
                  .fontWeight('Medium')
                  .fontSize(toPx(10))
                }
                .width('100%')
                .margin({ left: toPx(0), bottom: toPy(0), right: toPx(0), top: toPy(0) })
              }
              .height(toPy(16))
              .width(toPx(20))
              .opacity(1)
              .position({ left: toPx(0), top: toPy(0) })
            }
            .height(toPy(16))
            .width(toPx(20))
            .opacity(1)
            .clip(true)
            .position({ left: toPx(70), top: toPy(0) })
            Stack() {
              Column() {
                Text() {
                  Span(`任务`)
                  .fontColor('rgba(152, 154, 172, 1)')
                  .fontFamily('Noto Sans SC')
                  .fontWeight('Medium')
                  .fontSize(toPx(10))
                }
                .width('100%')
                .margin({ left: toPx(0), bottom: toPy(0), right: toPx(0), top: toPy(0) })
              }
              .height(toPy(16))
              .width(toPx(20))
              .opacity(1)
              .position({ left: toPx(0), top: toPy(0) })
            }
            .height(toPy(16))
            .width(toPx(20))
            .opacity(1)
            .clip(true)
            .position({ left: toPx(230.5), top: toPy(0) })
            Stack() {
              Column() {
                Text() {
                  Span(`我的`)
                  .fontColor('rgba(152, 154, 172, 1)')
                  .fontFamily('Noto Sans SC')
                  .fontWeight('Medium')
                  .fontSize(toPx(10))
                }
                .width('100%')
                .margin({ left: toPx(0), bottom: toPy(0), right: toPx(0), top: toPy(0) })
              }
              .height(toPy(16))
              .width(toPx(20))
              .opacity(1)
              .position({ left: toPx(0), top: toPy(0) })
            }
            .height(toPy(16))
            .width(toPx(20))
            .opacity(1)
            .clip(true)
            .position({ left: toPx(295), top: toPy(0) })
          }
          .height(toPy(16))
          .width(toPx(315))
          .opacity(1)
          .clip(true)
          .position({ left: toPx(28), top: toPy(47) })
          Stack() {
            Image($r('app.media.Vector_1_225'))
            .height(toPy(76))
            .width(toPx(108))
            .opacity(1)
            .position({ left: toPx(0), top: toPy(0) })
            Stack() {
              Row()
              .id('1:227')
              .height(toPy(28))
              .width(toPx(28))
              .opacity(1)
              .position({ left: toPx(0), top: toPy(0) })
              .backgroundColor('rgba(255, 255, 255, 0)')
              Image($r('app.media.Boolean_operation_1_228'))
              .height(toPy(19.64))
              .width(toPx(19.64))
              .opacity(1)
              .position({ left: toPx(4), top: toPy(4) })
            }
            .height(toPy(28))
            .width(toPx(28))
            .opacity(1)
            .clip(true)
            .position({ left: toPx(23), top: toPy(6) })
          }
          .height(toPy(40))
          .width(toPx(72))
          .opacity(1)
          .clip(true)
          .position({ left: toPx(152), top: toPy(23) })
          Stack() {
            Row()
            .id('1:238')
            .height(toPy(24))
            .width(toPx(24))
            .opacity(1)
            .position({ left: toPx(0), top: toPy(0) })
            .backgroundColor('rgba(255, 255, 255, 0)')
            Stack() {
              Image($r('app.media.Boolean_operation_1_241'))
              .height(toPy(19.53))
              .width(toPx(18.14))
              .opacity(1)
              .position({ left: toPx(3.23), top: toPy(2.82) })
              Image($r('app.media.Vector_1_245'))
              .height(toPy(2.11))
              .width(toPx(6.65))
              .opacity(1)
              .position({ left: toPx(9.03), top: toPy(1.69) })
            }
            .height(toPy(24))
            .width(toPx(24))
            .opacity(1)
            .clip(true)
            .position({ left: toPx(0), top: toPy(0) })
          }
          .height(toPy(24))
          .width(toPx(24))
          .opacity(1)
          .clip(true)
          .position({ left: toPx(96), top: toPy(23) })
          Row()
          .id('1:246')
          .height(toPy(2))
          .width(toPx(375))
          .opacity(1)
          .position({ left: toPx(0), top: toPy(0) })
          .backgroundColor('rgba(244, 245, 249, 1)')
          Stack() {
            Image($r('app.media.Boolean_operation_1_249'))
            .height(toPy(18.97))
            .width(toPx(20))
            .opacity(1)
            .position({ left: toPx(2), top: toPy(2.51) })
          }
          .height(toPy(24))
          .width(toPx(24))
          .opacity(1)
          .clip(true)
          .position({ left: toPx(32), top: toPy(23) })
          Stack() {
            Image($r('app.media.Boolean_operation_1_255'))
            .height(toPy(19.2))
            .width(toPx(18))
            .opacity(1)
            .position({ left: toPx(3), top: toPy(2) })
          }
          .height(toPy(24))
          .width(toPx(24))
          .opacity(1)
          .clip(true)
          .position({ left: toPx(256), top: toPy(23) })
          Stack() {
            Image($r('app.media.Vector_1_260'))
            .height(toPy(24))
            .width(toPx(24))
            .opacity(1)
            .position({ left: toPx(0), top: toPy(0) })
            Image($r('app.media.Boolean_operation_1_261'))
            .height(toPy(18.86))
            .width(toPx(18.86))
            .opacity(1)
            .position({ left: toPx(2.57), top: toPy(2.57) })
          }
          .height(toPy(24))
          .width(toPx(24))
          .opacity(1)
          .clip(true)
          .position({ left: toPx(321), top: toPy(23) })
        }
        .height(toPy(109))
        .width(toPx(375))
        .opacity(1)
        .clip(true)
        .position({ left: toPx(0), top: toPy(703) })
        Component_54_2({
            propList: [{"componentId":"53:21","pathString":"53:21","nodeText":"可用库数量"},{"componentId":"53:22","pathString":"53:22","nodeText":"456"},{"componentId":"54:2","pathString":"54:2","width":toPx(166),"height":toPy(69),"left":toPx(11),"top":toPy(366),"name":"实例 2"}]
          })
        Component_54_2({
            propList: [{"componentId":"53:21","pathString":"53:21","nodeText":"裸模库数量"},{"componentId":"53:22","pathString":"53:22","nodeText":"123"},{"componentId":"54:2","pathString":"54:2","width":toPx(166),"height":toPy(69),"left":toPx(198),"top":toPy(287),"name":"实例 1"}]
          })
        Component_54_2({
            propList: [{"componentId":"53:21","pathString":"53:21","nodeText":"封层库数量"},{"componentId":"53:22","pathString":"53:22","nodeText":"89"},{"componentId":"54:2","pathString":"54:2","width":toPx(166),"height":toPy(69),"left":toPx(198),"top":toPy(366),"name":"实例 3"}]
          })
        Stack() {
          Stack() {
            Column() {
              Text() {
                Span(`全局智能搜索`)
                .fontColor('rgba(128, 128, 128, 1)')
                .fontFamily('Noto Sans SC')
                .fontWeight('Regular')
                .fontSize(toPx(16))
              }
              .width('100%')
              .margin({ left: toPx(0), bottom: toPy(0), right: toPx(0), top: toPy(0) })
            }
            .height(toPy(24))
            .width(toPx(96))
            .opacity(1)
            .position({ left: toPx(35), top: toPy(0) })
            Stack() {
              Image($r('app.media.Frame_54_102'))
              .height(toPy(24))
              .width(toPx(24))
              .opacity(1)
              .clip(true)
              .position({ left: toPx(0), top: toPy(0) })
            }
            .height(toPy(24))
            .width(toPx(24))
            .opacity(0.5)
            .clip(true)
            .position({ left: toPx(0), top: toPy(0) })
          }
          .height(toPy(24))
          .width(toPx(131))
          .opacity(1)
          .clip(true)
          .position({ left: toPx(12), top: toPy(8) })
        }
        .id('54:98')
        .height(toPy(40))
        .width(toPx(343))
        .opacity(1)
        .position({ left: toPx(18), top: toPy(98) })
        .borderRadius(toPx(8))
        .borderWidth({ left: toPx(1), right: toPx(1), top: toPy(1), bottom: toPy(1) })
        .borderColor('rgba(213, 213, 213, 1)')
        .backgroundColor('rgba(245, 245, 245, 1)')
        Flex({ direction: FlexDirection.Row, justifyContent: FlexAlign.Center, alignItems: ItemAlign.Center, space: {  } }) {
          Flex({ direction: FlexDirection.Column, justifyContent: FlexAlign.End, alignItems: ItemAlign.Center, space: { main: LengthMetrics.vp(toPx(9)) } }) {
            Column() {
              Text() {
                Span(`加工管理`)
                .fontColor('rgba(38, 120, 255, 1)')
                .fontFamily('Noto Sans SC')
                .fontWeight('Medium')
                .fontSize(toPx(14))
              }
              .width('100%')
              .margin({ left: toPx(0), bottom: toPy(0), right: toPx(0), top: toPy(0) })
            }
            .height('auto')
            .width(toPx(75))
            .opacity(1)
            .flexShrink(0)
            Row()
            .id('54:343')
            .height(toPy(2))
            .width(toPx(42))
            .opacity(1)
            .flexShrink(0)
            .backgroundColor('rgba(38, 120, 255, 1)')
          }
          .id('54:341')
          .height(toPy(44))
          .width(toPx(93))
          .opacity(1)
          .flexShrink(0)
          .backgroundColor('rgba(255, 255, 255, 1)')
          Stack() {
            Column() {
              Text() {
                Span(`调模管理`)
                .fontColor('rgba(128, 128, 128, 1)')
                .fontFamily('Noto Sans SC')
                .fontWeight('Regular')
                .fontSize(toPx(14))
              }
              .width('100%')
              .margin({ left: toPx(0), bottom: toPy(0), right: toPx(0), top: toPy(0) })
            }
            .height(toPy(35))
            .width(toPx(94))
            .opacity(1)
            .position({ left: toPx(0), top: toPy(8.5) })
          }
          .id('54:344')
          .height(toPy(44))
          .width(toPx(94))
          .opacity(1)
          .flexShrink(0)
          .backgroundColor('rgba(255, 255, 255, 1)')
          Stack() {
            Column() {
              Text() {
                Span(`生产管理`)
                .fontColor('rgba(128, 128, 128, 1)')
                .fontFamily('Noto Sans SC')
                .fontWeight('Regular')
                .fontSize(toPx(14))
              }
              .width('100%')
              .margin({ left: toPx(0), bottom: toPy(0), right: toPx(0), top: toPy(0) })
            }
            .height(toPy(35))
            .width(toPx(94))
            .opacity(1)
            .position({ left: toPx(0), top: toPy(8.5) })
          }
          .id('54:346')
          .height(toPy(44))
          .width(toPx(94))
          .opacity(1)
          .flexShrink(0)
          .backgroundColor('rgba(255, 255, 255, 1)')
          Stack() {
            Column() {
              Text() {
                Span(`设备管理`)
                .fontColor('rgba(128, 128, 128, 1)')
                .fontFamily('Noto Sans SC')
                .fontWeight('Regular')
                .fontSize(toPx(14))
              }
              .width('100%')
              .margin({ left: toPx(0), bottom: toPy(0), right: toPx(0), top: toPy(0) })
            }
            .height(toPy(35))
            .width(toPx(94))
            .opacity(1)
            .position({ left: toPx(0), top: toPy(8.5) })
          }
          .id('54:348')
          .height(toPy(44))
          .width(toPx(94))
          .opacity(1)
          .flexShrink(0)
          .backgroundColor('rgba(255, 255, 255, 1)')
          Stack() {
            Column() {
              Text() {
                Span(`耗材管理`)
                .fontColor('rgba(128, 128, 128, 1)')
                .fontFamily('Noto Sans SC')
                .fontWeight('Regular')
                .fontSize(toPx(14))
              }
              .width('100%')
              .margin({ left: toPx(0), bottom: toPy(0), right: toPx(0), top: toPy(0) })
            }
            .height(toPy(35))
            .width(toPx(94))
            .opacity(1)
            .position({ left: toPx(0), top: toPy(8.5) })
          }
          .id('54:350')
          .height(toPy(44))
          .width(toPx(94))
          .opacity(1)
          .flexShrink(0)
          .backgroundColor('rgba(255, 255, 255, 1)')
        }
        .id('54:340')
        .height(toPy(44))
        .width(toPx(375))
        .opacity(1)
        .position({ left: toPx(0), top: toPy(145) })
        .borderWidth({ left: toPx(1), right: toPx(1), top: toPy(1), bottom: toPy(1) })
        .borderColor('rgba(0, 0, 0, 1)')
        .backgroundColor('rgba(255, 255, 255, 1)')
        Stack() {
          Row()
          .id('56:10')
          .height(toPy(180))
          .width(toPx(331))
          .opacity(1)
          .position({ left: toPx(0), top: toPy(0) })
          .backgroundImageSize(ImageSize.FILL)
          .backgroundImage($r('app.media.567b4bc51f0c608ff662621fe375470693cbf82b'))
        }
        .id('55:279')
        .height(toPy(180))
        .width(toPx(331))
        .opacity(1)
        .clip(true)
        .position({ left: toPx(21), top: toPy(466) })
        .borderWidth({ left: toPx(1), right: toPx(1), top: toPy(1), bottom: toPy(1) })
        .borderColor('rgba(0, 0, 0, 1)')
        Column() {
          Text() {
            Span(`调模样品图片`)
            .fontColor('rgba(0, 0, 0, 0.5)')
            .fontFamily('Noto Sans SC')
            .fontWeight('Regular')
            .fontSize(toPx(18))
          }
          .width('100%')
          .margin({ left: toPx(0), bottom: toPy(0), right: toPx(0), top: toPy(0) })
        }
        .height(toPy(18))
        .width(toPx(120))
        .opacity(1)
        .position({ left: toPx(11), top: toPy(448) })
      }
      .id('1:3')
      .height(toPy(812))
      .width(toPx(375))
      .opacity(1)
      .clip(true)
      .backgroundColor('rgba(255, 255, 255, 1)')
    }
  }
  
}