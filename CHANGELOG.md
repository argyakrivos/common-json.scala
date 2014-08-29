# Change log

## 0.2.1 ([#4](https://git.mobcastdev.com/Platform/common-json/pull/4) 2014-08-29 15:49:39)

Cross compile to Scala 2.11

### Improvements

- Now cross-compiles to Scala 2.11.
- Updated dependencies to latest versions.

## 0.2.0 ([#3](https://git.mobcastdev.com/Platform/common-json/pull/3) 2014-08-21 12:52:46)

Added support for serializing LocalDate instances

### New features

- Now serialises joda `LocalDate` instances in the format `yyyy-MM-dd`
so these can be used when a date with no time is needed. This is also
more semantically correct as a LocalDate “represents a date without a
time or time zone”.

## 0.1.1 ([#2](https://git.mobcastdev.com/Platform/common-json/pull/2) 2014-08-06 13:08:29)

Now handles date/times without milliseconds

### Bug fixes

- Now deserialises date/times in ISO format whether they have a
milliseconds component or not.

## 0.1.0 ([#1](https://git.mobcastdev.com/Platform/common-json/pull/1) 2014-08-06 12:07:45)

Initial json helpers

### New features

- Pulls together a bunch of common formats, serializers, etc. that have been used for JSON in other libraries.

