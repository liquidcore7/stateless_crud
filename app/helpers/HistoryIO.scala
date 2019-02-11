package helpers

import models.CrudEntity

import scala.collection.immutable.Set

trait HistoryIO[T] {

  def read(history: String)(implicit toModel: (Long, String) => CrudEntity[T]): HistoryParser[T] = {
    new HistoryParser(
      GzipConverter.combine(UrlEncodeConverter)
        .inverse(history)
    )
  }

  def write(hp: HistoryParser[T], newHistory: Set[CrudEntity[T]]): String = {
    UrlEncodeConverter.combine(GzipConverter)
      .direct(hp.inverse(newHistory))
  }

}
