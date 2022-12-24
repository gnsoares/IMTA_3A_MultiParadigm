// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html
import money.*

class MySuite extends munit.FunSuite {
  test("Currency to string") {
    assertEquals(Euro.toString(), "EUR")
    assertEquals(Dollar.toString(), "USD")
    assertEquals(Yen.toString(), "YEN")
  }
  test("Exchange rate map") {
    assertEquals(Currency.exchangeRate(Euro)(Dollar), 1.1585)
  }
  test("Account to string") {
    assertEquals(Account(10, Euro).toString(), "10.0 EUR")
  }
  test("Account add") {
    assertEquals(
      Account(10, Dollar) + Account(10, Euro),
      Account(21.585, Dollar)
    )
  }
  test("Account multiply") {
    assertEquals(3 * Account(10_000, Yen), Account(30_000, Yen))
  }
}
