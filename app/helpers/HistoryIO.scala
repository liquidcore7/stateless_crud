package helpers

import models.CrudEntity

import scala.collection.immutable.Set

trait HistoryIO[E <: CrudEntity[_]] {

  def read(history: String)(implicit toModel: (Long, String) => E): HistoryParser[E] = {
    new HistoryParser(
      GzipConverter.combine(Base64Encoder)
        .inverse(history)
    )
  }

  def write(hp: HistoryParser[E], newHistory: Set[E]): String = {
    GzipConverter.combine(Base64Encoder)
      .direct(hp.inverse(newHistory))
  }

}
