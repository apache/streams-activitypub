package org.apache.streams.activitypub.util

import org.apache.http.client.utils.URIBuilder
import org.apache.juneau.BeanSession
import org.apache.juneau.ClassMeta
import org.apache.juneau.swap.StringSwap

import java.net.URI

object AcctPrefixResourceToResourceURISwap {
  def doSwap(uri: URI): String = {
    val domain = uri.getHost
    val preferredName = uri.getPath.split("/").last
    s"acct:${preferredName}@${domain}"
  }
  def doUnswap(string: String): URI = {
    val preferredName = string.split("@")(0)
    val domain = string.split("@")(1)
    val uriBuilder = new URIBuilder()
      .setScheme("https")
      .setHost(domain)
      .setPath(s"/users/${preferredName}")
    uriBuilder.build()
  }
}

class AcctPrefixResourceToResourceURISwap extends StringSwap[URI] {

  //@throws(classOf[Exception])
  override def swap(session: BeanSession, uri: URI) : String = {
    AcctPrefixResourceToResourceURISwap.doSwap(uri)
  }

  //@throws(classOf[Exception])
  override def unswap(session: BeanSession, in: String, hint: ClassMeta[Any]): URI = {
    AcctPrefixResourceToResourceURISwap.doUnswap(in);
  }

}
