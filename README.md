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

# 어플리케이션 결합

### 1. Plain Code

- configuration component
    1. web adapter 인스턴스 생성
    2. HTTP 요청이 web adapter로 라우트 확인
    3. use case 인스턴스 생성
    4. web adapter와 use case 인스턴스 제공
    5. persistence adapter 인스턴스 생성
    6. persistence adapter와 use case 인스턴스 제공
    7. persistence adapter가 데이터베이스에 접근하는지 확인
- 정리
    1. 인스턴스 생성
    2. 의존성 연결
- 단점
    1. Boiler Plate 크다
    2. 모든 클래스가 public이 된다
        - Service가 직접 Persistence에 접근할 수 있다
        - package-private하게 막는다

### 2. classpath 스캔

- 정리
    1. 스프링은 classpath를 모두 뒤져서 @Component가 있는 classs를 찾음
    2. 해당 class로 instance 생성
    3. 해당 class들을 bean이라 부르고, bean들을 관리하는 것이 application context
- 단점
    1. 스프링 프레임워크에 종속
    2. 다른 어플리케이션의 어노테이션을 바인딩 할 수도 있다
        - 어떤 bean이 application context에 로드 되는지 모른다

### 3. 스프링의 자바 설정

- 정리
    1. bean을 생성하는 configuration 클래스 생성 (@Bean)
    2. application context에 등록 (@Properties)
- 장점
    1. Repository의 경우 스프링이 자동 생성
        - @EnableJpaRepositories 사용
        - 해당 어노테이션을 모듈로 옮긴다
        - main에서 실행하면 테스트 할 때도 해당 어노테이션을 사용하기 때문
    2. 테스트에서 mock할 때 매우 유용하다
        - 해당 모듈만 mock 하면 되기 때문
    3. @Component 어노테이션을 여기저기 뿌리고 다닐 필요 없다
        - 모든 레이어에서 스프링 프레임워크의 의존성을 없앨 수 있다
        - 한 곳에서 해당 레이어의 dependency를 관리하여 응집도를 높힐 수 있다
        - SRP
- 단점
    - Configuration 클래스가 해당 레이어 패키지에 없으면 public이 되어야 한다
    - 패키지를 모듈 바운더리로 사용할 수 있다
    - 각 패키지에서 필요한 레이어의 configuration 설정 가능

# 아키텍처 바운더리 강화

- 의존성의 방향을 한방향으로 하는 것이 아키텍쳐 바운더리를 강화하는 방법
    - 레이어 간의 바운더리 구분
    - 안쪽 레이어로 의존성 방향

![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/e308bf70-c141-470e-9418-ea9c2dca3ca2/78759EDB-CD3E-4990-BF92-23F8B7696A19.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/e308bf70-c141-470e-9418-ea9c2dca3ca2/78759EDB-CD3E-4990-BF92-23F8B7696A19.jpeg)

1. Domain 레이어
    - 도메인 모델
2. Application 레이어
    - Input Port
    - Output Port
    - Service
3. Adapter 레이어
    - Web Adapter
    - Persistence Adapter
4. Configuration 레이어
    - **Adapter** Configuration

### Visibility Modifer

1. public
2. protected
3. private
4. **default** = **package private**
    - **자바 패키지**가 클래스를 그룹화 하는 응집도 높은 **모듈**로 사용가능
    - 패키지 내의 클래스끼리 접근 가능하지만, 외부 패키지에서는 접근할 수 없다
    - 패키지 내의 특정 클래스를 모듈의 엔트리 포인트로 만들기 위해 public 지정 가능
    - 이는 디펜던시의 방향이 잘못흐르는 것을 막을 수 있다
    - DI는 reflection을 사용하기 때문에 package private에서도 가능하다
        - 하지만 classpath scanning에서만 가능하다
    - 하지만 크기가 커지면 같은 패킺에 많은 클래스가 있어서 혼란스럽다
        - 서브 패키지를 만들면된다
        - **하지만 자바에서는 서브 패키지는 pakcage private 멤버가 접근하지 못한다**
        - 서브 패키지는 다른 패키지 이기 때문
        - **그래서 서브 패키지의 멤버는 public 해야한다**

```markdown
acount
├── adapter
│   ├── in
│   │   └── **web**
│   │       └── SendMoneyController.java
│   └── out
│       └── **persistence**
│           ├── AccountJpaEntity.java
│           ├── AccountMapper.java
│           ├── AccountPersistenceAdapter.java
│           ├── AccountRepository.java
│           ├── ActivityJpaEntity.java
│           ├── ActivityRepository.java
│           └── PersistenceAdapterConfiguration.java
├── application
│   ├── port
│   │   ├── **in**
│   │   │   ├── GetAccountBalanceQuery.java
│   │   │   ├── SendMoneyCommand.java
│   │   │   └── SendMoneyUseCase.java
│   │   └── **out**
│   │       ├── AccountLock.java
│   │       ├── LoadAccountPort.java
│   │       ├── LocalAccountPort.java
│   │       └── UpdateAccountStatePort.java
│   └── service
│       ├── GetAccountBalanceService.java
│       ├── MoneyTransferProperties.java
│       ├── NoOpAccountLock.java
│       ├── SendMoneyService.java
│       └── ThresholdExceededException.java
└── **domain**
    ├── Account.java
    ├── Activity.java
    ├── ActivityWindow.java
    └── Money.java
```

- **package-private**
    - **Persistence Adapter**
    - **Web Adapter**
    - **Service**
    - 외부에서 접근할 일이 없기 때문이다
    - 외부에서의 접근은 상속받은 인터페이스인 output port에서 이루어진다
- **public**
    - **Input Port**
    - **Output Port**
        - Web Adapter와 Persistence Adapter가 사용
    - **Domain**
        - 다른 레이어에서도 사용

### Post Compiler Check

- public 접근자를 사용하면 컴파일러가 다른 클래스가 사용하도록 한다
- 이때 아키텍쳐가 잘 설계됨에도 불구하고, 의존성의 방향이 잘못된 방향으로 흐를 수 있다
- 설계의 의존성 규칙이 잘 지켜지는지 체크하는 방법이 필요
    - 그것이 post compiler check

Post Compiler Check

- 런타임에 의존성 방향 체크
- 런타임이므로 CI 시, 자동 테스트가 가능하다
- ArchUnit
    - [https://www.archunit.org/](https://www.archunit.org/)
    - 유닛테크스에서 설계 의존성 방향이 깨졌을 때 오류 발생해줌
- DSL로 만들어서 현재 아키텍쳐에서 잘 사용하도록 만들 수도 있다

### Build Artifacts

- 모듈별로 빌드가능

![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/53319e5a-d63c-4e18-a88d-8fdd47fb4f25/DBBA774A-464A-457B-85BA-41BB45911672.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/53319e5a-d63c-4e18-a88d-8fdd47fb4f25/DBBA774A-464A-457B-85BA-41BB45911672.jpeg)

1. 3개
    - configuration.jar
    - adapters.jar
        - **web adapter**
        - **persistence adapter**
        - 빌드 툴이 web과 persistence adapter 사이의 의존성을 막지 않는다
        - 그래서 web adapter와 persistence adapter의 의존성이 흐를 수 있다
    - application.jar
        - entity
        - service
        - port
2. 4개
    - configuration.jar
    - web adapters.jar
    - persistence adapters.jar
    - application.jar
        - entity
        - service
        - port
3. 5개
    - configuration.jar
    - web adapters.jar
    - persistence adapters.jar
    - ports.jar
        - **외부 api** 호출 시 사용
        - **adapter** 호출 시 사용
    - application.jar
        - entity
        - service
4. 6개
    - configuration.jar
    - web adapters.jar
    - persistence adapters.jar
    - api-in.jar
        - input port
    - api-out.jar
        - output port
    - application.jar
        - service
    - domain.jar
        - 외부에서 도메인을 절대 사용하지 못한다

모듈을 나눌 수록 의존성을 잘 통제할 수 있다

- 모듈 내에서만 변경을 가둘 수 있다

하지만 모듈간 매핑이 더 많아진다  → 더 나은 매핑 전략 선택

- 장점
    1. 빌드 툴은 순환 의존성을 싫어한다
        - 순환 의존성에서는 모듈하나를 바꾸면 의존된 다른 모듈 모두에 영향을 미친다
        - SRP 위반
        - 하지만 컴파일러는 순환 의존성 신경쓰지도 않는다 그냥 컴파일
    2. 코드 변화는 모듈 안에서만 일어난다
        - 만약 테스트 할 때, adapter에서 에러 발생 시, 모듈이 분리 안되있다면 application 레이어 테스트 이전에 adapter의 오류를 고쳐야 한다
        - 분리한다면 그럴 필요가 없다
    3. 모듈 간 의존성은 빌드 스크립트에서만 발생
        - 새로운 의존성 추가는 빌드 스크립트에 추가만하면 된다
        - 간단해 진다
        - 빌드 스크립트를 관리하는 의존성만 발생

1. 소프트웨어 아키텍쳐는 의존성 관리가 전부다
2. 패키지 구조가 중요하다
    - 의존성이 한쪽으로 흐르도록
    - pakcage private 사용
3. 빌드 모듈로 아키텍쳐 바운더리를 강화
4. 아키텍쳐가 정착되었다 싶으면 아키텍처를 추출해서 빌드 모듈에 넣는다
    - 디펜던시에 대한 제한적인 통제를 할 수 있다

# 아키텍처 스타일 정하기

1. 언제 hexagonal 아키텍처 사용할 것인가
2. 언제 전통적인 레이어 아키텍처 사용할 것인가

### 도메인이 왕이다

- hexagonal 아키텍처는 DDD에 좋다
    - 도메인을 가장 가운데 놓고
    - 이존성을 도메인으로 향하게 할 수 있다

### 경험은 여왕이다

- 작은 프로젝트 부터 해봐라

### It Depends

- 상황에 알맞은 아키텍처 스타일을 사용한다