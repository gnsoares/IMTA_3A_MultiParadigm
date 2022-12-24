package rational
import scala.annotation.targetName

def gcd(a: Int, b: Int): Int =
    if b > a then gcd(b, a) else if b == 0 then a else gcd(b, a % b)

class Rational(n_i: Int, d_i: Int = 1) derives CanEqual:

    private val _n = n_i/gcd(n_i, d_i)
    private val _d = d_i/gcd(n_i, d_i)

    def n: Int = _n
    // def n_=(newN: Int): Unit = _n = newN
    def d: Int = _d
    // def d_=(newD: Int): Unit = _d = newD

    override def toString(): String = s"${_n}/${_d}"

    override def equals(that: Any): Boolean = that match
        case that: Rational => this.hashCode() == that.hashCode()
        case that: Float => this.hashCode() == that.toDouble.hashCode()
        case that: Double => this.hashCode() == that.hashCode()
        case that: Int => that == n && 1 == d
        case _ => false

    override def hashCode(): Int = (_n.toDouble/_d).hashCode()

    @targetName("add")
    def +(that: Rational): Rational =
        Rational(_n * that.d + that.n * _d, _d * that.d)

    @targetName("add")
    def +(that: Int): Rational = Rational(_n + that * _d, _d)
    
end Rational

object Rational:
    given CanEqual[Int, Rational] = CanEqual.derived
    given CanEqual[Rational, Int] = CanEqual.derived
    given CanEqual[Float, Rational] = CanEqual.derived
    given CanEqual[Rational, Float] = CanEqual.derived
    given CanEqual[Double, Rational] = CanEqual.derived
    given CanEqual[Rational, Double] = CanEqual.derived
    extension (i: Int)
        @targetName("add")
        infix def +(that: Rational): Rational = that + i
        // def ==(that: Rational): Boolean = that == i
