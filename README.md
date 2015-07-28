#led를 이용한 푸시알림 기능


안드로이드를 이용하여 개발한다.

#개발목적
 생활하면서 문자메세지, 카카오톡, 전화 등 캐치를 못하고 그냥 지나치는 경우가 많다.

필자의 경우 꽤나 심하다 이를 해결 하기 위한 방법으로 보려고 의식 하지않아도 자연스럽게 인식할 수 있는것을 고민하던 중

led전구의 빛은 인식하지 않아도 눈에 보이기 때문에 이를 이용하여 각종 푸시메세지를 led로 표현하면 되겠다고 생각하였다.

#순서

필립스 휴의 스마트 led의 경우 다양한 색상의 빛을 낼 수 있고 hue library를 통해 직접 개발할 수 있다.


1.안드로이드 푸시메세지 분석

2.푸시메세지 활용

3. 브릿지 연결 구현

4. led제어 구현

5. 푸시메세지를 통해 led 제어

6. 어플 보완


진행상황


브릿지를 연결하고 led를 제어 하는데 까지는 성공하였다.



#문제

안드로이드 개발능력이 미숙하다.

핸드폰의 전화,문자,푸시메세지 등의 알람들이 어떤 신호로 오는지 분석이 안되어 있다.

현재 led제어를 성공은 하였으나

hue전구의 경우

hue 0~65535, saturation 0~254, blightness 0~254 로 제어

hue: green: 25500, blue: 46920

기본 HSB의 경우

hue 0~360도(각도이다.) saturation 0~100%, blightness 0~100%로 제어

hue: green: 120도, blue: 240도

![HSB !](3219315_1279195852.jpg)

RGB 

RED 0~255, GREEN 0~255, BLUE 0~255

philips hue의 HSB변환 방식이 어떻게 이루어 지는지 분석이 필요하다.

7월 28일 문제점

어플 종료시 설정값들이 모두 초기화 설정값들을 계속 사용하기 위해 라즈베리파이를 서버로 이용할지 어플 자체 기능으로 처리할지 

효율적인 방식을 채택 해야한다. 

led에 목록을 어떻게 띄울것인가 또한 각각의 led에 대해서 어떻게 제어 할 것인가.







