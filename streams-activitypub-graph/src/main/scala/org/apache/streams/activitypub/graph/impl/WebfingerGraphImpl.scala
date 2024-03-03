package org.apache.streams.activitypub.graph.impl

import org.apache.jena.query.DatasetFactory
import org.apache.jena.query.ParameterizedSparqlString
import org.apache.jena.rdf.model.Model
import org.apache.jena.rdf.model.impl.ModelCom
import org.apache.jena.riot.RDFFormat
import org.apache.jena.riot.system.PrefixMap
import org.apache.jena.riot.system.PrefixMapStd
import org.apache.jena.riot.writer.JsonLD11Writer
import org.apache.jena.shared.PrefixMapping
import org.apache.jena.sparql.exec.http.QueryExecutionHTTP
import org.apache.jena.sparql.util.Context
import org.apache.jena.sparql.util.PrefixMapping2
import org.apache.jena.sparql.util.Symbol
import org.apache.juneau.collections.JsonMap
import org.apache.juneau.json.JsonParser
import org.apache.streams.activitypub.api.WebfingerApi
import org.apache.streams.activitypub.api.pojo.profile.ProfileQueryResponse
import org.apache.streams.activitypub.api.pojo.webfinger.WebfingerQueryRequest
import org.apache.streams.activitypub.api.pojo.webfinger.WebfingerQueryResponse
import org.apache.streams.activitypub.graph.config.WebfingerGraphImplConfig
import org.apache.streams.activitypub.graph.impl.BaseGraphImpl.jsonParser
import org.apache.streams.activitypub.utils.AcctPrefixResourceToResourceURISwap
import org.apache.streams.activitypub.utils.JsonLdHelper
import org.apache.streams.config.ComponentConfigurator

import java.io.Reader
import java.io.StringWriter
import java.net.URI
import scala.io.Source
import scala.jdk.CollectionConverters.*

/**
 * Implementation of the Webfinger API using jena/fuseki as back-end.
 */
object WebfingerGraphImpl {

  private final val configurator: ComponentConfigurator[WebfingerGraphImplConfig] = new ComponentConfigurator(classOf[WebfingerGraphImplConfig])
  final val config: WebfingerGraphImplConfig = configurator.detectConfiguration()
  final val DEFAULT: WebfingerGraphImpl = new WebfingerGraphImpl(config)

}

class WebfingerGraphImpl(config: WebfingerGraphImplConfig) extends BaseGraphImpl(config) with WebfingerApi {

  /**
   * Query the dataset for the requested resource.
   * @param request
   * @return
   */
  override def webfingerQuery(request: WebfingerQueryRequest): WebfingerQueryResponse = {
    doWebfingerQuery(request)
  }

  def doWebfingerQuery(request: WebfingerQueryRequest): WebfingerQueryResponse = {
    val resourceParamURI: URI = {
      if (request.getResource.startsWith("acct:")) {
        AcctPrefixResourceToResourceURISwap.doUnswap(request.getResource)
      } else {
        new URI(request.getResource)
      }
    }

    val askQueryBody: String = Source.fromResource("queries/webfingerAsk.sparql").getLines.mkString
    val askQuery: ParameterizedSparqlString = new ParameterizedSparqlString(askQueryBody)
    askQuery.setIri("resourceParam", resourceParamURI.toString)
    val askExecution: QueryExecutionHTTP = sparqlBuilder.query(askQuery.asQuery()).build()
    val askResult = askExecution.execAsk()
    if( !askResult ) throw new Exception("Requested resource not found in dataset.")

    val constructQueryBody: String = Source.fromResource("queries/webfingerConstruct.sparql").getLines.mkString
    val constructQuery: ParameterizedSparqlString = new ParameterizedSparqlString(constructQueryBody)
    constructQuery.setIri("resourceParam", resourceParamURI.toString)
    val constructExecution: QueryExecutionHTTP = sparqlBuilder.query(constructQuery.asQuery()).build()
    val modelCom = new ModelCom(constructExecution.execConstruct().getGraph)

    //    val result = writer.write(System.out, constructed, prefixMap, RDFFormat.JSONLD_FRAME_PRETTY, context);
    if( checkModelContainsSubject( resourceParamURI, modelCom ) ) {
      generateResponse(request, modelCom, resourceParamURI)
    } else throw new Exception("Error generating response.")
  }

  /**
   * Translate the model into a webfinger response object.
   * @param request
   * @param model
   * @return
   */
  def generateResponse(request: WebfingerQueryRequest, modelCom: ModelCom, resourceParamURI: URI): WebfingerQueryResponse = {
    val jsonLdWriter = new JsonLD11Writer(RDFFormat.JSONLD11_PRETTY)
    val baseUriString: String = "https://www.w3.org/ns/activitystreams#"
    val baseUri: URI = new URI(baseUriString)
    val datasetGraph = DatasetFactory.wrap(modelCom).asDatasetGraph();
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
    val templateReader: Reader = Source.fromResource("framing/WebfingerQueryResponse.jsonld").reader()
    val template = jsonParser.parse(templateReader, classOf[JsonMap])
    val result = JsonLdHelper.clean(model, template)
    val responseJson = result.asJson()
    val response = jsonParser.parse(responseJson, classOf[WebfingerQueryResponse])
    if (!response.getSubject.startsWith("acct:")) {
      val subjectUri = new URI(response.getSubject)
      response.setSubject(AcctPrefixResourceToResourceURISwap.doSwap(subjectUri))
    }
    response
  }

}
