## **Synopsis**

---

Android Studio를 사용해 개발하였습니다.

### Quick Start

```bash
git clone https://github.com/openInfra-project/AutoWatch-Android-Client.git

Android Studio에서 열기
 
앱 다운받을 스마트폰(samsung) 설정-개발자 옵션에서 USB 디버깅 허용
컴퓨터에 스마트폰을 USB로 연결

안드로이드 스튜디오 상단 스마트폰 연결 확인 후 실행
```

## 🎨 Preview

---

### 🛠 **Development Stack**

- Kotlin
- Java
- Retrofit
- NDK
- OpenCV
- SQLite

### 🖋Features

1. 회원 가입 및 로그인/ 로그아웃
    - 로그인 시 SQLite에 사용자 저장
    - 중복 회원 가입 방지
2.  사용자 My Page 
    - 사용자 이미지 변경 가능
    - 기본 default 이미지 제공
3. Room을 EXAM/STUDY 2가지 모드로 생성 및 입장
    - AutoWatch 접근성 미허용시 다음 단계로 넘어갈 수 없음
    - room 생성시 생성한 room 정보 확인 및 바로 입장 가능
    - **EXAM모드**
        1. EXAM 모드 생성 시, 시험 명단 누락 방지
        2. 수험번호/학번, 이름 일치여부 확인 후 얼굴 인식
        3. 특정 앱 차단
    - **STUDY모드**
        1. 특정 앱 차단 후 앱 접근 횟수 count
        2. 5초마다 사람 인지 확인 후 자리비움 count
4. MY ROOM에서 자신이 생성한 모든 방 리스트 (Study mode방 클릭시 바로 입장 가능)
5. 회원 삭제

### 🙋‍♂️Role

@김유림 

     전체 Layout 구축

SQLite 로그인한 사용자 저장

study, exam모드 별로 방 생성 (exam모드 생성 시 파일[회원명단] 업로드 필수)

retrofit 사용하여 서버와 통신

exam 방 입장시 카메라 촬영 및 이미지 통신

안드로이드 NDK + OpenCV를 활용한 사람 인지 기능

     특정 앱 차단 구현

 

@김준영 

회원가입  구현

로그인  구현

이미지 통신 구현
### Lisense
```
MIT License

Copyright (혜몽유식) [2021-08-15] [YuLim Kim]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
