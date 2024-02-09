package org.apache.streams.activitypub.webapp.test.cases

import org.apache.http.HttpStatus
import org.apache.http.client.utils.URIBuilder
import org.apache.http.entity.ContentType
import org.apache.juneau.rest.client.RestResponse
import org.apache.streams.activitypub.webapp.test.ActivityPubWebappTestSuite
import org.apache.streams.activitypub.webapp.test.ActivityPubWebappTestSuiteExtension
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.nio.charset.Charset;

class WebfingerServletTest {

    private final val LOGGER = LoggerFactory.getLogger(classOf[WebfingerServletTest]);

    private val uriBuilder : URIBuilder = ActivityPubWebappTestSuite.helper.uriBuilder
      .setCharset(Charset.defaultCharset())
      .setPath(".well-known/webfinger")

    @Test
    @DisplayName("WebfingerQuery With Abbreviated Resource Parameter")
    @throws(classOf[Exception])
    def testWebfingerQueryWithAbbreviatedResourceParameter() : Unit = {
        val request = ActivityPubWebappTestSuite.helper.restClientBuilder
          .accept(ContentType.APPLICATION_JSON.getMimeType)  
          .json()
          .build()
          .get(uriBuilder.setParameter("resource", "acct:sblackmon@asf.social").build())
        val response = request.run();
        response.assertStatus(HttpStatus.SC_OK)
    }
}
            
