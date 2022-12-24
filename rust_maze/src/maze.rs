use std::fmt;
use std::sync::Arc;
use std::sync::Mutex;

#[derive(PartialEq, Eq, Debug)]
pub enum Exploration {
    Explored,
    LeftExplored,
    Unexplored,
}

pub enum Maze {
    Branch {
        label: String,
        left: Arc<Maze>,
        right: Arc<Maze>,
        status: Mutex<Exploration>,
    },
    Leaf {
        label: String,
        status: Mutex<Exploration>,
    },
}

impl fmt::Debug for Maze {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        match self {
            Maze::Branch { label, .. } => {
                write!(f, "Branch: {}", label)
            }
            Maze::Leaf { label, .. } => {
                write!(f, "Leaf: {}", label)
            }
        }
    }
}

impl Maze {
    pub fn explore(
        &self,
        copy: Arc<Maze>,
        stack: Arc<Mutex<Vec<Arc<Maze>>>>,
        trace: Arc<Mutex<Vec<Arc<Maze>>>>,
    ) {
        match self {
            Maze::Branch {
                left,
                right,
                status,
                ..
            } => {
                let mut status_uw = status.lock().unwrap();
                let mut stack_uw = stack.lock().unwrap();
                let mut trace_uw = trace.lock().unwrap();
                if *status_uw == Exploration::Unexplored {
                    *status_uw = Exploration::LeftExplored;
                    trace_uw.push(Arc::clone(&copy));
                    stack_uw.push(Arc::clone(&copy));
                    stack_uw.push(Arc::clone(left));
                } else if *status_uw == Exploration::LeftExplored {
                    *status_uw = Exploration::Explored;
                    stack_uw.push(Arc::clone(right));
                }
            }
            Maze::Leaf { status, .. } => {
                let mut status_uw = status.lock().unwrap();
                let mut trace_uw = trace.lock().unwrap();
                if *status_uw == Exploration::Unexplored {
                    *status_uw = Exploration::Explored;
                    trace_uw.push(copy);
                }
            }
        }
    }
}
