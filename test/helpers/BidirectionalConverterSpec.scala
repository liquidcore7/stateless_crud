package helpers

import org.scalatest.{FlatSpec, Matchers}


class BidirectionalConverterSpec extends FlatSpec with Matchers {

  object IntIncrementConverter extends BidirectionalConverter[Int, Int] {
    override def direct(a: Int): Int = a + 1
    override def inverse(b: Int): Int = b - 1
  }

  lazy val testSequence: Range = 0 to 10

  "Bidirectional converter" should "have well-defined inverse" in {

    // Given f and f' as inverse of f the following should hold:
    // f(f'(x)) == f'(f(x)) == x

    testSequence.foreach { testCase =>
      IntIncrementConverter.inverse( IntIncrementConverter.direct(testCase) ) shouldEqual testCase
      IntIncrementConverter.direct( IntIncrementConverter.inverse(testCase) ) shouldEqual testCase
    }
  }

  it should "compose properly" in {
    val incrementByTwo = IntIncrementConverter.combine(IntIncrementConverter)

    testSequence.map(incrementByTwo.direct) shouldEqual testSequence.map(_ + 2)
    testSequence.map(incrementByTwo.inverse) shouldEqual testSequence.map(_ - 2)
  }

}
