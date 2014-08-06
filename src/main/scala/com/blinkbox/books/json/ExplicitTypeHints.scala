package com.blinkbox.books.json

import org.json4s.TypeHints

/**
 * Class that allows custom strings to be used as type hints for classes, by passing in a map
 * of classes to the strings that they should be tagged with in generated JSON.
 */
class ExplicitTypeHints(customHints: Map[Class[_], String]) extends TypeHints {
  private val hintToClass = customHints.map(_.swap)
  override val hints = customHints.keys.toList
  override def hintFor(clazz: Class[_]) = customHints.get(clazz).get
  override def classFor(hint: String) = hintToClass.get(hint)
}

object ExplicitTypeHints {
  def apply(customHints: Map[Class[_], String]) = new ExplicitTypeHints(customHints)
}