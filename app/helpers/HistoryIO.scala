package helpers

import models.CrudEntity

import scala.collection.immutable.Set

trait HistoryIO[E <: CrudEntity[_]] {

  def read(history: String)(implicit toModel: (Long, String) => E): HistoryParser[E] = {
    new HistoryParser(
      GzipConverter.combine(UrlEncodeConverter)
        .inverse(history)
    )
  }

  def write(hp: HistoryParser[E], newHistory: Set[E]): String = {
    UrlEncodeConverter.combine(GzipConverter)
      .direct(hp.inverse(newHistory))
  }

}
