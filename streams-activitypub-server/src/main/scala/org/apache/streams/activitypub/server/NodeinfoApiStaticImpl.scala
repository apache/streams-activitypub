package org.apache.streams.activitypub.server

import org.apache.streams.activitypub.api.NodeinfoApi
import org.apache.streams.activitypub.api.pojo.nodeinfo.NodeinfoQueryResponse
import org.apache.streams.activitypub.utils.config.NodeinfoApiStaticImplConfig
import org.apache.streams.config.ComponentConfigurator

object NodeinfoApiStaticImpl {

  private final val configurator: ComponentConfigurator[NodeinfoApiStaticImplConfig] = new ComponentConfigurator(classOf[NodeinfoApiStaticImplConfig])
  final val config: NodeinfoApiStaticImplConfig = configurator.detectConfiguration()
  final val DEFAULT: NodeinfoApiStaticImpl = new NodeinfoApiStaticImpl(config)

}

class NodeinfoApiStaticImpl(config: NodeinfoApiStaticImplConfig) extends NodeinfoApi {

  override def nodeinfoQuery: NodeinfoQueryResponse = {
    val response = config
    response
  }

}
