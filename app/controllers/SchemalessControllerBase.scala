package controllers

import helpers.{GzipConverter, HistoryIO, HistoryParser, UrlEncodeConverter}
import javax.inject.Inject
import models._
import play.api.mvc.{AbstractController, ControllerComponents}

class SchemalessControllerBase @Inject() (cc: ControllerComponents) extends AbstractController(cc)
  with HistoryIO[String] {

  private[this] implicit val toSchemalessEntity: (Long, String) => SchemalessEntity = SchemalessEntity.apply

  protected def perform[OP <: Operation](history: String, maybeElement: Option[String] = None, maybeId: Option[Long] = None)
                                                                             (implicit operation: OP) = Action {

    val historyParser: HistoryParser[String] = read(history)
    val targetId: Long = maybeId.getOrElse(historyParser.lastId + 1)
    val targetElement: String = maybeElement.map(UrlEncodeConverter.inverse).getOrElse("")
    val entity: SchemalessEntity = SchemalessEntity(targetId, targetElement)

    val result: String = operation match {
      case _: NonMutatingOperation => StatelessRequest[String](historyParser.history, entity)
          .result.map(_.body).getOrElse("Not found")

      case _: MutatingOperation =>
        val nextState = StatefulRequest[String, OP](historyParser.history, entity).nextState
        GzipConverter.combine(UrlEncodeConverter)
          .direct(historyParser.inverse(nextState))
    }

    Ok(result)
  }

}
