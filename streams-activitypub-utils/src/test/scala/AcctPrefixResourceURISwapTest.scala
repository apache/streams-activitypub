package org.apache.streams.activitypub.utils.test.cases

import org.apache.http.client.utils.URIBuilder
import org.apache.streams.activitypub.utils.AcctPrefixResourceToResourceURISwap
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

import java.net.URI

class AcctPrefixResourceURISwapTest {

  private final val swapper = new AcctPrefixResourceToResourceURISwap()

  @Test
  @DisplayName("Test Acct Prefix Swap")
  def testSwap() = {
    val testInput: URI = new URIBuilder()
      .setScheme("https")
      .setHost("mastodon.social")
      .setPath("/users/steveblackmon")
      .build()
    val testOutput: String = "acct:steveblackmon@mastodon.social"
    Assertions.assertEquals(testOutput, AcctPrefixResourceToResourceURISwap.doSwap(testInput))
  }

  @Test
  @DisplayName("Test Acct Prefix Un-Swap")
  def testUnswap() = {
    val testInput: String = "acct:steveblackmon@mastodon.social"
    val testOutput: URI = new URIBuilder("https://mastodon.social/users/steveblackmon").build()
    Assertions.assertEquals(testOutput, AcctPrefixResourceToResourceURISwap.doUnswap(testInput))
  }
}
