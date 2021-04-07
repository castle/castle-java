# Changelog

## master

## 2.0.0 (2021-04-07)

**BREAKING CHANGES:**
- [#89](https://github.com/castle/castle-java/pull/89)
  * Rename the `whiteListHeaders/blackListHeaders` configuration option to `allowListHeaders/denyListHeaders`
  * Remove `locale`, `user_agent`, `ip`, `headers`, `client_id` fields from context - no action required
- [#88](https://github.com/castle/castle-java/pull/88) Remove `identify` and `review` commands – they are no longer supported

**Enhancements:**
- [#89](https://github.com/castle/castle-java/pull/89)
  * Add new supported top-level fields to the commands (`fingerprint`, `status`, `headers`, `ip`)
  * Update Readme to reflect the changes
- [#87](https://github.com/castle/castle-java/pull/87) Add configuration of max number of simultaneous API requests
- [#86](https://github.com/castle/castle-java/pull/86) Add docs about custom event properties

## 1.6.0 (2020-10-13)

- [#77](https://github.com/castle/castle-java/pull/77) Add risk policy support
- [#78](https://github.com/castle/castle-java/pull/78) Move impersonator to properties
- [#80](https://github.com/castle/castle-java/pull/80) Fix compilation error
- [#82](https://github.com/castle/castle-java/pull/82) Update the Castle Headers' initialization
