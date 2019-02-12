package models


abstract class CrudEntity[T](val id: Long, val compressed: String) {
  val body: T

  final override def hashCode(): Int = id.hashCode

  final override def equals(o: Any): Boolean = o match {
    case sameType: CrudEntity[T] => sameType.id == this.id
    case _ => false
  }
}
