package money
import scala.annotation.targetName

// object Currency extends Enumeration:
//   val Euro = "EUR"
//   val Dollar = "USD"
//   val Yen = "YEN"

trait Currency:
    def toString(): String

object Currency:
    val ExchangeRateMap: Map[Currency, Map[Currency, Double]] = Map(
        Euro -> Map(Euro -> 1, Dollar -> 1.15850, Yen -> 131.676),
        Dollar -> Map(Euro -> 0.863187, Dollar -> 1, Yen -> 113.648),
        Yen -> Map(Euro -> 0.00759441, Dollar -> 0.00879910, Yen -> 1)
    )

    def exchangeRate(curr_in: Currency)(curr_out: Currency): Double =
        ExchangeRateMap(curr_in)(curr_out)


object Euro extends Currency:
    override def toString(): String = "EUR"

object Dollar extends Currency:
    override def toString(): String = "USD"

object Yen extends Currency:
    override def toString(): String = "YEN"


case class Account(amount: Double, curr: Currency):
    // override def toString(): String = f"${amount}%.3f ${curr}"
    override def toString(): String = f"${amount} ${curr}"

    @targetName("add")
    def +(that: Account): Account =
        Account(
            Currency.exchangeRate(that.curr)(curr)*that.amount + amount,
            this.curr
        )

object Account:
    extension (d: Double)
        @targetName("mutiply")
        infix def *(that: Account) = Account(d * that.amount, that.curr)
