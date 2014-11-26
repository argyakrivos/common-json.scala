package com.blinkbox.books.json

import java.net.{URI, URL}
import java.util.UUID

import org.joda.time.format.{DateTimeFormatter, DateTimeFormatterBuilder, ISODateTimeFormat}
import org.joda.time.{DateTime, DateTimeZone, LocalDate}
import org.json4s.JsonAST.{JNull, JString}
import org.json4s.{CustomSerializer, MappingException, Serializer}

private object JsonDateTimeFormat {
  val dateTimeOptionalMillis: DateTimeFormatter = {
    val parsers = Array(ISODateTimeFormat.dateTime.getParser, ISODateTimeFormat.dateTimeNoMillis.getParser)
    new DateTimeFormatterBuilder().append(ISODateTimeFormat.dateTime.getPrinter, parsers).toFormatter
  }
}

object Serializers {

  val all: List[Serializer[_]] =
    ISODateTimeSerializer :: LocalDateSerializer :: URISerializer :: URLSerializer :: UUIDSerializer :: Nil

  /**
   * Serializer for Joda DateTime objects in the ISO date format, which ensures that the time zone
   * is set to UTC and that the time is converted between zones correctly (it isn't with the standard
   * serializer, and can end up wrong by an hour in BST as it goes via a java.util.DateTime).
   */
  object ISODateTimeSerializer extends CustomSerializer[DateTime](_ => ({
    case JString(s) =>
      try {
        JsonDateTimeFormat.dateTimeOptionalMillis.parseDateTime(s).withZone(DateTimeZone.UTC)
      } catch {
        case e: Exception => throw MappingException(s"'$s' is not a valid ISO date", e)
      }
    case JNull => null
  }, {
    case d: DateTime => JString(JsonDateTimeFormat.dateTimeOptionalMillis.print(d.withZone(DateTimeZone.UTC)))
  }))

  /**
   * Serializer for Joda LocalDate objects in the ISO date format, which ignores any time zone in
   * the instance (i.e. 2014-07-23 in a non-UTC chronology will still be serialized as 2014-07-23
   * even if the date would be in a different day when the time zone offset is taken into account).
   */
  object LocalDateSerializer extends CustomSerializer[LocalDate](_ => ({
    case JString(s) => ISODateTimeFormat.yearMonthDay.parseLocalDate(s)
    case JNull => null
  }, {
    case d: LocalDate => JString(ISODateTimeFormat.yearMonthDay.print(d))
  }))

  object URISerializer extends CustomSerializer[URI](_ => ({
    case JString(s) => new URI(s)
    case JNull => null
  }, {
    case u: URI => JString(u.toString)
  }))

  object URLSerializer extends CustomSerializer[URL](_ => ({
    case JString(s) => new URL(s)
    case JNull => null
  }, {
    case u: URL => JString(u.toString)
  }))

  object UUIDSerializer extends CustomSerializer[UUID](_ => ({
    case JString(s) => UUID.fromString(s)
    case JNull => null
  }, {
    case id: UUID => JString(id.toString)
  }))

}
