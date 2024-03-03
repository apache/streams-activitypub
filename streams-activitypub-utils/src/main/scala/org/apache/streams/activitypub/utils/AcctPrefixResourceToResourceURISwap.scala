package org.apache.streams.activitypub.utils

import org.apache.http.client.utils.URIBuilder
import org.apache.juneau.BeanSession
import org.apache.juneau.ClassMeta
import org.apache.juneau.swap.StringSwap

import java.net.URI

/**
 * Swaps between a URI and a string with the format acct:preferredName@domain
 * Used by the Webfinger Query endpoint
 */
object AcctPrefixResourceToResourceURISwap {

  val unswapExtractFields = "acct:(.*)@(.*)".r

  def doSwap(uri: URI): String = {
    val domain = uri.getHost
    val preferredName = uri.getPath.split("/").last
    s"acct:${preferredName}@${domain}"
  }
  def doUnswap(string: String): URI = {
    val uriBuilder = new URIBuilder().setScheme("https")
    val (preferredName, domain) = string match {
      case unswapExtractFields(preferredName, domain) => (preferredName, domain)
      case _ => throw new IllegalArgumentException(s"Could not parse $string")
    }
    uriBuilder.setHost(domain)
    uriBuilder.setPath(s"/users/${preferredName}")
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
