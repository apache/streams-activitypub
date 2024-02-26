package org.apache.streams.activitypub.api

import org.apache.streams.activitypub.api.pojo.webfinger.WebfingerQueryRequest
import org.apache.streams.activitypub.api.pojo.webfinger.WebfingerQueryResponse

trait WebfingerApi {

  def webfingerQuery(request: WebfingerQueryRequest): WebfingerQueryResponse;

}
