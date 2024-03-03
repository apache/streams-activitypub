package org.apache.streams.activitypub.graph.test.cases

import org.apache.juneau.collections.JsonMap
import org.apache.juneau.json.JsonParser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.slf4j.LoggerFactory

import java.io.Reader
import scala.io.Source
import scala.util.Try

/**
 * Tests that all JSON-LD frame resources are valid
 */
class JsonLdFrameValidityTest {

  private final val LOGGER = LoggerFactory.getLogger(classOf[JsonLdFrameValidityTest]);

  private final val jsonParser: JsonParser = JsonParser.DEFAULT.copy()
    .debug()
    .ignoreUnknownBeanProperties()
    .ignoreUnknownEnumValues()
    .build()

  @ParameterizedTest
  @ValueSource(strings = Array(
    "framing/ProfileQueryResponse.jsonld",
    "framing/WebfingerQueryResponse.jsonld"
  ))
  @DisplayName("JsonLdFrameValidityTest")
  @Order(2)
  def testJsonLdFrameValid(frameResource : String): Unit = {
    val frameReader : Reader = Source.fromResource(frameResource).reader()
    val attempt = Try(jsonParser.parse(frameReader, classOf[JsonMap]))
    Assertions.assertTrue(attempt.isSuccess)
  }

}
