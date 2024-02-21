package org.apache.streams.activitypub.graph.test.cases

import org.apache.http.HttpStatus
import org.apache.http.client.utils.URIBuilder
import org.apache.http.entity.ContentType
import org.apache.streams.activitypub.api.pojo.WebfingerQueryRequest
import org.apache.streams.activitypub.api.pojo.WebfingerQueryResponse
import org.apache.streams.activitypub.graph.impl.WebfingerGraphImpl
import org.apache.streams.activitypub.graph.test.ActivityPubGraphTestSuite
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

import java.nio.charset.Charset
import scala.util.Try;

class WebfingerGraphImplTest {

    private final val LOGGER = LoggerFactory.getLogger(classOf[GraphDatabaseServerAvailableTest]);

    val primaryTestResourceURI = "https://mastodon.social/users/steveblackmon"

    /**
     * Test webfinger lookup of a known resource, using abbreviated 'acct:' prefix form
     * @throws java.lang.Exception
     */
    @Test
    @DisplayName("WebfingerQuery For Known Abbreviated Resource")
    @Order(1)
    def testWebfingerQueryForKnownAbbreviatedResource() : Unit = {
        val knownAbbreviatedResource = "acct:steveblackmon@mastodon.social"
        val testWebfingerGraphImpl = WebfingerGraphImpl.DEFAULT
        val request = new WebfingerQueryRequest()
        request.setResource(knownAbbreviatedResource)
        val response: WebfingerQueryResponse = testWebfingerGraphImpl.webfingerQuery(request)
        Assertions.assertNotNull(response)
        Assertions.assertTrue(response.getSubject.equals(primaryTestResourceURI))
    }

    /**
     * Test webfinger lookup of a known resource, using official indexed URI form
     *
     * @throws java.lang.Exception
     */
    @Test
    @DisplayName("WebfingerQuery For Known URI Resource")
    @Order(1)
    def testWebfingerQueryForKnownUriResource(): Unit = {
        val knownPrimaryResourceUri = primaryTestResourceURI
        val testWebfingerGraphImpl = WebfingerGraphImpl.DEFAULT
        val request = new WebfingerQueryRequest()
        request.setResource(knownPrimaryResourceUri)
        val response: WebfingerQueryResponse = testWebfingerGraphImpl.webfingerQuery(request)
        Assertions.assertNotNull(response)
        Assertions.assertTrue(response.getSubject.equals(primaryTestResourceURI))
    }

    /**
     * Test webfinger lookup of a known resource, using common alternative non-indexed URI form
     * This test requires the ask and construct queries to be able to match the alias form,
     * without the alias URI being in the graph.
     * @throws java.lang.Exception
     */
    @Disabled("alias inference not yet implemented")
    @Test
    @DisplayName("WebfingerQuery For Known Alias")
    @Order(1)
    def testWebfingerQueryForKnownAlias(): Unit = {
        val knownAliasUri = "https://mastodon.social/@steveblackmon"
        val testWebfingerGraphImpl = WebfingerGraphImpl.DEFAULT
        val request = new WebfingerQueryRequest()
        request.setResource(knownAliasUri)
        val response: WebfingerQueryResponse = testWebfingerGraphImpl.webfingerQuery(request)
        Assertions.assertNotNull(response)
        Assertions.assertTrue(response.getSubject.equals(primaryTestResourceURI))
    }

    /**
     * Test that webfinger lookup of a non-present resource fails with exception
     * @throws java.lang.Exception
     */
    @Test
    @DisplayName("WebfingerQuery For Missing URI Resource")
    @Order(2)
    def testWebfingerQueryForMissingUriResource(): Unit = {
        val testWebfingerGraphImpl = WebfingerGraphImpl.DEFAULT
        val request = new WebfingerQueryRequest()
        request.setResource("https://mastodon.social/users/joeschmo")
        val attempt = Try(testWebfingerGraphImpl.webfingerQuery(request))
        Assertions.assertTrue(attempt.isFailure)
    }

    /**
     * Test that webfinger lookup of a non-present abbreviated form resource fails with exception
     * @throws java.lang.Exception
     */
    @Test
    @DisplayName("WebfingerQuery For Missing Abbreviated Resource")
    @Order(2)
    def testWebfingerQueryForMissingAbbreviatedResource(): Unit = {
        val testWebfingerGraphImpl = WebfingerGraphImpl.DEFAULT
        val request = new WebfingerQueryRequest()
        request.setResource("acct:joeschmo@mastodon.social")
        val attempt = Try(testWebfingerGraphImpl.webfingerQuery(request))
        Assertions.assertTrue(attempt.isFailure)
    }

    /**
     * Test that webfinger lookup with a malformed abbreviated prefix resource fails with exception
     *
     * @throws java.lang.Exception
     */
    @Test
    @DisplayName("WebfingerQuery For Atypical Prefixed Resource")
    @Order(2)
    def testWebfingerQueryForAtypicalPrefixedResource(): Unit = {
        val testWebfingerGraphImpl = WebfingerGraphImpl.DEFAULT
        val request = new WebfingerQueryRequest()
        request.setResource("account:steveblackmon@mastodon.social")
        val attempt = Try(testWebfingerGraphImpl.webfingerQuery(request))
        Assertions.assertTrue(attempt.isFailure)
    }
}
