package org.apache.streams.activitypub.api.test

import org.apache.streams.activitypub.api.WebfingerApi
import org.apache.streams.activitypub.api.pojo.WebfingerQueryRequest
import org.apache.streams.activitypub.api.pojo.WebfingerQueryResponse

class WebfingerApiTestImpl extends WebfingerApi {

  def webfingerQuery(request: WebfingerQueryRequest): WebfingerQueryResponse = {

    new WebfingerQueryResponse().withSubject(request.getResource)

  }

}
