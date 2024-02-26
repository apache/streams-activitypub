package org.apache.streams.activitypub.remote

import org.apache.juneau.http.remote.Remote
import org.apache.juneau.http.remote.RemoteGet
import org.apache.streams.activitypub.api.pojo.nodeinfo.NodeinfoQueryResponse

@Remote
trait NodeinfoRest {

  @RemoteGet(path="/.well-known/x-nodeinfo2")
  def nodeinfoQuery: NodeinfoQueryResponse;

}
