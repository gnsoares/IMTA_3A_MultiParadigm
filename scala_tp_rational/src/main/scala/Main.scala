import rational_case.Rational
import scala.collection.immutable.HashSet

@main def main: Unit = 
  val r1 = Rational(1, 2)
  val r2 = Rational(2, 4)
  println(Rational(1, 2))
  println(Rational(-1, 2))
  println(Rational(1, -2))
  println(Rational(-1, -2))
  println(r1 == r2)
  println(r1 == .5)
  var s = HashSet(r1)
  println(s)
  s = s + r2
  println(s)
