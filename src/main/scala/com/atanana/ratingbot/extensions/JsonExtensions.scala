package com.atanana.ratingbot.extensions

import spray.json.{JsBoolean, JsNumber, JsString, JsValue}

object JsonExtensions {

  extension (json: JsValue) def intField(field: String): Int = json.asJsObject
    .fields(field)
    .asInstanceOf[JsNumber]
    .value.toIntExact

  extension (json: JsValue) def stringField(field: String): String = json.asJsObject
    .fields(field)
    .asInstanceOf[JsString]
    .value

  extension (json: JsValue) def booleanField(field: String): Boolean = json.asJsObject
    .fields(field)
    .asInstanceOf[JsBoolean]
    .value
}
