package models

import org.scalatest._

import scala.collection.immutable.HashSet

class CrudEntitySpec extends FlatSpec with Matchers {

  class NoneEntity(override val id: Long, override val compressed: String) extends CrudEntity[None.type](id, compressed) {
    override val body: None.type = None
  }

  "CrudEntity" should "provide hashcode based on its id" in {
    val id: Long = 123L
    id.hashCode shouldEqual new NoneEntity(id, "").hashCode
  }

  it should "check equality based on its id only" in {
    val id: Long = 123L
    new NoneEntity(id, "BodyOne") shouldEqual new NoneEntity(id, "BodyTwo")
  }

  it should "show unequality for different ids" in {
    val body: String = "body"
    new NoneEntity(0, body) should not be equal(new NoneEntity(1, body))
  }

  it should "work properly with HashSets" in {
    val idxRange = 0L to 10L
    val entities: HashSet[NoneEntity] = idxRange.map(new NoneEntity(_, "")).to[HashSet]

    val pickedId: Long = idxRange(5)
    val modifiedBody: String = "modify"
    val modifiedEntity = new NoneEntity(pickedId, modifiedBody)
    (entities - modifiedEntity + modifiedEntity)
      .find(_.compressed == modifiedBody)
      .map(_.compressed) should contain(modifiedBody)
  }

}
