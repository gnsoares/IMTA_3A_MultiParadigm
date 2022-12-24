package rational_case
import scala.annotation.targetName

def gcd(a: Int, b: Int): Int =
    if b > a then gcd(b, a) else if b == 0 then a else gcd(b, a % b)

case class Rational(n: Int, d: Int = 1) derives CanEqual:

    // override def toString(): String = s"${n}/${d}"

    // override def equals(that: Any): Boolean = that match
    //     case that: Rational => this.hashCode() == that.hashCode()
    //     case that: Float => this.hashCode() == that.toDouble.hashCode()
    //     case that: Double => this.hashCode() == that.hashCode()
    //     case that: Int => that == n && 1 == d
    //     case _ => false

    // override def hashCode(): Int = (n.toDouble/d).hashCode()

    @targetName("add")
    def +(that: Rational): Rational =
        Rational(n * that.d + that.n * d, d * that.d)

    @targetName("add")
    def +(that: Int): Rational = Rational(n + that * d, d)
    
end Rational

object Rational:
    given CanEqual[Int, Rational] = CanEqual.derived
    given CanEqual[Rational, Int] = CanEqual.derived
    given CanEqual[Float, Rational] = CanEqual.derived
    given CanEqual[Rational, Float] = CanEqual.derived
    given CanEqual[Double, Rational] = CanEqual.derived
    given CanEqual[Rational, Double] = CanEqual.derived
    def apply(n: Int, d: Int = 1): Rational =
        val sign = n * d / (n * d).abs
        val g = gcd(n.abs, d.abs)
        new Rational(sign*n.abs/g, d.abs/g)
    extension (i: Int)
        @targetName("add")
        infix def +(that: Rational): Rational = that + i
        // def ==(that: Rational): Boolean = that == i
