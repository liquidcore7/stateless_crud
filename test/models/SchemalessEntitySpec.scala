package models

import org.scalatest.{FlatSpec, Matchers}

class SchemalessEntitySpec extends FlatSpec with Matchers {

  "SchemalessEntity" should "have its body equal to compressed arg" in {
    val body: String = "body"
    val schemalessEntity = SchemalessEntity(1, body)
    schemalessEntity.compressed shouldEqual schemalessEntity.body
  }

  it should "be checked for equality based on id" in {
    val id: Long = 123L
    SchemalessEntity(id, "bodyOne") shouldEqual SchemalessEntity(id, "bodyTwo")
  }

}
