package org.apache.streams.activitypub.utils.test.cases

import org.apache.juneau.collections.JsonList
import org.apache.juneau.collections.JsonMap
import org.apache.juneau.json.JsonParser
import org.apache.juneau.json.JsonSerializer
import org.apache.streams.activitypub.utils.JsonLdHelper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

import scala.io.Source

class JsonLdHelperTest {

  /**
   * unit test for JsonLdHelper.clean()
   *
   * This test is designed to ensure that the JsonLdHelper.clean() method is functioning as expected.
   */
  @Test
  @DisplayName("Test Profile Response Cleanup")
  def testProfileFraming(): Unit = {
    val testInputSource = Source.fromResource("JsonLdHelperTest/testProfileCleanupInput.jsonld")
    val testInputJsonMap = JsonParser.DEFAULT.parse(testInputSource.reader(), classOf[JsonMap])
    val testTemplateSource = Source.fromResource("JsonLdHelperTest/testProfileCleanupTemplate.jsonld")
    val testTemplateJsonMap = JsonParser.DEFAULT.parse(testTemplateSource.reader(), classOf[JsonMap])
    val outputJsonMap: JsonMap = JsonLdHelper.clean(testInputJsonMap, testTemplateJsonMap)
    System.out.print(JsonSerializer.DEFAULT_READABLE.serializeToString(outputJsonMap))
    Assertions.assertFalse(outputJsonMap.containsKey("@graph"))
    Assertions.assertFalse(outputJsonMap.containsKey("@id"))
    Assertions.assertTrue(outputJsonMap.containsKey("@context"))
    Assertions.assertEquals(outputJsonMap.get("@context").getClass, classOf[String])
  }

}
