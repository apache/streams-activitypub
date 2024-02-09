package org.apache.streams.activitypub.servlets

import jakarta.servlet.ServletConfig
import org.apache.juneau.rest.RestRequest
import org.apache.juneau.rest.annotation.Rest
import org.apache.juneau.rest.beans.ChildResourceDescriptions
import org.apache.juneau.rest.servlet.BasicRestServletGroup

/**
 * RootResource responds to requests to the root of the microservice.
 */

@Rest(
  path = "/",
  title=Array("Apache Streams ActivityPub Server"),
  description=Array("Top-level resources page"),
  children = Array(
    classOf[WebfingerServlet]
  )
)
class RootServlet extends BasicRestServletGroup {

  override def init(servletConfig: ServletConfig) : Unit = {
    super.init(servletConfig)
  }
  
}
