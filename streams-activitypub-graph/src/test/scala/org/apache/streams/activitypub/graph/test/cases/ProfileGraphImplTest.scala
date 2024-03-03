package org.apache.streams.activitypub.graph.test.cases

import org.apache.juneau.json.JsonParser
import org.apache.juneau.collections.JsonMap
import org.apache.streams.activitypub.api.pojo.profile.ProfileQueryRequest
import org.apache.streams.activitypub.api.pojo.profile.ProfileQueryResponse
import org.apache.streams.activitypub.graph.impl.ProfileGraphImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assumptions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

import scala.io.Source
import scala.util.Try;

/**
 * Test cases for ProfileGraphImpl
 */
class ProfileGraphImplTest {

    private final val LOGGER = LoggerFactory.getLogger(classOf[ProfileGraphImplTest]);

    private final val testProfileGraphImpl: ProfileGraphImpl = ProfileGraphImpl.DEFAULT

    private final val jsonParser: JsonParser = JsonParser.DEFAULT.copy()
      .debug()
      .ignoreUnknownBeanProperties()
      .ignoreUnknownEnumValues()
      .build()

    /**
     * Test load profile page for an existing 'as:Person' type user
     */
    @Test
    @DisplayName("ProfileQuery For Known Person")
    @Order(5)
    def testGetProfileForKnownPerson() : Unit = {
        val expectedResponseAttempt: Try[ProfileQueryResponse] = {
            val expectedJson = Try(Source.fromResource("ProfileGraphImplTest/testGetProfileForKnownPersonOutput.jsonld").getLines.mkString)
            val expectedMap = Try(JsonMap.ofText(expectedJson.get, jsonParser))
            val expectedResponse = Try(expectedMap.get.cast(classOf[ProfileQueryResponse]))
            expectedResponse
        }
        Assumptions.assumeTrue(expectedResponseAttempt.isSuccess)
        Assumptions.assumeTrue(expectedResponseAttempt.get != null)
        val username = "steveblackmon"
        val request = new ProfileQueryRequest().withUsername(username)
        val attempt = Try(testProfileGraphImpl.profile(request))
        Assertions.assertTrue(attempt.isSuccess)
        val response: ProfileQueryResponse = attempt.get
        Assertions.assertNotNull(response)
        Assertions.assertNotNull(response.getId)
        Assertions.assertNotNull(response.getType)

    }

    /**
     * Test load profile page for a non-existent resource URI from a fake/wrong username
     */
    @Test
    @DisplayName("ProfileQuery For Missing User")
    @Order(5)
    def testGetProfileForMissingUser(): Unit = {
        val username = "elonmusk"
        val request = new ProfileQueryRequest().withUsername(username)
        val attempt = Try(testProfileGraphImpl.profile(request))
        Assertions.assertFalse(attempt.isSuccess)
    }

}
