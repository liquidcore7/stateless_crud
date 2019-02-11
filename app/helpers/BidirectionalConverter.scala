package helpers


trait BidirectionalConverter[A, B] {
  def direct(a: A): B
  def inverse(b: B): A

  final def combine[C](another: BidirectionalConverter[B, C]): BidirectionalConverter[A, C] = {
    val aToC: A => C = {this.direct(_: A)}.andThen(another.direct)
    val cToA: C => A = {another.inverse(_: C)}.andThen(this.inverse)

    new BidirectionalConverter[A, C] {
      override def direct(a: A): C = aToC(a)
      override def inverse(c: C): A = cToA(c)
    }
  }
}

