package org.apache.streams.activitypub.webapp.test.cases

import org.apache.http.HttpHeaders
import org.apache.http.HttpStatus
import org.apache.http.client.utils.URIBuilder
import org.apache.http.entity.ContentType
import org.apache.streams.activitypub.servlets.NodeinfoServlet
import org.apache.streams.activitypub.webapp.test.ActivityPubWebappTestSuiteExtension
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory

import java.nio.charset.Charset;

@ExtendWith(Array(classOf[ActivityPubWebappTestSuiteExtension]))
class NodeinfoServletTest(using helper: ActivityPubWebappTestSuiteExtension) {

    private final val LOGGER = LoggerFactory.getLogger(classOf[NodeinfoServletTest]);

    private val uriBuilder : URIBuilder = helper.uriBuilder
      .setCharset(Charset.defaultCharset())
      .setPath(NodeinfoServlet.PATH)

    /**
     * Request NodeinfoQueryResponse as HTML
     */
    @DisplayName("NodeinfoQuery as HTML")
    @Order(5)
    @Test
    def testNodeinfoQueryAsHtml(using helper: ActivityPubWebappTestSuiteExtension): Unit = {
        val request = helper.restClientBuilder
          .accept(ContentType.TEXT_HTML.getMimeType)
          .json()
          .build()
          .get(uriBuilder.build())
        val response = request.run()
        response.assertStatus(HttpStatus.SC_OK)
        response.assertHeader(HttpHeaders.CONTENT_TYPE).isContains(ContentType.TEXT_HTML.getMimeType)
    }

    /**
     * Request NodeinfoQueryResponse as Json
     */
    @DisplayName("NodeinfoQuery as Json")
    @Order(5)
    @Test
    def testNodeinfoQueryAsJson(using helper: ActivityPubWebappTestSuiteExtension) : Unit = {
        val request = helper.restClientBuilder
          .accept(ContentType.APPLICATION_JSON.getMimeType)
          .json()
          .build()
          .get(uriBuilder.build())
        val response = request.run()
        response.assertStatus(HttpStatus.SC_OK)
        response.assertHeader(HttpHeaders.CONTENT_TYPE).isContains(ContentType.APPLICATION_JSON.getMimeType)
    }

}
