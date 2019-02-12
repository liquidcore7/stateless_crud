package models

import org.scalatest.{FlatSpec, Matchers}

import scala.collection.immutable.NumericRange

class RequestSpec extends FlatSpec with Matchers {

  import models.Implicits._

  val historyIdRange: NumericRange[Long] = 0L to 15L
  val fakeHistory: Set[SchemalessEntity] = historyIdRange.map{id => SchemalessEntity(id, "id" + id.toString)}.toSet

  "Stateless request" should "fetch item from history based on id" in {
    val lastId = historyIdRange.last
    val queriedEntity: SchemalessEntity = SchemalessEntity(lastId, "")
    val result = StatelessRequest[SchemalessEntity](fakeHistory, lastId).result
    result.getOrElse(queriedEntity).body shouldEqual "id" + lastId.toString
  }

  private def getFromHistory(history: Set[SchemalessEntity], id: Long): Option[SchemalessEntity] = {
    StatelessRequest[SchemalessEntity](history, id).result
  }

  // used to access history in all three tests
  var history: Set[SchemalessEntity] = _

  "Stateful request" should "insert first item into empty history" in {
    val toInsert: SchemalessEntity = SchemalessEntity(0, "body")
    history = StatefulRequest[SchemalessEntity, Add.type](Set.empty, toInsert).nextState

    getFromHistory(history, toInsert.id)
      .getOrElse(SchemalessEntity(1, "")).body shouldEqual toInsert.body
  }

  it should "insert items into non-empty history" in {
    val toInsert = fakeHistory.tail
    toInsert.foreach { entity =>
      history = StatefulRequest[SchemalessEntity, Add.type](history, entity).nextState
    }
    history.tail shouldEqual fakeHistory.tail
  }

  it should "mutate elements in existing history" in {
    val toMutate = fakeHistory.head
    history = StatefulRequest[SchemalessEntity, Mutate.type](history, toMutate).nextState

    history shouldEqual fakeHistory
  }

  it should "be able to remove all elements from the history" in {
    historyIdRange.foreach { id =>
      history = StatefulRequest[SchemalessEntity, Subtract.type](history, SchemalessEntity.fromId(id)).nextState
    }
    history.isEmpty shouldBe true
  }

}
