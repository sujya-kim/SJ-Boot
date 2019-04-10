# Soojin-SpringBoot

## 개발환경 및 사용 라이브러리
* Server : Java8, Spring Boot 1.5.10, Spring Security
* DB : H2Base
* 추가 사용 Library : JPA, Lombok, Junit, jjwt

-----

## Install 및 실행 가이드
### Spring Boot Project 실행
* 폴더구조<br/>
```bash
SJ-Boot
├── dist
│   └── sujya-0.0.1-SNAPSHOT.jar
├── assets
├── assetsTest
├── src
│   │── main
│   └── test
│       │── PrjApplicationTests.java
│       └── PrjControllerTest.java
└── README.md
```
1. IDE에 lombok plugin을 설치한다.<br/>
(IntelliJ의 경우 Settings > Build,Execution,Deployment > Compiler > Annotation Processors의 Enable annotation processing을 체크한다.)
2. Gradle project로 SJ-Boot 폴더를 import한다.
3. Gradle build를 한다.
4. Spring Boot Application(PrjApplication)을 실행시켜 Server를 구동한다.
5. http://localhost:8080 으로의 접속을 확인한다.

### JAR 실행
* 아래의 방법으로 실행 시 Spring Boot 서버가 실행되며, localhost:8080으로 접속하여 확인 가능하다.
<br/>``` > java -jar sujya-0.0.1-SNAPSHOT.jar ```

### H2Base console 접속 방법
* 접속 url : <http://localhost:8080/h2-console>
* Login 정보<br/>
<img src="/images/h2-console.PNG" width="500" height="500">

-----

### Test 방법
* gradle test를 이용하여 Junit test 진행
1. PrjApplicationTests
: Login 후 token발행 및 Refresh token 발행 테스트
2. PrjControllerTest
: Rest API 테스트
