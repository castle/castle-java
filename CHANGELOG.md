# Changelog

## master

## 3.0.0 (2026-06-11)

- Add `CastleApiPaymentRequiredException` for HTTP 402
- Add webhook signature verification (`Castle#verifyWebhookSignature`) and the `X-Castle-Signature` header constant
- Add the Events API: `eventsSchema`, `queryEvents` and `groupEvents`
- Add the privacy methods `requestUserData` and `deleteUserData`
- Set the default request timeout to 1000 milliseconds
- Add `CastleConfigurationBuilder#withTimeout(java.time.Duration)`
- Scope `system-stubs-jupiter` to tests
- Redact the `Authorization` header when HTTP request logging is enabled
- Add `Castle#close()` (`AutoCloseable`) to shut down the HTTP dispatcher and connection pool
- Accept an empty response body in `CastleResponse`
- Remove the legacy `authenticate` and `track` endpoints
- Remove device management (`approve`, `report`, `userDevices`, `device`) and impersonation (`impersonateStart`, `impersonateEnd`)
- Remove the `recover` endpoint and the `removeUser` privacy method
- Remove the authenticate failover configuration (`AuthenticateFailoverStrategy`, `withAuthenticateFailoverStrategy`, the `failover_strategy` setting) and the `Verdict`/`CastleMessage` models
- Build and test against Java 17, 21 and 25 (CI matrix); bump JaCoCo to 0.8.15, Mockito to 5.23.0, System Stubs to 2.1.8, AssertJ to 3.27.7 and JUnit to 5.14.4
- Upgrade runtime dependencies: Gson 2.14.0, Guava 33.6.0-jre, SLF4J 2.0.18, OkHttp 4.12.0, jackson-databind-nullable 0.2.10, swagger-annotations 1.6.16, ThreeTen-Backport 1.7.3; declare an explicit `jsr305` dependency for the JSR-305 annotations
- Upgrade test dependencies: Logback 1.5.34, JSONassert 1.5.3, spring-test 6.1.21; remove the unused System Rules dependency
- Upgrade build plugins: maven-compiler-plugin 3.14.0, maven-surefire-plugin 3.5.6, maven-source-plugin 3.4.0, maven-javadoc-plugin 3.12.0, versions-maven-plugin 2.18.0, maven-gpg-plugin 3.2.8 and central-publishing-maven-plugin 0.10.0

## 2.6.1 (2025-07-28)

- [#153](https://github.com/castle/castle-java/pull/153) Remove string json serialization cap
- [#151](https://github.com/castle/castle-java/pull/151) Bump org.springframework:spring-web from 6.0.23 to 6.1.14

## 2.6.0 (2025-02-25)

- [#149](https://github.com/castle/castle-java/pull/149) Add support for List API

## 2.5.0 (2025-01-28)

- [#147](https://github.com/castle/castle-java/pull/147) Update response models and related classes for risk and filter

## 2.4.4 (2025-01-02)

- [#145](https://github.com/castle/castle-java/pull/145) Readd $requested to risk status enum

## 2.4.3 (2024-11-10)

- [#143](https://github.com/castle/castle-java/pull/143) Add custom deserializer for BaseChangesetEntry
- [#142](https://github.com/castle/castle-java/pull/142) Update models to add expand to risk and filter api

## 2.4.2 (2024-10-02)

- [#140](https://github.com/castle/castle-java/pull/140) Add attempted to filter status enum
- [#139](https://github.com/castle/castle-java/pull/139) Bump org.springframework:spring-web from 6.0.18 to 6.0.23
- [#138](https://github.com/castle/castle-java/pull/138) Bump org.springframework:spring-web from 6.0.11 to 6.0.18

## 2.4.1 (2024-02-06)

- [#135](https://github.com/castle/castle-java/pull/135) Serialize null values in ChangesetEntry 
- [#134](https://github.com/castle/castle-java/pull/134) Bump ch.qos.logback:logback-classic from 1.4.11 to 1.4.12
- [#132](https://github.com/castle/castle-java/pull/132) Update nexus staging plugin
- [#131](https://github.com/castle/castle-java/pull/131) Fix singleton tests

## 2.4.0 (2023-08-31)

- [#129](https://github.com/castle/castle-java/pull/129) Update to spring boot 6.0.1 and servlets 6.0.0
- [#127](https://github.com/castle/castle-java/pull/127) Bump com.google.guava:guava from 31.1-jre to 32.0.0-jre


## 2.3.2 (2023-06-22)

- [#125](https://github.com/castle/castle-java/pull/125) Update generated classes to add scores, skip_request_token_validation, skip_context_validation fields, mark risk field as nullable

## 2.3.1 (2023-04-21)

- [#123](https://github.com/castle/castle-java/pull/123) Update dependencies
- [#121](https://github.com/castle/castle-java/pull/121) Update generated classes to add score field


## 2.3.0 (2022-05-20)

- [#119](https://github.com/castle/castle-java/pull/119) Add response body to api exception message
- [#118](https://github.com/castle/castle-java/pull/118) Add not found exception
- [#117](https://github.com/castle/castle-java/pull/117) Add model classes for payload and response for risk, log and filter
- [#116](https://github.com/castle/castle-java/pull/116) Handle prematurely closed response body

## 2.2.1 (2022-05-02)

- [#114](https://github.com/castle/castle-java/pull/114) Handle closed response in OkHttpExceptionUtil

## 2.2.0 (2022-04-29)

- [#112](https://github.com/castle/castle-java/pull/112) Add recover api support
- [#111](https://github.com/castle/castle-java/pull/111) Update dependencies
- [#110](https://github.com/castle/castle-java/pull/110) Rename parameter exception classes
- [#107](https://github.com/castle/castle-java/pull/107) Fixed flaky tests

## 2.1.0 (2022-03-07)

- [#108](https://github.com/castle/castle-java/pull/108) Add exception for handling invalid request token
- [#102](https://github.com/castle/castle-java/pull/102) Add id key that was missing

## 2.0.0 (2021-06-03)

- [#87](https://github.com/castle/castle-java/pull/87) Add configuration for max number of simultaneous api requests
- [#91](https://github.com/castle/castle-java/pull/91) Use float for risk field in Verdict
- [#92](https://github.com/castle/castle-java/pull/92) Add getter for internal json object in Verdict
- [#97](https://github.com/castle/castle-java/pull/97) Add generic request api
- [#98](https://github.com/castle/castle-java/pull/98) Add support for filter, log and risk api

## 1.6.0 (2020-10-13)

- [#77](https://github.com/castle/castle-java/pull/77) Add risk policy support
- [#78](https://github.com/castle/castle-java/pull/78) Move impersonator to properties
- [#80](https://github.com/castle/castle-java/pull/80) Fix compilation error
- [#82](https://github.com/castle/castle-java/pull/82) Update the Castle Headers' initialization
