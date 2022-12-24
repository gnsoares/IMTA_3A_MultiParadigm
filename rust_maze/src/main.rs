mod maze;
use std::sync::Arc;
use std::sync::Mutex;
use std::thread;

fn main() {
    let leaf2 = Arc::new(maze::Maze::Leaf {
        label: "2".to_string(),
        status: Mutex::new(maze::Exploration::Unexplored),
    });
    let leaf4 = Arc::new(maze::Maze::Leaf {
        label: "4".to_string(),
        status: Mutex::new(maze::Exploration::Unexplored),
    });
    let leaf5 = Arc::new(maze::Maze::Leaf {
        label: "5".to_string(),
        status: Mutex::new(maze::Exploration::Unexplored),
    });
    let leaf8 = Arc::new(maze::Maze::Leaf {
        label: "8".to_string(),
        status: Mutex::new(maze::Exploration::Unexplored),
    });
    let branch3 = Arc::new(maze::Maze::Branch {
        label: "3".to_string(),
        left: Arc::clone(&leaf4),
        right: Arc::clone(&leaf5),
        status: Mutex::new(maze::Exploration::Unexplored),
    });
    let branch1 = Arc::new(maze::Maze::Branch {
        label: "1".to_string(),
        left: Arc::clone(&leaf2),
        right: Arc::clone(&branch3),
        status: Mutex::new(maze::Exploration::Unexplored),
    });
    let branch7 = Arc::new(maze::Maze::Branch {
        label: "7".to_string(),
        left: Arc::clone(&leaf5),
        right: Arc::clone(&leaf8),
        status: Mutex::new(maze::Exploration::Unexplored),
    });
    let branch6 = Arc::new(maze::Maze::Branch {
        label: "6".to_string(),
        left: Arc::clone(&branch3),
        right: Arc::clone(&branch7),
        status: Mutex::new(maze::Exploration::Unexplored),
    });
    let maze = Arc::new(maze::Maze::Branch {
        label: "0".to_string(),
        left: Arc::clone(&branch1),
        right: Arc::clone(&branch6),
        status: Mutex::new(maze::Exploration::Unexplored),
    });
    let work = Arc::new(Mutex::new(vec![Arc::clone(&maze)]));
    let trace = Arc::new(Mutex::new(vec![]));
    while !work.lock().unwrap().is_empty() {
        let mut handles = vec![];
        let mut nodes = vec![];

        // take all the nodes currently in stack
        while !work.lock().unwrap().is_empty() {
            nodes.push(work.lock().unwrap().pop().expect("unexpected"));
        }

        // create threads for all the nodes currently in stack
        for node in nodes {
            let work = Arc::clone(&work);
            let trace = Arc::clone(&trace);
            let handle = thread::spawn(move || {
                node.explore(Arc::clone(&node), work, trace);
            });
            handles.push(handle);
        }

        // wait for threads to finish
        for handle in handles {
            handle.join().unwrap();
        }
    }
    println!("Trace: {:?}", *trace.lock().unwrap());
}
