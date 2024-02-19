package org.apache.streams.activitypub.servlets

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
import org.apache.streams.activitypub.api.NodeinfoApi
import org.apache.streams.activitypub.api.pojo.nodeinfo.NodeinfoQueryResponse
import org.apache.streams.activitypub.server.NodeinfoApiStaticImpl

/**
 * This is a servlet that will return the nodeinfo document for the server
 */
object NodeinfoServlet {

  final val PATH = ".well-known/x-nodeinfo2"

  given nodeinfo: NodeinfoApi = NodeinfoApiStaticImpl.DEFAULT

}

@Rest(
  title = Array("NodeInfo Microservice"),
  description = Array("NodeInfo Microservice"),
  defaultAccept = MediaType.TEXT_HTML,
  produces = Array(
    MediaType.APPLICATION_JSON,
    MediaType.TEXT_HTML,
  ),
  serializers = Array(classOf[JsonSerializer], classOf[HtmlSerializer]),
  debugOn = "true"
)
class NodeinfoServlet extends BasicRestServlet with NodeinfoApi {

  import NodeinfoServlet.nodeinfo

  override def init(servletConfig: ServletConfig): Unit = {
    super.init(servletConfig)
  }

  @RestGet(
    path = Array("/*"),
    summary = "Request NodeInfo from server",
    description = Array("Request NodeInfo from server"),
  )
  def nodeinfoQueryGet(restRequest: RestRequest,
                       restResponse: RestResponse ) = {
    try {
      restResponse.setContent(nodeinfoQuery)
      restResponse.setStatus(HttpServletResponse.SC_OK);
    } catch {
      case e: Exception => {
        restResponse.setException(e);
        restResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      }
    }
  }

  override def nodeinfoQuery: NodeinfoQueryResponse = {
    doNodeinfoQuery()
  }

  def doNodeinfoQuery()(using nodeinfoApi: NodeinfoApi): NodeinfoQueryResponse = {
    nodeinfoApi.nodeinfoQuery
  }


}
