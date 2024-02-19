package org.apache.streams.activitypub.remote

import org.apache.juneau.annotation.Bean
import org.apache.juneau.http.remote.Remote
import org.apache.juneau.http.remote.RemoteGet
import org.apache.streams.activitypub.api.WebfingerApi
import org.apache.streams.activitypub.api.pojo.webfinger.WebfingerQueryRequest
import org.apache.streams.activitypub.api.pojo.webfinger.WebfingerQueryResponse

@Remote
trait WebfingerRest extends WebfingerApi {

  @RemoteGet(path="/.well-known/webfinger")
  def webfingerQuery(@Bean request: WebfingerQueryRequest): WebfingerQueryResponse;

}
