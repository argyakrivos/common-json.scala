package com.blinkbox.books.json

import java.net.{URL, URI}

import org.joda.time.{DateTimeZone, DateTime}
import org.joda.time.format.ISODateTimeFormat
import org.json4s.{Serializer, CustomSerializer}
import org.json4s.JsonAST.{JNull, JString}

object Serializers {

  val all: List[Serializer[_]] = ISODateTimeSerializer :: URISerializer :: URLSerializer :: Nil

  /**
   * Serializer for Joda DateTime objects in the ISO date format, which ensures that the time zone
   * is set to UTC and that the time is converted between zones correctly (it isn't with the standard
   * serializer, and can end up wrong by an hour in BST as it goes via a java.util.DateTime).
   */
  object ISODateTimeSerializer extends CustomSerializer[DateTime](_ => ({
    case JString(s) => ISODateTimeFormat.dateTime.parseDateTime(s).withZone(DateTimeZone.UTC)
    case JNull => null
  }, {
    case d: DateTime => JString(ISODateTimeFormat.dateTime.print(d.withZone(DateTimeZone.UTC)))
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
