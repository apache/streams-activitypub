package org.apache.streams.activitypub.webapp.test.cases

import org.apache.http.HttpStatus
import org.apache.http.client.utils.URIBuilder
import org.apache.http.entity.ContentType
import org.apache.streams.activitypub.servlets.WebfingerServlet
import org.apache.streams.activitypub.webapp.test.ActivityPubWebappTestSuite
import org.apache.streams.activitypub.webapp.test.ActivityPubWebappTestSuiteExtension
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.nio.charset.Charset;

@ExtendWith(Array(classOf[ActivityPubWebappTestSuiteExtension]))
class WebfingerServletTest(using helper: ActivityPubWebappTestSuiteExtension) {

    private final val LOGGER = LoggerFactory.getLogger(classOf[WebfingerServletTest]);

    private val uriBuilder : URIBuilder = helper.uriBuilder
      .setCharset(Charset.defaultCharset())
      .setPath(WebfingerServlet.PATH)

    /**
     * Test the WebfingerServlet with a valid abbreviated-form esource parameter
     */
    @DisplayName("WebfingerQuery With Abbreviated Resource Parameter")
    @Order(4)
    @Test
    def testWebfingerQueryWithAbbreviatedResourceParameter(using helper: ActivityPubWebappTestSuiteExtension) : Unit = {
        val request = helper.restClientBuilder
          .accept(ContentType.APPLICATION_JSON.getMimeType)
          .json()
          .build()
          .get(uriBuilder.setParameter("resource", "acct:steveblackmon@mastodon.social").build())
        val response = request.run();
        response.assertStatus(HttpStatus.SC_OK)
    }

    /**
     * Test the WebfingerServlet with a valid URI-form resource parameter
      */
    @DisplayName("WebfingerQuery With URI Resource Parameter")
    @Order(4)
    @Test
    def testWebfingerQueryWithUriResourceParameter(using helper: ActivityPubWebappTestSuiteExtension): Unit = {
        val request = helper.restClientBuilder
          .accept(ContentType.APPLICATION_JSON.getMimeType)
          .json()
          .build()
          .get(uriBuilder.setParameter("resource", "https://mastodon.social/users/steveblackmon").build())
        val response = request.run();
        response.assertStatus(HttpStatus.SC_OK)
    }
}
