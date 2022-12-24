import money.*

@main def main: Unit = 
    println(Euro)
    println(Currency.exchangeRate(Euro)(Dollar))
    println(Account(10, Euro))
    println(Account(10, Dollar) + Account(10, Euro))
    println(3*Account(10_000, Yen))
