package org.apache.streams.activitypub.api

import org.apache.juneau.collections.JsonMap
import org.apache.streams.activitypub.api.pojo.profile.ProfileQueryRequest
import org.apache.streams.activitypub.api.pojo.profile.ProfileQueryResponse

import java.net.URI

trait ProfileApi {

  /**
   * Returns a JSON[-LD] representation of the profile page data.
   *
   * @return ProfileQueryResponse
   */
  def profile(request: ProfileQueryRequest): ProfileQueryResponse

}
