#[derive(Debug)]
enum List {
    Cons(Rc<RefCell<i32>>, Rc<List>),
    Nil,
}
use std::cell::RefCell;
use std::rc::Rc;
use List::{Cons, Nil};
fn main() {
    let data = Rc::new(RefCell::new(10));

    let _first_list = Rc::new(Cons(Rc::clone(&data), Rc::new(Nil)));

    let _second_list = Cons(Rc::new(RefCell::new(9)), Rc::clone(&_first_list));

    let _third_list = Cons(Rc::new(RefCell::new(10)), Rc::clone(&_first_list));

    *data.borrow_mut() += 20;

    println!("first list after = {:?}", _first_list);
    println!("second list after = {:?}", _second_list);
    println!("third list after = {:?}", _third_list);
}
