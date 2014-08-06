package com.blinkbox.books.json

import java.net.{URL, URI}

import org.joda.time.{DateTimeZone, DateTime}
import org.joda.time.format.{DateTimeFormatter, DateTimeFormatterBuilder, ISODateTimeFormat}
import org.json4s.{Serializer, CustomSerializer}
import org.json4s.JsonAST.{JNull, JString}

private object JsonDateTimeFormat {
  val dateTimeOptionalMillis: DateTimeFormatter = {
    val parsers = Array(ISODateTimeFormat.dateTime.getParser, ISODateTimeFormat.dateTimeNoMillis.getParser)
    new DateTimeFormatterBuilder().append(ISODateTimeFormat.dateTime.getPrinter, parsers).toFormatter
  }
}

object Serializers {

  val all: List[Serializer[_]] = ISODateTimeSerializer :: URISerializer :: URLSerializer :: Nil

  /**
   * Serializer for Joda DateTime objects in the ISO date format, which ensures that the time zone
   * is set to UTC and that the time is converted between zones correctly (it isn't with the standard
   * serializer, and can end up wrong by an hour in BST as it goes via a java.util.DateTime).
   */
  object ISODateTimeSerializer extends CustomSerializer[DateTime](_ => ({
    case JString(s) => JsonDateTimeFormat.dateTimeOptionalMillis.parseDateTime(s).withZone(DateTimeZone.UTC)
    case JNull => null
  }, {
    case d: DateTime => JString(JsonDateTimeFormat.dateTimeOptionalMillis.print(d.withZone(DateTimeZone.UTC)))
  }))

  object URISerializer extends CustomSerializer[URI](_ => ( {
    case JString(s) => new URI(s)
    case JNull => null
  }, {
    case u: URI => JString(u.toString)
  }))

  object URLSerializer extends CustomSerializer[URL](_ => ( {
    case JString(s) => new URL(s)
    case JNull => null
  }, {
    case u: URL => JString(u.toString)
  }))

}
