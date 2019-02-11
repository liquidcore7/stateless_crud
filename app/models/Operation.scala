package models

sealed trait Operation

sealed trait MutatingOperation extends Operation
sealed trait NonMutatingOperation extends Operation

case object Add extends MutatingOperation
case object Subtract extends MutatingOperation
case object Mutate extends MutatingOperation
case object Read extends NonMutatingOperation

object Implicits {
  implicit val add: Add.type = Add
  implicit val subtract: Subtract.type = Subtract
  implicit val mutate: Mutate.type = Mutate
  implicit val read: Read.type = Read
}