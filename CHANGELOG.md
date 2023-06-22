# Changelog

## master

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
