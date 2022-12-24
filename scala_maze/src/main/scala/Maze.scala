package Maze

import scala.collection.mutable.ListBuffer

enum Exploration:
  case Explored, UnExplored

enum Maze:
  case Branch(
      label: String,
      left: Maze,
      right: Maze,
      var status: Exploration = Exploration.UnExplored
  )
  case Leaf(label: String, var status: Exploration = Exploration.UnExplored)
  def explore(): List[String] =
    val buf = scala.collection.mutable.ListBuffer.empty[String]
    this.explore(buf)
    buf.toList
  def explore(trace: ListBuffer[String]): Unit =
    println(trace)
    this match
      case branch @ Branch(label, left, right, status) =>
        status match
          case Exploration.UnExplored =>
            branch.status = Exploration.Explored;
            trace += label;
            left.explore(trace);
            right.explore(trace);
          case Exploration.Explored => return
      case leaf @ Leaf(label, status) =>
        status match
          case Exploration.UnExplored =>
            leaf.status = Exploration.Explored;
            trace += label;
          case Exploration.Explored => return
