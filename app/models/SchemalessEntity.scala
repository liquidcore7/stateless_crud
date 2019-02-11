package models

case class SchemalessEntity(override val id: Long, override val compressed: String) extends CrudEntity[String](id, compressed) {
  override val body: String = compressed
}
