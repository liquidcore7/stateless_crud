package models

import scala.collection.immutable.Set

sealed trait Request[T, OP <: Operation] {
  val history: Set[CrudEntity[T]]
}

case class StatefulRequest[E, OP <: Operation](override val history: Set[CrudEntity[E]], operand: CrudEntity[E])
                                                         (implicit operation: OP) extends Request[E, OP] {

  val nextState: Set[CrudEntity[E]] = operation match {
    case _: Add.type => history + operand
    case _: Subtract.type => history - operand
    case _: Mutate.type => history - operand + operand  // remove operand from history based on hashCode and add modified version
  }
}

case class StatelessRequest[E](override val history: Set[CrudEntity[E]], elem: CrudEntity[E])
    extends Request[E, Read.type] {
  val result: Option[CrudEntity[E]] = history.find(_.id == elem.id)
}
