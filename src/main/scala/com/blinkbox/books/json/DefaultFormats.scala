package com.blinkbox.books.json

trait DefaultFormats extends org.json4s.DefaultFormats {
  override val customSerializers = Serializers.all
  override val wantsBigDecimal = true // we often deal with money
}

object DefaultFormats extends DefaultFormats
