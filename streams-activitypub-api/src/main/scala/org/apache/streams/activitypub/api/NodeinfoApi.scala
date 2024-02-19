package org.apache.streams.activitypub.api

import org.apache.streams.activitypub.api.pojo.nodeinfo.NodeinfoQueryResponse

trait NodeinfoApi {

  def nodeinfoQuery: NodeinfoQueryResponse;

}
