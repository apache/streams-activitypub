package org.apache.streams.activitypub.servlets;

import jakarta.servlet.ServletConfig
import jakarta.servlet.http.HttpServletResponse
import jakarta.ws.rs.core.MediaType
import org.apache.juneau.html.HtmlSerializer
import org.apache.juneau.json.JsonSerializer
import org.apache.juneau.rest.RestRequest
import org.apache.juneau.rest.RestResponse
import org.apache.juneau.rest.annotation.Rest
import org.apache.juneau.rest.annotation.RestGet
import org.apache.juneau.rest.servlet.BasicRestServlet
import org.apache.streams.activitypub.api.WebfingerApi
import org.apache.streams.activitypub.api.pojo.WebfingerQueryRequest
import org.apache.streams.activitypub.api.pojo.WebfingerQueryResponse
import org.apache.streams.activitypub.graph.impl.WebfingerGraphImpl
import org.apache.streams.activitypub.remote.WebfingerRest
import org.apache.streams.activitypub.servlets.WebfingerServlet.webfinger

/**
 * org.apache.streams.activitypub.servlets.WebfingerResource response to inquires about any URI, typically an item in one of the published feeds, or one of the
 * entities referenced by those items.
 *
 * org.apache.streams.activitypub.servlets.WebfingerResource is always bound to '/.well-known/webfinger' per RFC 7033.
 *
 * org.apache.streams.activitypub.servlets.WebfingerResource often delegates the research needed to respond to the request to one of the actors
 * running in the StreamsActorSystem.
 *
 * NOTE: RFC 7033 declares 'application/jrd+json' as the standard Accept Content-Type, but we use text/html
 * as the default when no Accept is supplied, so that casual use through a
 */

object WebfingerServlet {
  given webfinger : WebfingerApi = WebfingerGraphImpl.DEFAULT

}

@Rest(
  title = Array("Webfinger Microservice"),
  description = Array("Webfinger Microservice"),
  defaultAccept = MediaType.TEXT_HTML,
  produces = Array(
    MediaType.APPLICATION_JSON,
    "application/jrd+json",
    MediaType.TEXT_HTML,
  ),
  serializers = Array(classOf[JsonSerializer], classOf[HtmlSerializer]),
  debugOn = "true",
  allowedHeaderParams = "*",
  allowedMethodHeaders = "*",
  allowedMethodParams = "*"
)
class WebfingerServlet extends BasicRestServlet with WebfingerRest {

  override def init(servletConfig: ServletConfig): Unit = {
    super.init(servletConfig)
  }

  def webfingerQueryRequestIsValid( request: WebfingerQueryRequest) : Boolean = {
    !request.getResource().isEmpty()
  }

  @RestGet(
    path = Array("/*"),
    summary = "Lookup resources by URI",
    description = Array("Lookup resources by URI"),
    //matchers = Array(classOf[WebfingerAcceptMatcher])
  )
  def webfingerQueryGet(restRequest: RestRequest,
                        restResponse: RestResponse ) = {

    val resource = restRequest.getParameter("resource")

    val webfingerQueryRequest = new WebfingerQueryRequest()
      .withResource(resource);

    if (webfingerQueryRequestIsValid(webfingerQueryRequest)) {
      try {
        restResponse.setContent(webfingerQuery(webfingerQueryRequest));
        restResponse.setStatus(HttpServletResponse.SC_OK);
      } catch {
        case e : Exception => {
          restResponse.setException(e);
          restResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
      }
    } else {
      restResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

  }

  def doWebfingerQuery( request : WebfingerQueryRequest )( using webfingerApi : WebfingerApi ) : WebfingerQueryResponse = {
    webfingerApi.webfingerQuery(request);
  }

  override def webfingerQuery(request: WebfingerQueryRequest): WebfingerQueryResponse = doWebfingerQuery(request)
}
