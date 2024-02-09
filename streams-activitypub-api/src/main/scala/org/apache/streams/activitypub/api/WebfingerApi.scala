package org.apache.streams.activitypub.api

import org.apache.streams.activitypub.api.pojo.WebfingerQueryRequest
import org.apache.streams.activitypub.api.pojo.WebfingerQueryResponse

trait WebfingerApi {

  def webfingerQuery(request: WebfingerQueryRequest): WebfingerQueryResponse;

}
