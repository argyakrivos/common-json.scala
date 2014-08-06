package com.blinkbox.books.json

import java.net.{URL, URI}

import org.joda.time.{DateTimeZone, DateTime}
import org.json4s.jackson.Serialization.{read, write}
import org.scalatest.FunSuite

object DefaultFormatsTests {
  case class ObjectWithDateTime(value: DateTime)
  case class ObjectWithURI(value: URI)
  case class ObjectWithURL(value: URL)
}

class DefaultFormatsTests extends FunSuite {
  import com.blinkbox.books.json.DefaultFormatsTests._

  implicit val formats = DefaultFormats

  test("Serializes and deserializes a Joda DateTime in UTC") {
    val obj = ObjectWithDateTime(new DateTime(2014, 7, 12, 11, 2, 47, 183, DateTimeZone.UTC))
    val json = write(obj)
    assert(json == """{"value":"2014-07-12T11:02:47.183Z"}""")
    assert(obj == read[ObjectWithDateTime](json))
  }

  test("Serializes a Joda DateTime in a non-UTC zone to UTC") {
    val obj = ObjectWithDateTime(new DateTime(2014, 7, 12, 11, 2, 47, 183, DateTimeZone.forOffsetHours(-3)))
    val json = write(obj)
    assert(json == """{"value":"2014-07-12T14:02:47.183Z"}""")
  }

  test("Deserializes a Joda DateTime in a non-UTC zone to UTC") {
    val obj = ObjectWithDateTime(new DateTime(2014, 7, 12, 11, 2, 47, 183, DateTimeZone.UTC))
    val json = """{"value":"2014-07-12T14:02:47.183+03:00"}"""
    assert(obj == read[ObjectWithDateTime](json))
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
