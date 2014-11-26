package com.blinkbox.books.json

import org.json4s.JsonDSL._
import org.json4s._
import org.json4s.jackson.JsonMethods

import scala.language.implicitConversions

object Json4sExtensions {
  implicit class RichJValue(val v: JValue) extends JsonMethods {
    def removeDirectField(key: String): JValue = v.remove(_ == v \ key)
    def removeDirectFields(key: String*): JValue = {
      val fields = key.map(v \ _)
      v.remove(fields.contains(_))
    }
    def overwriteDirectField(key: String, value: JValue) = {
      val newKey: JValue = key -> value
      removeDirectField(key).merge(newKey)
    }
    lazy val sha1: String = {
      // MessageDigest is not thread safe
      val md = java.security.MessageDigest.getInstance("SHA-1")
      md.digest(compact(v).getBytes("UTF-8")).map("%02x".format(_)).mkString
    }
  }
}
