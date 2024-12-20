# 프로젝트 설명

### 기술 스택
- Spring Boot
- Kotlin
- Redis
- H2 Database
- Jpa, QueryDsl
- Flyway

### 프로젝트 실행방법

1. Docker Daemon 설치 및 실행
2. cd [root project path]
3. docker-compose up --build

### API 문서
http://localhost:8080/swagger-ui/index.html

### ERD 문서
https://www.erdcloud.com/d/3mfmvFeKjtwk9WTji

# 고민한 내용 및 해결방안

## 문제 조회 API 관련

### 유형 코드 데이터 저장

유형코드가 “uc1580” 형태로 되어있는데 앞에 “uc”라는 문자때문에 정렬이 전체 문자 기준으로 설정되어 제대로 정렬이 되지 않아 범위 검색시에 B+Tree와 같은 인덱스를 활용하지 못해 성능상 좋지 않아 어플리케이션 코드상에서는 “uc1580”를 사용하되 디비상으로는 “uc”를 제외한 1580과 같은 숫자를 저장해 범위 검색시에 B+Tree와 같은 인덱스를 활용하여 성능상 이점을 챙길 수 있습니다. 별도 테이블을 두고 sequence한 id 값만 참조하도록 하는 방식도 있지만 조인또는 별도 조회가 시스템의 복잡성을 올리기 때문에 채택하지 않았습니다.

### 문제 정답 데이터 타입

정답 데이터 같은 경우 제공된 데이터 셋은 모두 int 형태로 되어있지만 추후 어떤 주관식 답이 추가 될지 몰라 데이터 저장 타입을 VARCHAR(255) 형태로 설정하고 추후 더 길이가 긴 정답이 생기는 경우 해당 Size를 쉽게 올릴 수 있기 때문에 해당 타입이 적절하다고 판단했습니다. 

### 문제 관련 커버링 인덱스

문제 같은 경우는 문제 유형 코드, 문제 유형, 난이도 이순으로 필터링을 적용하게 되는데 이 3개에 대한 커버링 인덱스를 생성하면 효율적으로 검색할 수  있습니다. 다만 문제를 조회할 때 정답이 항상 포함될 가능성이 높기 때문에 Disk I/O를 줄이기 위해 커버링 인덱스에 정답도 같이 포함하도록 하거나 정답의 크기가 커진다고 하면 인덱스의 크기가 커질 수 있기 때문에 이런 경우는 필터링 용 인덱스와 별개로 인덱스를 만드는 방식을 채택할 수 있습니다. 어떤 방식을 선택할지는 많은 더미 데이터를 넣고 EXPLAIN 결과를 확인해보는 것인데 단순성을 위해 하나의 인덱스를 생성하는 방식을 채택했습니다.

### 문제 난이도에 따른 문제 비율

- 상 선택시 : **하** 문제 20%, **중** 문제 30%, **상** 문제 50%
- 중 선택시 : **하** 문제 25%, **중** 문제 50%, **상** 문제 25%
- 하 선택시 : **하** 문제 50%, **중** 문제 30%, **상** 문제 20%

위와 같은 요구사항이 존재하는데 문제 조회시 항상 해당 비율을 만족할 수 없기 때문에 

총 문제수에 대한 문제 비율이 소수점으로 떨어지는 경우에는 반올림이나 내림 처리를 하면 총 문제수를 만족하지 못할 수 있기 때문에 올림 처리를 하고 총 문제수만큼 가져오도록 처리(비율에 대한 오차는 있을 순 있지만 올림처리를 하더라도 큰 오차는 없기 때문에 총 문제수를 어느정도 만족시킬 수 있는 방식 채택)

## 학습지 생성 API 관련

### 학습지의 문제 목록 리스트 저장 방식

학습지 테이블에 문제 목록을 **문제 ID 리스트 형태로 저장**하는 방식을 선택했습니다. 이 방식은 **단순성과 유지보수성**을 중시한 설계로, 학습지와 문제의 관계를 효율적으로 관리하기 위함입니다. 문제 ID 리스트를 문자열로 저장하면, 학습지를 조회한 후 문제 테이블에 별도의 쿼리를 날려 데이터를 가져오는 구조가 됩니다. 이로 인해 데이터베이스 구조 및 쿼리가 단순해집니다.

하나의 추가적인 쿼리가 발생하게 되지만 문제 ID 기반으로 Where in 쿼리는 문제 테이블에 존재하는 인덱스를 활용해 효율적으로 조회가 가능합니다. 또한, 학습지 하나당 최대 50개의 문제만 포함할 수 있도록 제한을 두었기 때문에, 문제 ID 리스트의 길이가 관리 가능한 범위 내에 있어 성능에 크게 영향을 주지 않을 것으로 예상됩니다. 추후 문제 테이블의 파티셔닝이나 샤딩을 고려하더라도, 이 방식은 확장성을 유지할 수 있습니다. 데이터 무결성은 문제 리스트 조회후에 어플리케이션에서 검증할 수 있기 때문에 서비스 요구사항에 맞게 처리할 수 있습니다.

## 캐싱 관련

### 어떤 것을 캐싱할까?

요구사항을 생각하면 일단 캐싱하기에 가장 좋은 데이터가 문제 데이터라고 판단을 하였습니다.

학습지에 있는 문제들을 조회할 때와 학습지를 채점할 때 문제를 조회하게 되는데 이러한 요구사항은 같은 데이터를 반복해서 가져올 가능성이 많이 높습니다.

하나의 학습지는 여러 학생에게 배정될 수 있습니다. 이로 인해 동일한 문제들이 여러 학생에 의해 반복적으로 조회되고 채점 시에도 동일한 문제가 반복적으로 사용됩니다.

### 어떤 방식으로 캐싱할까?

복잡한 조건이 포함된 조회는 캐싱하기가 까다롭고 효율적이지 않기 때문에, **문제 ID 기반의 조회**에만 캐싱을 적용했습니다. 문제 ID 리스트를 입력받아 해당 문제 ID들을 기반으로 캐시에서 데이터를 조회한 후, **캐시 미스가 발생한 문제 ID**들만 데이터베이스에서 조회하여 캐시에 저장하는 방식으로 설계했습니다.

이 시스템은 **어플리케이션 서버가 여러 개로 스케일 아웃**될 가능성을 고려해 **분산 캐시**가 필요했습니다. 따라서 다양한 자료구조와 TTL을 지원하는 **Redis**를 인메모리 데이터베이스로 선택했습니다. Redis는 고성능 분산 캐시 시스템으로서, 데이터를 빠르게 처리하고, 노드 간 데이터 일관성을 유지할 수 있습니다. 또한, 캐시 데이터의 **TTL(Time-To-Live)**을 설정해 데이터가 오래되지 않도록 관리할 수 있어 효율적인 메모리 사용이 가능합니다.

성능측면을 좀 더 고려한다면 로컬캐시도 도입해 핫 데이터는 로컬캐시에 캐싱해 Redis와 통신하는 네트워크 비용을 줄일 수 있는데 핫 데이터를 고르는 기준에 대한 알고리즘을 생각해야했기 때문에 시간상 구현하지는 못했습니다.

데이터를 가져올 때 **Redis의 MGET** 명령어를 사용해 여러 키를 한 번에 조회함으로써, 루프를 통해 개별적으로 요청하는 방식보다 **네트워크 비용을 절감**할 수 있습니다. 같은 원리로 **MSET**을 이용해 데이터를 한 번에 설정하여 Redis 서버의 작업 오버헤드를 줄였습니다. 이렇게 함으로써 네트워크와 CPU 리소스를 효율적으로 사용하고, 어플리케이션의 응답 속도를 높일 수 있었습니다.

제가 작성한 로직상 문제 데이터가 바뀔수 없다는 점에서 캐시 무효화 로직을 작성하지 않았지만 만약 문제 데이터가 바뀌는 요구사항이 생긴다면 캐시 무효화 로직을 작성해야합니다.

Redis에 객체를 저장할때 Redis는 byte 형태로 저장해야해서 직렬화 할때 타입정보를 넣어주지 않으면 역직렬화 할 때 타입정보가 없어 역직렬화 하지 못하기 때문에 타입정보를 넣어야하는데 package 부터해서 많은 양의 문자가 포함되기 때문에 문제 데이터에 UnitCode VO 객체와 ProblemType의 ENUM 객체가 존재했었는데 타입정보로 인해 메모리를 더 사용하기 때문에 해당 타입을 기본타입으로 변경해 직렬화할 때 타입정보가 들어가지 않도록 해 메모리를 절약할 수 있도록 설계했습니다.

### 캐시 TTL 설정

캐시 TTL 같은 경우 여러가지 지표를 통해 최적의 값을 설정하는게 좋습니다. 왜냐하면 메모리에 대한 비용이 비싸기 때문에 비효율적으로 운영하게 되면 많은 비용적인 낭비가 생길 수 있습니다. 이러한 지표를 얻기 위해서는 많은 다양한 테스트가 필요하기 때문에 개인적 생각에 따라 캐시 TTL을 **1일(24시간)**로 설정했습니다.

**이는 문제 데이터의 사용 시기**를 고려했는데 보통 사용자들이 문제를 풀고 채점하는 주기가 하루 정도일 것으로 예상되어 하루 동안 데이터를 캐싱하면 채점이나 문제 조회와 관련된 반복적인 요청을 효율적으로 처리할 수 있을 것이라 판단하였습니다. 

## 학습지 학습 통계 관련

### 통계 로직 호출

통계 로직을 매번 API 호출할 때 마다 호출하게 되면 서버의 리소스 낭비도 크기 때문에 해당 방식으로 처리하는 것은 좋지않습니다. 그래서 미리 계산하고 그 결과만 반환해주면 리소스 낭비를 줄일 수 있다고 판단하였습니다. 해당 방법은 크게 두가지로 생각할 수 있는데 하나는 채점할 때 채점이 끝나게 되면 비동기로 해당 학습지에 대한 학습 통계 로직을 호출해 저장하는 방식인데 이방식은 비동기 로직이기 때문에 Lost Update가 발생할 수 있어 정확성을 위해서 분산락이 필요하게 됩니다. 또 다른 하나는 매번 같은 시간에 학습지 학습 통계 로직을 모든 학습지에 대해 수행해 저장하는 방식이 있는데 요구사항에서 빠르게 정확한 데이터를 확인해야하면 전자를 선택할 수 있고 최종 일관성만 맞추면 된다고 하면 후자를 선택할 수 있습니다.
