# Get Your Hands Dirty on Clean Architecture 정리

Get Your Hands Dirty on Clean Architecture 책을 읽고 정리한 내용입니다.

---
서비스와 모델(Dto)의 관계

- 서비스 생성 할 때마다, 인풋, 아웃풋 모델(Dto)를 생성한다
- 이는 다른 UseCase(서비스)에 재사용하여 결합도를 높이는 것을 방지해준다
- 결합도를 낮춤으로서 장기적으로 사이드 이펙트를 줄일 수 있다
- Query와 Command를 나눔으로서 CQRS CQS의 개념을 사용할 수도 있게된다
- 또한 구체적인 모델로 어떤 UseCase(서비스)인지 가늠할 수 있다
- 또한 여러 개발자가 병렬적으로 작업을 할 수 있다

Validation

- 선택 유무는 **도메인 객체의 접근 유무**
1. Input Validation
    - Input Dto에서 작업
    - 선언적
2. Business Validation
    - 도메인 엔티티에서 작업
3. Use Case
    - 잘모르겠으면 Use Case에서 작업

Domain model

1. Rich
    - 모든 도메인 로직이 엔티티에
    - 상태변경을 위한 메소드 제공
2. Anemic
    - 모든 도메인 로직이 서비스에

무엇을 반환할 것인가

- 최소한만 반환한다
    - caller가 정말 원하는 것만 반환
    - 잘 모르겠으면 최소한반 반환한다
    - 왜냐면 메소드는 하나의 역할만 해야하기 때문
    - 예) 계좌 생성 이후, 생성된 계좌 정보를 보내야 하는가?
        - 생성 유무만 표현하면 된다
        - 계좌 정보를 알려면 계좌 조회를 호출하라고 하면된다
        - 계좌 생성은 계좌를 생성한다는 하나의 역할에만 집중하도록 한다
- 인풋, 아웃풋 도메인
    - 인풋과 아웃풋 dto에 도메인 있는 것은 좋지 않다
    - 서비스 레이에 의존성이 더해지기 때무???
    - 아 상관없네??
    - 하지만 좋지않다
    - 사용할 수도 있다... 다음 챕터에서 계속

Read Only 서비스는?

- 서비스 하나 생성 해야지
- in port 생성
- out port  생성
- 이렇게 하면 조회(Query) 요청과 수정(Command) 요청을 분리할 수 있다
    - 자연스럽게 CQS(Command Query Separation)과 CQRS(Command Query Responsibility Segregation)의 컨셉을 사용할 수 있다
    -

### WebAdaptor

- 모든 외부와의 소통은 Adaptor를 통해 이뤄진다
- HTTP 웹 인터페이스 등...

왜 port라는 레이어를 더 두는가?

1. Dependency Inversion
2. 어떤 adaptor와 소통하는지 알 수 있다
    - 이는 레거시 코드를 유지하는데 매우 가치있는 정보가 될 것이다

WebAdaptor는 뭐하는 녀석인가

1. HTTP 요청과 자바객체를 매핑
2. 권한 체크
3. input 요청 검증
    - 서비스 레이어에서 검증하는데?
    - 서비스로 가는 구조나 정책이 바뀔 수도 있기 때문에 input을 검증한다
    - 하지만 같은 validation을 할 필요는 없다
    - **adaptor의 input과 usecase의 모델로 변환 가능한지 검증!**
4. input 요청과 input 모델 매핑
5. UseCase(서비스) 호출
6. 서비스의 output을 HTTP로 전송
7. HTTP 응답 반환
    - 모든 에러를 HTTP 응답으로 변환해야 한다
8. HTTP에 관한 모든 것이 UseCase 레이어로 흘러들어가는 것을 막는다

몇개의 컨트롤러를 만들어야 하는가?

- port를 나눈 만큼 만든다
- 다른 컨트롤러와 공유하지 못하게 한다

하나의 컨트롤러에 여러개의 요청을 왜 받으면 안되는가?

1. 하나의 클래스에 많은 코드가 있으면 좋지않다
    - 코드를 줄이는게 쉽지 않다
2. 테스트 코드의 크기도 커진다
    - mocking 해야 될 것도 ㅁㅁㅁㄴㅇㄹ많아진다
    - 복잡도 커짐
    - 어짜피 UseCase와 비슷한 이름으로 만드는 것이 좋기 때문이다
3. 각자의 모델을 가질 수 있다
    - CreateAccountResource, UpdateAccountResource 등
    - controller 패키지에서 private으로 만든다면 다른 컨트롤러와 공유할 수 없게도 만들 수 있다
4. 패키지를 나눌 수 있다
5. 네이밍하기 편하다
    - Create, Update, Delete로 UseCase를 설명하기는 부족하다
6. 병렬적으로 일하기 좋다

### Persistence Adapter

역할

1. input 받기
2. input과 데이터베이스 포맷 매핑
3. input을 데이터베이스로 전송
4. 데이터베이스 output을 어플리케이션 포멧으로 매핑
5. output 반환

**input 모델, output 모델과 persistence adapter는 application core에 있어야 한다**

- persistence adapter 아님!!.
- 그 외는 기존 persistence layer와 같은 역할

port를 몇개를 만들 것인가

1. 하나만 만들지 않는다
    - 다양한 역할의 인터페이스는 다른 개발자와 테스트할 때 불편하다
    - 예) AService와 BService는 CRepository에 의존할 때, 각각 find, save라는 별개의 메소드를 호출한다면 (각 서비스에 공통되지 않는 메소드를 호출한다면) 각각의 Service를 테스트 할 때, find의 목을 생성했을 때, 다른 개발자는 save라는 목을 생성했다고 착각할 것이다
    - 사실 그렇지 않으면서 정상동작 할 수도 있다
    - 그래서 interface를 자신의 역할에 따라 분리하는 것이 좋다
    - ISP
2. 각각의 포트가 어떤 역할을 하는지 알 수 있다
3. port 하나당 하나의 메소드를 만들 수 있다
    - 테스트하기 편하다

Adapter는 몇개를 만들 것인가

1. 도메인 클래스 별로 하나
    - DDD에서의 **aggregate**
    - one **persistence adapter** per **aggregate**는 여러개의 **bounded context**를 위해 persistence필요성을 분리하는 좋은 방법이다
    - bounded context 간단히 말해 어플리케이션 혹은 패키지를 완전히 분리하는 단위?
    - 예) account 서비스와 billing 서비스가 완전히 분리되서 각 persistence가 서로의 서비스에 접근 못하는 경계
    - 만약 접근하고 싶다면 지정된 port를 통해 접근한다

Entity를 따로 생성 ㄷㄷ

- 어쩐지 도메인 로직이 엔티티에 흘러가고 이러면 디펜던시가 생기는데 어떻게 막았지 했다
- adapter의 input output에만 domain 객체 존재
- 메소드 내부에는 entity만 존재
- entity를 domain으로 매핑하여 output 모델로 만든다
- 결국 output 모델 (dto)는 domain에 대해 알게 된다
- 결론, persistence adapter의 input, output에만 domain객체 존재, 내부에는 entity만! (도메인 로직 쓰지마라)
- domain이 있을 수도 있지만 결국 input, output만 가능

왜 도메인 모델과 데이터베이스 모델을 매핑하나 ㅋㅋㅋ

1. **JPA의 기본 형식에 도메인 로직이 강제화 된다**
    - JPA는 항상 기본 생성자가 필요하다
        - 도메인 로직에는 필요없을 수 있다
    - 혹은 @ManyToOne 관계가 데이터베이스에는 필요하지만 실제 도메인 로직에는 필요없을 수 있다
2. 그래서 풍부한 도메인 객체를 만들 수 있다
    - 데이터베이스 로직에 강제 되지 않기 때문이당

어디에 transaction 바운더리를 둬야하나?

1. Service 레이어
    - 왜냐면 persistence layer에서는 service 레이어의 다른 persistence 레이어를 알지 못한다
    - 그래서 하나의 persistence layer에서만 열어놓으면 문제가 된다
    - 간단히 Service 클래스에 @Transaction 하나 추가한다

# 테스트

1. System Test
    - 실제 요청으로 실제 결과를 검증
    - 3rd 파티 라이브러리는 Mock해야 할 수도 있다
        - 하지만 Port 인터페이스가 있으므로 Mocking 쉽게 가능
    - 역할
        1. 전체 어플리케이션 시작
        2. API 요청
            - 실제 요청 전송
            - RestTemplate
        3. 모든 Layer 검증
    - Behavior Driven Test
        - [http://jgiven.org](https://jgiven.org/)
2. Integration Test
    - Controller
        - Web Adaptor
        - 스프링 프레임워크에 의존하기 때문에 unit test 안된다
        - 범위
            1. 요청
            2. HTTP 요청과  JSON 매핑
            3. JSON 검증
            4. 응답
    - Repository
        - Persistence Adaptor
        - 스프링 프레임워크에 의존하기 때문에 unit test 안된다
        - 역할
            1. Adaptor 로직 검증
            2. 데이터베이스 데이터 매핑
        - 실제 데이터베이스로 테스트
            - [https://www.testcontainers.org/](https://www.testcontainers.org/)
            - 다른 두개의 데이터베이스 시스템을 관리할 이유 줄여줌
3. Unit Test
    - Domain
    - Service

얼마나 테스트하는가

- 맘편히 release 할 수 있을 정도
- **자주 배포할 수록 신뢰가능한 테스트이다**
- 문서와 테스트가 중요하다
    - 운영 중 에러가 있다면 **테스트에 넣고 문서화한다**

Hexagonal Architecture

1. Domain
    - Unit Test
2. Adapter
    - Unit Test
3. Service
    - Integration Test
4. Port
    - 외부 API Mocking 유리

# Boundary간 Mapping

### 1. No Mapping

- web, service, persistence에 domain 공유
    - domain이 많은 역할을 해야한다
    - 각 레이어에서 필요한 특정한 요건을 domain이 모두 충족할 수가 없다
    - SRP 위배
    - 간단한 CRUD는 가능

### 2. Two-Way Mapping

- web, persistence 레이어가 각각 자신만의 모델을 가짐
- domain 모델과 persistence 모델이 port로 들어가고 나갈때 두방향으로 매핑
- 각 레이어에서만 모델 사용
- 다른 레이어에 영향x
- 깨끗한 도메인 모델을 만들어준다
- No mapping 다음으로 가장 간단
    - 내부 레이어에서는 자신의 모델만 안다
    - 도메인 로직에 집중할 수 있다
- 단점
    1. 많은 코드량
    2. domain 모델이 Layer간 통신에 사용된다

### 3. Full Mapping

- 각 operation 마다 모델
    - **Application Layer**
        - **Port**
            - **Request 모델**
            - **Command 모델** → Domain 모델
    - **Web Layer**
        - HTTP 인풋 → Command 모델
    - **Persistence Layer**
        - 오버헤드 때문에 따로 모델 만들지 않는다
- Domain 모델이 레이어 바운더리를 벗어나지 않게 하기 위해

### 4. One-Way Mapping

- 모든 모델을 매핑하는 State Interface 생성
- DDD에 적합
- factory 가능
    - DDD 용어
    - 특정 State로 부터 도메인을 재빌드
- 하지만 어렵다
- 모델의 모양이 비슷하다면 최고의 선택
- 전략 선택 시, 어느 상황에서 최고의 선택인지 가이드라인 필요, 왜 선택했는지도 필요
- 선택
    1. Modify 유즈케이스
        - **Web Layer**와  **Application Layer** 간
            - **Full Mapping**
            - 검증의 특수성 고려
        - **Application Layer**와 **Consistence Layer** 간
            - **No Mapping**
            - 매핑 오버헤드 줄임
            - 만약 이슈가 발생하면 **Two-Way Mapping**
                - Persistence 이슈를 가둘 수 있다
    2. Query 유즈케이스
        - **Web Layer**와  **Application Layer** 간
        - **Application Layer**와 **Consistence Layer** 간
            - **No Mapping**
            - 빠른 코드 작성
            - 만약 이슈가 발생하면 **Two-Way Mapping**

  어떻게 유지가능한 소프트웨어로 만드는가

    - 포트
        - 유즈케이스에 따라 다른 Mapping을 사용하도록 만들어준다