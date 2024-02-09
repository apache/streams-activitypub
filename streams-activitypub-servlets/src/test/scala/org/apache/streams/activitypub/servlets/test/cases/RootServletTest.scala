package org.apache.streams.activitypub.servlets.test.cases

import org.apache.http.HttpHeaders
import org.apache.http.HttpStatus
import org.apache.http.entity.ContentType
import org.apache.juneau.collections.JsonList
import org.apache.juneau.collections.JsonMap
import org.apache.juneau.dto.html5.Body
import org.apache.juneau.dto.html5.Head
import org.apache.juneau.dto.html5.Html
import org.apache.juneau.rest.mock.MockRestClient
import org.apache.streams.activitypub.servlets.RootServlet
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions

import scala.Option
import scala.util.Try

/**
 * RootResourceTest tests that RootResource responds correctly to requests.
 */


class RootServletTest {

  @Test
  @DisplayName("Get RootServlet JSON")
  @throws(classOf[Exception])
  def testGetRootServletJson(): Unit = {
    val request = MockRestClient.create(classOf[RootServlet])
      .json.build.get("/")
    val response = request.run
    response.assertStatus(HttpStatus.SC_OK)
    response.assertHeader(HttpHeaders.CONTENT_TYPE).is(ContentType.APPLICATION_JSON.getMimeType)
    val attempt = Try(response.getEntity.as(classOf[JsonList]))
    val content = attempt.get
    Assertions.assertNotNull(content)
    Assertions.assertTrue(content.size() > 0)
  }

  @Test
  @DisplayName("Get RootServlet HTML")
  @throws(classOf[Exception])
  def testGetRootServletHtml(): Unit = {
    val request = MockRestClient.create(classOf[RootServlet])
      .html.build.get("/")
    val response = request.run
    response.assertStatus(HttpStatus.SC_OK)
    response.assertHeader(HttpHeaders.CONTENT_TYPE).is(ContentType.TEXT_HTML.getMimeType)
    val attempt = Try(response.getEntity.as(classOf[Html]))
    val content = attempt.get
    Assertions.assertNotNull(content)
    Assertions.assertTrue(content.getChild(0).isInstanceOf[Head])
    Assertions.assertTrue(content.getChild(1).isInstanceOf[Body])
  }

}
