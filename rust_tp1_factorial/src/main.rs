mod fact;

fn main() {
    let ns: [i32; 3] = [10, 30, 100];
    for n in ns {
        println!("(u128) {}! = {}", n, fact::fact_u128(n as u128));
        println!("(BigInt) {}! = {}", n, fact::fact_bigint(n));
    }
}
