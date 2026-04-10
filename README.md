# 🖥️ Personal APM Project

> Spring Boot 기반 개인 APM(Application Performance Monitoring) 프로젝트입니다.
> 목표는 Spring 애플리케이션에서 요청별 성능을 모니터링하고, ThreadLocal 기반 요청 추적을 학습하는 것입니다.
## 🗂️ 프로젝트 관리

- **[Wiki](https://github.com/qbsb147/coreapm-lab/wiki)**: 코드 설명, 설계 개념, ThreadLocal/TraceId 동작 원리 등을 정리
- **[GitHub Projects](https://github.com/users/qbsb147/projects/3)**: 진행 단계와 작업 상태를 시각화

## 📌 학습 목표

1. **Spring Boot 기반 APM 구현**

   * 요청별 실행 시간, 예외 처리, 동시성 이슈를 모니터링하는 방법 학습

2. **ThreadLocal과 요청 추적**

   * traceId 생성 및 관리
   * 비동기 환경(@Async, CompletableFuture)에서 ThreadLocal 전파 학습

3. **AOP 기반 성능 모니터링**

   * `@Around`를 활용한 메서드 실행 시간 측정
   * 예외 및 로그 기록과 통합

4. **API Metrics 수집 및 시각화**

   * Timer, Counter, Gauge 등 다양한 메트릭 수집
   * Grafana/Prometheus를 통한 시각화 및 slow API 탐지

5. **통합 로그 관리와 분석**

   * SLF4J, Logback 기반 로그 설계
   * traceId 기반 요청 단위 분석 학습

6. **부하 테스트 및 성능 검증**

   * k6, JMeter 등 도구를 활용한 부하 테스트
   * API 성능 측정과 시뮬레이션 학습

7. **Docker 기반 개발 환경 구성**

   * Prometheus, Grafana 컨테이너 구성
   * Docker Compose를 활용한 로컬 환경 구축 및 테스트

## 🛠️ 기술 스택

* **백엔드**: Spring Boot, Spring AOP, Spring TaskDecorator, CompletableFuture, @Async
* **DB/저장**: MySQL(옵션), Prometheus/Grafana 기반 메트릭 저장 및 시각화, ElasticSearch
* **로깅/모니터링**: SLF4J, Logback, Micrometer, Kibana
* **부하 테스트**: k6, JMeter
* **테스트**: JUnit 5
* **환경/배포**: Docker, Docker Compose

## 📂 프로젝트 구조

```text
main/
├─ Application.java          # Spring Boot 진입점
├─ aspect/                   # PerformanceAspect, 로깅/트레이싱
├─ common/                   # DTO, ThreadLocal ContextHolder, 유틸
├─ config/                   # AsyncConfig, TaskDecorator, MetricsConfig
├─ controller/               # REST API 컨트롤러
├─ filter/                   # Servlet Filter, 요청 전처리/후처리
├─ metrics/                  # API Metrics 수집, slow API 탐지
├─ repository/               # DB 접근/저장소 계층
└─ service/                  # 비즈니스 로직, 서비스 계층
test/                        # 단위/통합 테스트
```

