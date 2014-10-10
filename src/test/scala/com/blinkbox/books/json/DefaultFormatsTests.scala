package com.blinkbox.books.json

import java.net.{URI, URL}

import org.joda.time.chrono.ISOChronology
import org.joda.time.{DateTime, DateTimeZone, LocalDate}
import org.json4s.MappingException
import org.json4s.jackson.Serialization.{read, write}
import org.scalatest.FunSuite

object DefaultFormatsTests {
  case class ObjectWithDateTime(value: DateTime)
  case class ObjectWithLocalDate(value: LocalDate)
  case class ObjectWithURI(value: URI)
  case class ObjectWithURL(value: URL)
}

class DefaultFormatsTests extends FunSuite {
  import com.blinkbox.books.json.DefaultFormatsTests._

  implicit val formats = DefaultFormats

  test("Serializes and deserializes a DateTime with milliseconds in UTC") {
    val obj = ObjectWithDateTime(new DateTime(2014, 7, 12, 11, 2, 47, 183, DateTimeZone.UTC))
    val json = write(obj)
    assert(json == """{"value":"2014-07-12T11:02:47.183Z"}""")
    assert(obj == read[ObjectWithDateTime](json))
  }

  test("Serializes a DateTime with milliseconds in a non-UTC zone to UTC") {
    val obj = ObjectWithDateTime(new DateTime(2014, 7, 12, 11, 2, 47, 183, DateTimeZone.forOffsetHours(-3)))
    val json = write(obj)
    assert(json == """{"value":"2014-07-12T14:02:47.183Z"}""")
  }

  test("Deserializes a DateTime with milliseconds in a non-UTC zone to UTC") {
    val obj = ObjectWithDateTime(new DateTime(2014, 7, 12, 11, 2, 47, 183, DateTimeZone.UTC))
    val json = """{"value":"2014-07-12T14:02:47.183+03:00"}"""
    assert(obj == read[ObjectWithDateTime](json))
  }

  test("uses sane error messages when deserializing fails") {
    val json = """{"value":"Ceci n'est pas un jour"}"""
    val ex = intercept[MappingException]{read[ObjectWithDateTime](json)}
    assert(ex.getMessage == "No usable value for value\n'Ceci n'est pas un jour' is not a valid ISO date")
  }

  test("Deserializes a DateTime without milliseconds") {
    val obj = ObjectWithDateTime(new DateTime(2014, 7, 12, 11, 2, 47, DateTimeZone.UTC))
    val json = """{"value":"2014-07-12T11:02:47Z"}"""
    assert(obj == read[ObjectWithDateTime](json))
  }

  test("Does not deserialize a DateTime without a time zone") {
    intercept[MappingException] { read[ObjectWithDateTime]("""{"value":"2014-07-12T11:02:47"}""") }
  }

  test("Does not deserialize a DateTime without a 'T' character between the date and time") {
    intercept[MappingException] { read[ObjectWithDateTime]("""{"value":"2014-07-12 11:02:47Z"}""") }
  }

  test("Does not deserialize a DateTime without a time") {
    intercept[MappingException] { read[ObjectWithDateTime]("""{"value":"2014-07-12"}""") }
  }

  test("Does not deserialize non-ISO DateTime formats") {
    intercept[MappingException] { read[ObjectWithDateTime]("""{"value":"Fri, 09 Sep 2005 13:51:39 -0700"}""") }
    intercept[MappingException] { read[ObjectWithDateTime]("""{"value":"9/9/2005 1:51:39 PM"}""") }
  }

  test("Serializes and deserializes a UTC LocalDate") {
    val obj = ObjectWithLocalDate(new LocalDate(2014, 7, 12))
    val json = write(obj)
    assert(json == """{"value":"2014-07-12"}""")
    assert(obj == read[ObjectWithLocalDate](json))
  }

  test("Serializes and deserializes a non-UTC LocalDate, ignoring any time zone") {
    val obj = ObjectWithLocalDate(new LocalDate(2014, 7, 12, ISOChronology.getInstance(DateTimeZone.forOffsetHours(3))))
    val json = write(obj)
    assert(json == """{"value":"2014-07-12"}""")
    assert(obj == read[ObjectWithLocalDate](json)) // equality of LocalDate ignores the time zone anyway
  }

  test("Does not deserialize non-ISO LocalDate formats") {
    intercept[MappingException] { read[ObjectWithLocalDate]("""{"value":"12-07-2014"}""") }
    intercept[MappingException] { read[ObjectWithLocalDate]("""{"value":"12/07/2014"}""") }
    intercept[MappingException] { read[ObjectWithLocalDate]("""{"value":"12 Jul 2014"}""") }
  }

  test("Serializes and deserializes objects with absolute URIs") {
    val obj = ObjectWithURI(new URI("amqp://guest:guest@localhost:5672/somehost"))
    val json = write(obj)
    assert(json == """{"value":"amqp://guest:guest@localhost:5672/somehost"}""")
    assert(obj == read[ObjectWithURI](json))
  }

  test("Serializes and deserializes objects with relative URIs") {
    val obj = ObjectWithURI(new URI("/somehost"))
    val json = write(obj)
    assert(json == """{"value":"/somehost"}""")
    assert(obj == read[ObjectWithURI](json))
  }

  test("Serializes and deserializes objects with URLs") {
    val obj = ObjectWithURL(new URL("http://localhost:8080/somepath"))
    val json = write(obj)
    assert(json == """{"value":"http://localhost:8080/somepath"}""")
    assert(obj == read[ObjectWithURL](json))
  }

}
