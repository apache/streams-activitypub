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
 * Tests that all JSON schema resources are valid
 */
class JsonSchemaValidityTest {

  private final val LOGGER = LoggerFactory.getLogger(classOf[JsonSchemaValidityTest]);

  private final val jsonParser: JsonParser = JsonParser.DEFAULT.copy()
    .debug()
    .ignoreUnknownBeanProperties()
    .ignoreUnknownEnumValues()
    .build()

  @ParameterizedTest
  @ValueSource(strings = Array(
    "org/apache/streams/activitypub/graph/config/BaseGraphImplConfig.json",
    "org/apache/streams/activitypub/graph/config/ProfileGraphImplConfig.json",
    "org/apache/streams/activitypub/graph/config/WebfingerGraphImplConfig.json"
  ))
  @DisplayName("JsonSchemaValidityTest")
  @Order(1)
  def testJsonSchemaValid(schemaResource : String): Unit = {
    val schemaReader : Reader = Source.fromResource(schemaResource).reader()
    val attempt = Try(jsonParser.parse(schemaReader, classOf[JsonMap]))
    Assertions.assertTrue(attempt.isSuccess)
  }

}
