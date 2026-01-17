import { screenAdapter, toPx, toPy } from "../utils/screenAdapter"
screenAdapter.designWidth = 375;

@Entry
@Component
export struct Frame_1_1484 {
  private scrollController = new Scroller()
  build() {
    Scroll(this.scrollController) {
      Stack() {
        Image($r('app.media.Vector_1_1485'))
        .height(toPy(1))
        .width(toPx(322))
        .opacity(1)
        .position({ left: toPx(27), top: toPy(664) })
        Stack() {
          Row()
          .id('1:1503')
          .height(toPy(56))
          .width(toPx(311))
          .opacity(1)
          .position({ left: toPx(0), top: toPy(0) })
          .borderRadius(toPx(14))
          .shadow({ radius: 18, offsetX: 0, offsetY: 6, fill: false, color: 'rgba(116, 202, 0, 0.30196078431372547)' })
          .backgroundColor('rgba(116, 202, 0, 1)')
          Stack() {
            Column() {
              Text() {
                Span(`登录`)
                .fontColor('rgba(255, 255, 255, 1)')
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
            .position({ left: toPx(32), top: toPy(0) })
            Stack() {
              Image($r('app.media.Boolean_operation_1_1508'))
              .height(toPy(20.97))
              .width(toPx(21))
              .opacity(1)
              .position({ left: toPx(1.5), top: toPy(1.54) })
            }
            .height(toPy(24))
            .width(toPx(24))
            .opacity(1)
            .clip(true)
            .position({ left: toPx(0), top: toPy(0) })
          }
          .height(toPy(24))
          .width(toPx(65))
          .opacity(1)
          .clip(true)
          .position({ left: toPx(121), top: toPy(17) })
        }
        .height(toPy(56))
        .width(toPx(311))
        .opacity(1)
        .clip(true)
        .position({ left: toPx(33), top: toPy(546) })
        .shadow({ radius: 18, offsetX: 0, offsetY: 6, fill: false, color: 'rgba(29, 55, 235, 0.2980392156862745)' })
        Stack() {
          Row()
          .id('1:1515')
          .height(toPy(76))
          .width(toPx(311))
          .opacity(1)
          .position({ left: toPx(0), top: toPy(5) })
          .backgroundColor('rgba(255, 255, 255, 0)')
          Image($r('app.media.Vector_1_1516'))
          .height(toPy(48))
          .width(toPx(311))
          .opacity(1)
          .clip(true)
          .position({ left: toPx(0), top: toPy(33) })
          Column() {
            Text() {
              Span(`账号`)
              .fontColor('rgba(35, 34, 39, 1)')
              .fontFamily('Noto Sans SC')
              .fontWeight('Bold')
              .fontSize(toPx(14))
            }
            .width('100%')
            .margin({ left: toPx(0), bottom: toPy(0), right: toPx(0), top: toPy(0) })
          }
          .height(toPy(20))
          .width(toPx(311))
          .opacity(1)
          .position({ left: toPx(0), top: toPy(0) })
        }
        .height(toPy(81))
        .width(toPx(311))
        .opacity(1)
        .clip(true)
        .position({ left: toPx(32), top: toPy(315) })
        Stack() {
          Row()
          .id('1:1530')
          .height(toPy(76))
          .width(toPx(311))
          .opacity(1)
          .position({ left: toPx(0), top: toPy(5) })
          .backgroundColor('rgba(255, 255, 255, 0)')
          Image($r('app.media.Vector_1_1531'))
          .height(toPy(48))
          .width(toPx(311))
          .opacity(1)
          .clip(true)
          .position({ left: toPx(0), top: toPy(33) })
          Column() {
            Text() {
              Span(`密码`)
              .fontColor('rgba(35, 34, 39, 1)')
              .fontFamily('Noto Sans SC')
              .fontWeight('Bold')
              .fontSize(toPx(14))
            }
            .width('100%')
            .margin({ left: toPx(0), bottom: toPy(0), right: toPx(0), top: toPy(0) })
          }
          .height(toPy(20))
          .width(toPx(311))
          .opacity(1)
          .position({ left: toPx(0), top: toPy(0) })
        }
        .height(toPy(81))
        .width(toPx(311))
        .opacity(1)
        .clip(true)
        .position({ left: toPx(32), top: toPy(405) })
        Column() {
          Text() {
            Span(`第三方登录`)
            .fontColor('rgba(152, 154, 172, 1)')
            .fontFamily('Noto Sans SC')
            .fontWeight('Regular')
            .fontSize(toPx(14))
          }
          .width('100%')
          .margin({ left: toPx(0), bottom: toPy(0), right: toPx(0), top: toPy(0) })
        }
        .height(toPy(20))
        .width(toPx(304))
        .opacity(1)
        .position({ left: toPx(35), top: toPy(728) })
        Column() {
          Text() {
            Span(`安天模具管理系统`)
            .fontColor('rgba(35, 34, 39, 1)')
            .fontFamily('Noto Sans SC')
            .fontWeight('Bold')
            .fontSize(toPx(20))
          }
          .width('100%')
          .margin({ left: toPx(0), bottom: toPy(0), right: toPx(0), top: toPy(0) })
        }
        .height(toPy(28))
        .width(toPx(160))
        .opacity(1)
        .position({ left: toPx(103), top: toPy(254) })
        Column() {
          Text() {
            Span(`让今天更有效率 `)
            .fontColor('rgba(152, 154, 172, 1)')
            .fontFamily('Noto Sans SC')
            .fontWeight('Medium')
            .fontSize(toPx(16))
          }
          .width('100%')
          .margin({ left: toPx(0), bottom: toPy(0), right: toPx(0), top: toPy(0) })
        }
        .height(toPy(24))
        .width(toPx(306))
        .opacity(1)
        .position({ left: toPx(36), top: toPy(282) })
        Stack() {
          Image($r('app.media.Boolean_operation_1_1567'))
          .height(toPy(28.11))
          .width(toPx(28.11))
          .opacity(1)
          .position({ left: toPx(1.95), top: toPy(1.95) })
        }
        .height(toPy(32))
        .width(toPx(32))
        .opacity(1)
        .clip(true)
        .position({ left: toPx(171), top: toPy(688) })
        Row()
        .id('48:1')
        .height(toPy(73))
        .width(toPx(73))
        .opacity(1)
        .position({ left: toPx(146), top: toPy(135) })
        .backgroundImageSize(ImageSize.FILL)
        .backgroundImage($r('app.media.f1044208e2bf07575c43c2aa3959367cff05ca00'))
        Stack() {
          Stack() {
            Image($r('app.media.Frame_48_95'))
            .height(toPy(88))
            .width(toPx(375))
            .opacity(1)
            .position({ left: toPx(0), top: toPy(0) })
            Stack() {
              Row()
              .id('48:98')
              .height(toPy(0.5))
              .width(toPx(375))
              .opacity(1)
              .position({ left: toPx(0), top: toPy(0.5) })
              .backgroundColor('rgba(0, 0, 0, 0.09803921729326248)')
            }
            .id('48:97')
            .height(toPy(1))
            .width(toPx(375))
            .opacity(1)
            .position({ left: toPx(0), top: toPy(87) })
            Stack() {
              Stack() {
                Image($r('app.media.Group_48_135'))
                .height(toPy(3.5))
                .width(toPx(17.5))
                .opacity(1)
                .clip(true)
                .position({ left: toPx(3.25), top: toPy(10.25) })
              }
              .id('48:134')
              .height(toPy(24))
              .width(toPx(24))
              .opacity(1)
              .position({ left: toPx(60), top: toPy(10) })
            }
            .id('48:99')
            .height(toPy(44))
            .width(toPx(100))
            .opacity(1)
            .position({ left: toPx(275), top: toPy(44) })
            Stack() {
              Column() {
                Text() {
                  Span(`标题`)
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
            .id('48:109')
            .height(toPy(44))
            .width(toPx(120))
            .opacity(1)
            .position({ left: toPx(128), top: toPy(44) })
            Stack() {
              Stack() {
                Image($r('app.media.Group_48_141'))
                .height(toPy(17))
                .width(toPx(9.01))
                .opacity(1)
                .clip(true)
                .position({ left: toPx(1), top: toPy(3.5) })
              }
              .id('48:140')
              .height(toPy(24))
              .width(toPx(12))
              .opacity(1)
              .position({ left: toPx(16), top: toPy(10) })
            }
            .id('48:111')
            .height(toPy(44))
            .width(toPx(120))
            .opacity(1)
            .position({ left: toPx(0), top: toPy(44) })
            Stack() {
              Stack() {
                Row()
                .id('48:120')
                .height(toPy(11.33))
                .width(toPx(22))
                .opacity(0.3499999940395355)
                .position({ left: toPx(0), top: toPy(0) })
                .borderRadius(toPx(2.6666667461395264))
                .borderWidth({ left: toPx(1), right: toPx(1), top: toPy(1), bottom: toPy(1) })
                .borderColor('rgba(0, 0, 0, 1)')
                Image($r('app.media.Vector_48_121'))
                .height(toPy(4))
                .width(toPx(1.33))
                .opacity(0.4000000059604645)
                .position({ left: toPx(23), top: toPy(3.67) })
                Row()
                .id('48:122')
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
              Image($r('app.media.Boolean_operation_48_123'))
              .height(toPy(11))
              .width(toPx(15.33))
              .opacity(1)
              .position({ left: toPx(316), top: toPy(17.33) })
              Image($r('app.media.Boolean_operation_48_127'))
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
            .id('48:118')
            .height(toPy(44))
            .width(toPx(375))
            .opacity(1)
            .position({ left: toPx(0), top: toPy(0) })
          }
          .id('48:94')
          .height(toPy(88))
          .width(toPx(375))
          .opacity(1)
          .position({ left: toPx(0), top: toPy(0) })
        }
        .id('48:93')
        .height(toPy(88))
        .width(toPx(375))
        .opacity(1)
        .position({ left: toPx(0), top: toPy(0) })
        .backgroundColor('rgba(255, 255, 255, 1)')
      }
      .id('1:1484')
      .height(toPy(812))
      .width(toPx(375))
      .opacity(1)
      .clip(true)
      .backgroundColor('rgba(255, 255, 255, 1)')
    }
  }
  
}