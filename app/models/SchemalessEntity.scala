package models

case class SchemalessEntity(override val id: Long, override val compressed: String)
  extends CrudEntity[String](id, compressed) {

  override val body: String = compressed
}

object SchemalessEntity {
  private val defaultBody: String = ""

  // as CrudEntities are checked for equality based on id,
  // use this method to create a index
  def fromId(id: Long): SchemalessEntity = SchemalessEntity(id, defaultBody)
}
