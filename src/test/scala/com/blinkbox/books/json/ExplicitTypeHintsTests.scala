package com.blinkbox.books.json

import org.json4s.jackson.Serialization.{read, write}
import org.scalatest.FunSuite

object ExplicitTypeHintsTests {
  // Some classes to test JSON serialisation with.
  trait Thing
  case class Widget(stringValue: String) extends Thing
  case class Gizmo(intValue: Int) extends Thing
  case class Things(things: List[Thing])
}

class ExplicitTypeHintsTests extends FunSuite {
  import ExplicitTypeHintsTests._

  val things = Things(List(Widget("I'm a widget"), Gizmo(42)))

  test("Format without type hints") {
    // Use the default format, which doesn't use type hints.
    implicit val formats = DefaultFormats

    val serialised = write(things)
    assert(serialised == """{"things":[{"stringValue":"I'm a widget"},{"intValue":42}]}""")

    // Shouldn't be able to deserialise a list with mixed types without type hints.
    intercept[Exception] { read[Things](serialised) }
  }

  test("Format with type hints") {
    // Specify some explicit hints for types.
    implicit val formats = new DefaultFormats {
      override val typeHints = ExplicitTypeHints(Map(classOf[Gizmo] -> "urn:gizmo", classOf[Widget] -> "urn:widget"))
      override val typeHintFieldName = "type"
    }

    val serialised = write(things)
    assert(serialised == """{"things":[{"type":"urn:widget","stringValue":"I'm a widget"},{"type":"urn:gizmo","intValue":42}]}""")
    val deserialised = read[Things](serialised)
    assert(deserialised == things, "Should come out the same after serialisation + deserialisation")
  }

  test("Deserialise class that doesn't have a defined type hint") {
    // Define a type hint for one of the classes only.
    implicit val formats = new DefaultFormats {
      override val typeHints = ExplicitTypeHints(Map(classOf[Gizmo] -> "urn:gizmo"))
      override val typeHintFieldName = "type"
    }

    val data = List(Widget("I'm a widget"))
    val deserialised = read[List[Widget]](write(data))

    assert(data == deserialised, "Should be able to deserialise object with no defined type hint")
  }
}