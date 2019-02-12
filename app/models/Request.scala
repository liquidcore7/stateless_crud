package models

import scala.collection.immutable.Set

sealed trait Request[T, OP <: Operation]

class StatefulRequest[E <: CrudEntity[_], OP <: Operation](private val history: Set[E],
                                          private val operand: E)
                                         (implicit operation: OP) extends Request[E, OP] {

  lazy val nextState: Set[E] = operation match {
    case _: Add.type => history + operand
    case _: Subtract.type => history - operand
    case _: Mutate.type => history - operand + operand  // remove operand from history based on hashCode and add modified version
  }
}

object StatefulRequest {
  def apply[E <: CrudEntity[_], OP <: Operation](history: Set[E], operand: E)
                                                (implicit operation: OP): StatefulRequest[E, OP] =
    new StatefulRequest(history, operand)
}



class StatelessRequest[E <: CrudEntity[_]](private val history: Set[E], queriedId: Long)
    extends Request[E, Read.type] {
  lazy val result: Option[E] = history.find(_.id == queriedId)
}

object StatelessRequest {
  def apply[E <: CrudEntity[_]](history: Set[E], queriedId: Long): StatelessRequest[E] =
    new StatelessRequest(history, queriedId)
}
