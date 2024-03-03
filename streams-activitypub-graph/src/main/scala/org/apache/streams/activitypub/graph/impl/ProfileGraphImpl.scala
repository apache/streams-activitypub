package org.apache.streams.activitypub.graph.impl

import org.apache.http.client.utils.URIBuilder
import org.apache.jena.graph.Node
import org.apache.jena.graph.NodeFactory
import org.apache.jena.query.DatasetFactory
import org.apache.jena.query.ParameterizedSparqlString
import org.apache.jena.rdf.model.Resource
import org.apache.jena.rdf.model.impl.ModelCom
import org.apache.jena.riot.RDFFormat
import org.apache.jena.riot.system.PrefixMap
import org.apache.jena.riot.system.PrefixMapStd
import org.apache.jena.riot.writer.JsonLD11Writer
import org.apache.jena.shared.PrefixMapping
import org.apache.jena.sparql.exec.http.QueryExecutionHTTP
import org.apache.jena.sparql.util.Context
import org.apache.jena.sparql.util.PrefixMapping2
import org.apache.juneau.collections.JsonMap
import org.apache.juneau.json.JsonParser
import org.apache.streams.activitypub.api.NodeinfoApi
import org.apache.streams.activitypub.api.ProfileApi
import org.apache.streams.activitypub.api.pojo.profile.ProfileQueryRequest
import org.apache.streams.activitypub.api.pojo.profile.ProfileQueryResponse
import org.apache.streams.activitypub.graph.config.ProfileGraphImplConfig
import org.apache.streams.activitypub.graph.impl.BaseGraphImpl.jsonParser
import org.apache.streams.activitypub.server.NodeinfoApiStaticImpl
import org.apache.streams.activitypub.utils.JsonLdHelper
import org.apache.streams.config.ComponentConfigurator

import java.io.Reader
import java.io.StringWriter
import java.net.URI
import scala.io.Source
import scala.jdk.CollectionConverters.*

/**
 * Implementation of the Profile API using jena/fuseki as back-end.
 */
object ProfileGraphImpl {

  private final val configurator: ComponentConfigurator[ProfileGraphImplConfig] = new ComponentConfigurator(classOf[ProfileGraphImplConfig])
  final val config: ProfileGraphImplConfig = configurator.detectConfiguration()
  final val DEFAULT: ProfileGraphImpl = new ProfileGraphImpl(config)

  given nodeinfo : NodeinfoApi = NodeinfoApiStaticImpl.DEFAULT

}

class ProfileGraphImpl(config: ProfileGraphImplConfig) extends BaseGraphImpl(config) with ProfileApi {

  import ProfileGraphImpl.nodeinfo

  /**
   * Get the profile page for a user
   * @return
   */
  override def profile(request: ProfileQueryRequest): ProfileQueryResponse = {
    doProfile(request)
  }

  def doProfile(request: ProfileQueryRequest)(using nodeinfo: NodeinfoApi): ProfileQueryResponse = {

    val profileUri: URI = new URIBuilder(nodeinfo.nodeinfoQuery.getServer.getBaseUrl).setPathSegments("users", request.getUsername).build()
    val profileNode: Node = NodeFactory.createURI(profileUri.toString)
    val askQueryBody: String = Source.fromResource("queries/profileAsk.sparql").getLines.mkString
    val askQueryTemplate: ParameterizedSparqlString = new ParameterizedSparqlString(askQueryBody)
    askQueryTemplate.setParam("subjectParam", profileNode)
    val askQuery = askQueryTemplate.asQuery()
    val askExecution: QueryExecutionHTTP = sparqlBuilder.query(askQuery).build()
    val askResult = askExecution.execAsk()
    if (!askResult) throw new Exception("Requested resource not found in dataset.")

    val constructQueryBody: String = Source.fromResource("queries/profileConstruct.sparql").getLines.mkString
    val constructQueryTemplate: ParameterizedSparqlString = new ParameterizedSparqlString(constructQueryBody)
    constructQueryTemplate.setIri("resourceParam", profileUri.toString)
    val constructQuery = constructQueryTemplate.asQuery()
    val constructExecution: QueryExecutionHTTP = sparqlBuilder.query(constructQuery).build()
    val modelCom = new ModelCom(constructExecution.execConstruct().getGraph)

    val subjectResource = modelCom.getResource(profileNode.getURI)

    if (checkModelContainsSubject(profileUri, modelCom)) {
      generateResponse(request, modelCom, subjectResource)
    } else throw new Exception("Error generating response.")
  }

  /**
   * Translate the constructed model into a profile response object.
   *
   * @param request
   * @param model
   * @param subjectResource
   * @return
   */
  def generateResponse(request: ProfileQueryRequest, modelCom: ModelCom, subjectResource: Resource): ProfileQueryResponse = {
    val jsonLdWriter = new JsonLD11Writer(RDFFormat.JSONLD11_PRETTY)
    val baseUriString: String = "https://www.w3.org/ns/activitystreams#"
    val baseUri: URI = new URI(baseUriString)
    val datasetGraph = DatasetFactory.wrap(modelCom).asDatasetGraph()
    val prefixMapping: PrefixMapping = new PrefixMapping2(modelCom)
    val prefixMapValues: Map[String, String] = prefixMapping.getNsPrefixMap.asScala.toMap
    val prefixMap: PrefixMap = {
      val tmp = new PrefixMapStd()
      tmp.putAll(prefixMapValues.asJava)
      tmp
    }
    val jenaContext: Context = Context.fromDataset(datasetGraph)
    val modelWriter = new StringWriter()
    jsonLdWriter.write(modelWriter, datasetGraph, prefixMap, baseUri.toString, jenaContext)
    val model = jsonParser.parse(modelWriter.toString, classOf[JsonMap])
    val templateReader: Reader = Source.fromResource("framing/ProfileQueryResponse.jsonld").reader()
    val template = jsonParser.parse(templateReader, classOf[JsonMap])
    val result = JsonLdHelper.clean(model, template)
    val responseJson = result.asJson()
    val response = jsonParser.parse(responseJson, classOf[ProfileQueryResponse])
    response
  }

}
