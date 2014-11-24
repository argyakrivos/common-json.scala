# Change log

## 0.2.4 ([#8](https://git.mobcastdev.com/Platform/common-json/pull/8) 2014-11-24 13:57:54)

Updated dependencies

### Improvements

- Updated version dependencies
- Changed SBT file to multi-project style
- Now only supports scala 2.11

## 0.2.3 ([#7](https://git.mobcastdev.com/Platform/common-json/pull/7) 2014-10-10 13:50:04)

Sane error responses when failing to parse JSON datetime

patch

- Provide a sane error message when trying to parse a date from some posted json, before it would be a `MappingException("unknown error", _)`



## 0.2.2 ([#5](https://git.mobcastdev.com/Platform/common-json/pull/5) 2014-09-30 09:53:57)

Added Json4sExtensions

### Improvements

* Added some common json4s extensions for: removing and overwriting direct fields, along with sha1 hashing of a json

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

