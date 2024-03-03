package org.apache.streams.activitypub.utils

import com.apicatalog.jsonld.JsonLd
import com.apicatalog.jsonld.JsonLdEmbed
import com.apicatalog.jsonld.api.CompactionApi
import com.apicatalog.jsonld.api.FramingApi
import com.apicatalog.jsonld.document.JsonDocument
import jakarta.json.Json
import org.apache.juneau.collections.JsonMap
import org.apache.juneau.json.JsonParser
import org.apache.juneau.json.JsonSerializer

import java.io.StringReader
import java.io.StringWriter

/**
 * Helper for converting the potentially messy json-ld output of
 * an activitystreams base jena model to a clean json representation
 *
 * Ideally this class would not be used often or necessary, but need to figure
 * out how to constrain (frame?) the json-ld serializer to output in compact form
 * b/c no one wants unnecessary @id and @lang in the response
 */
object JsonLdHelper {

  val BASEURI = "https://www.w3.org/ns/activitystreams#"

  private val jsonParser = JsonParser.DEFAULT
  private val jsonSerializer = JsonSerializer.DEFAULT

  /**
   * Convert a JsonLd11Writer output to a json schema compatible representation
   * via framing.
   */
  def clean(model: JsonMap, template: JsonMap): JsonMap = {
    val modelReader = new StringReader(jsonSerializer.serialize(model))
    val templateReader = new StringReader(jsonSerializer.serialize(template))
    val modelDocument = JsonDocument.of(modelReader)
    val templateDocument = JsonDocument.of(templateReader)
    var framing: FramingApi = JsonLd.frame(modelDocument, templateDocument)
    framing = framing.base(BASEURI)
    framing = framing.context(BASEURI)
    framing = framing.embed(JsonLdEmbed.ONCE)
    framing = framing.explicit(true)
    framing = framing.omitDefault(true)
    framing = framing.omitGraph(true)
    val framedWriter = new StringWriter()
    val framedJsonWriter = Json.createWriter(framedWriter)
    framedJsonWriter.write(framing.get())
    val framedReader = new StringReader(framedWriter.toString)
    val framedDocument = JsonDocument.of(framedReader)
    var compact: CompactionApi = JsonLd.compact(framedDocument, BASEURI)
    compact = compact.compactToRelative(true)
    compact = compact.ordered(true)
    val compactWriter = new StringWriter()
    val compactJsonWriter = Json.createWriter(compactWriter)
    compactJsonWriter.write(compact.get())
    val compactJson = compactWriter.toString
    val compactMap = jsonParser.parse(compactJson, classOf[JsonMap])
    compactMap.putAt("@context", BASEURI)
    compactMap
  }

}
