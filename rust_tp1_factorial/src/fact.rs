use num_bigint::{BigUint, ToBigUint};
use num_traits::One;

pub fn fact_bigint(n: i32) -> BigUint {
    fn _fact(n: BigUint, acc: BigUint) -> BigUint {
        let one: BigUint = One::one();
        if n <= One::one() {
            acc
        } else {
            _fact(BigUint::new(n.to_u32_digits()) - one, acc * n)
        }
    }
    _fact(n.to_biguint().unwrap(), One::one())
}

pub fn fact_u128(n: u128) -> u128 {
    fn _fact(n: u128, acc: u128) -> u128 {
        if n <= 1 {
            acc
        } else {
            _fact(n - 1, acc * n)
        }
    }
    _fact(n, 1)
}
