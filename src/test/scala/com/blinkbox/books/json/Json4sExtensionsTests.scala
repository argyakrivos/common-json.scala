package com.blinkbox.books.json

import com.blinkbox.books.json.Json4sExtensions._
import org.json4s.JsonAST.{JString, JNothing, JValue}
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods
import org.scalatest.FunSuite

class Json4sExtensionsTests extends FunSuite with JsonMethods {
  val json: JValue =
    ("details" -> (("name" -> "John") ~ ("surname" -> "Smith") ~ ("age" -> 20))) ~
    ("name" -> "John Smith")

  test("Removing a direct field should not remove any other fields with the same name") {
    val result = json.removeDirectField("name")
    assert(result \ "name" == JNothing)
    assert(result \ "details" \ "name" == JString("John"))
  }

  test("Removing a direct field that does not exist should not change the json") {
    val result = json.removeDirectField("whatever")
    assert(result == json)
  }

  test("Overwriting a direct field should not replace any other fields with the same name") {
    val result = json.overwriteDirectField("name", "Bob")
    assert(result \ "name" == JString("Bob"))
    assert(result \ "details" \ "name" == JString("John"))
  }

  test("Overwriting a direct field that does not exist should just add that field") {
    val result = json.overwriteDirectField("data", "abc123")
    assert(result \ "data" == JString("abc123"))
  }

  test("The sha1 hash of two identical json documents should be the same") {
    val j1: JValue = ("f1" -> "a") ~ ("f2" -> "b")
    val j2: JValue = ("f1" -> "a") ~ ("f2" -> "b")
    assert(j1.sha1 == j2.sha1)
  }

  test("The sha1 hash of two different json documents should not be the same") {
    val j1: JValue = ("f1" -> "a") ~ ("f2" -> "b")
    val j2: JValue = ("f2" -> "b") ~ ("f1" -> "a")
    assert(j1.sha1 != j2.sha1)
  }
}
