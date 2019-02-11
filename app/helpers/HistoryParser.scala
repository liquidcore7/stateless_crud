package helpers

import com.fasterxml.jackson.databind.JsonNode
import models.CrudEntity
import play.libs.Json

import scala.collection.immutable.Set

class HistoryParser[T](private val historyPlain: String)(implicit toEntity: (Long, String) => CrudEntity[T])
  extends BidirectionalConverter[String, Set[CrudEntity[T]]] {

  private val elementsDelimiter: String = ";"

  override def direct(body: String): Set[CrudEntity[T]] =
    body.split(elementsDelimiter)
    .map{topLevelJson =>
      val (id: Long, bodyStr: String) = parseTopLevelJson(topLevelJson)
      toEntity(id, bodyStr)
    }.toSet

  override def inverse(history: Set[CrudEntity[T]]): String =
    history.map(entity => Json.stringify(Json.toJson(entity)))
    .mkString(elementsDelimiter)

  private def parseTopLevelJson(json: String): (Long, String) = {
    val rootNode: JsonNode = Json.parse(json)
    rootNode.get("id").asLong -> rootNode.get("body").asText
  }

  lazy val history: Set[CrudEntity[T]] = direct(historyPlain)

  lazy val lastId: Long = history.map(_.id).max

}
