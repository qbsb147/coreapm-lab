# 🖥️ Personal APM Project

> Spring Boot 기반의 개인 APM(Application Performance Monitoring) 프로젝트입니다.  
> 프로젝트 목표는 Spring 애플리케이션에서 요청별 성능 모니터링과 로그, 메트릭 수집을 구현하고, ThreadLocal 기반 요청 추적을 실습하여 포트폴리오와 면접 준비에 활용하는 것입니다.

## 📌 프로젝트 개요
- **목적**: Spring Boot 환경에서 요청별 실행 시간, 예외, 동시성 이슈를 추적하고 모니터링 기능 구현
- **주요 기능**
  1. ThreadLocal 기반 traceId 관리
  2. AOP(@Around) 기반 메서드 실행 시간 추적
  3. 비동기 환경에서 ThreadLocal 전파 및 검증
  4. API Metrics 수집 및 slow API 탐지
  5. 로그/메트릭 통합 관리

## 🛠️ 기술 스택
- **백엔드**: Spring Boot, Spring AOP, Spring TaskDecorator, CompletableFuture, @Async
- **DB/저장**: MySQL (Metrics 저장, 추적 기록)
- **로깅/모니터링**: SLF4J, Logback
- **테스트**: JUnit 5

## 📂 프로젝트 구조
```text
main/
├─ Application.java          # Spring Boot 메인 클래스, 애플리케이션 진입점
├─ aspect/                   # AOP 관련 클래스 (예: PerformanceAspect, 로깅/트레이싱)
├─ common/                   # 공용 유틸, DTO, ThreadLocal ContextHolder 등
├─ config/                   # Spring 설정 관련 (AsyncConfig, TaskDecorator 등)
├─ controller/               # REST API 컨트롤러
├─ filter/                   # Servlet Filter 및 요청 전처리/후처리
├─ metrics/                  # API Metrics 수집, slow API 탐지 관련
├─ repository/               # DB 접근/저장소 계층 (JPA Repository 등)
└─ service/                  # 비즈니스 로직, 서비스 계층
test/                        # 단위/통합 테스트, ThreadLocal/Async 검증 등
```

## 📝 프로젝트 관리
- **Wiki**: 코드 설명, 설계 개념, ThreadLocal/TraceId 동작 원리 등을 정리
- **GitHub Projects**: 진행 단계와 작업 상태를 시각화
  - 예: `Context`, `Filter`, `Metrics` 단계별 진행
  - Task 카드로 기능 구현, 테스트, 리팩토링 등 관리

## 🚀 진행 방식
1. ThreadLocal 기반 요청 추적 및 ContextHolder 구현
2. AOP로 메서드 실행 시간 측정
3. MetricsCollector를 통한 API 호출 통계 수집
4. 비동기 환경에서 ThreadLocal 전파 테스트
5. Wiki와 Projects를 활용한 진행 상황 관리

## 📌 참고 사항
- ThreadLocal 전파는 기본적으로 비동기(@Async, CompletableFuture)에서 전달되지 않으므로 TaskDecorator로 처리
- 모든 테스트 및 검증 결과는 로그로 확인 가능
