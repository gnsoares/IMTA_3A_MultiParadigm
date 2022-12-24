package scalix

import java.io.{File, PrintWriter}
import java.nio.file.{Paths, Files}
import org.json4s.*
import org.json4s.native.JsonMethods.*
import scala.io.Source

trait Config(val url_base: String, val api_key: String)

object Scalix
    extends App,
      Config(
        "https://api.themoviedb.org/3",
        "2f2b17cbfc78bc550c00cf1ed26ea4d1"
      ) {

  // cache maps
  var actorIdMap: Map[(String, String), Int] = Map()
  var actorMoviesMap: Map[Int, Set[(Int, String)]] = Map()
  var movieDirectorMap: Map[Int, (Int, String)] = Map()

  case class FullName(val name: String, val surname: String):
    override def toString(): String = f"$name $surname"

  /* \brief tries to get contents from file, if file does not exist, gets from
   * api and creates file with contents received
   */
  private def getContents(
      cache_path: String,
      url: String
  ): String =
    val file_exists = Files.exists(Paths.get(cache_path))
    if file_exists then Source.fromFile(cache_path).mkString
    else
      val source = Source.fromURL(url).mkString
      val pw = new PrintWriter(new File(cache_path))
      pw.write(source)
      pw.close
      source

  def findActorId(name: String, surname: String): Option[Int] =
    actorIdMap.get((name, surname)) match
      case None =>
        findActorIdFromFile(name, surname) match
          case None         => findActorIdFromAPI(name, surname)
          case i: Some[Int] => i
      case i: Some[Int] => i

  private def findActorIdFromFile(name: String, surname: String): Option[Int] =
    // get file contents
    val cache_path = f"data/$name%%20$surname.json"
    if !Files.exists(Paths.get(cache_path)) then return None
    val source = Source.fromFile(cache_path).mkString

    // parse file contents, update cache map and return id if found
    parse(source) \ "id" match
      case JArray(JInt(id) :: _) =>
        actorIdMap += ((name, surname) -> id.toInt)
        Some(id.toInt)
      case _ => None

  private def findActorIdFromAPI(name: String, surname: String): Option[Int] =
    // make api request
    val cache_path = f"data/$name%%20$surname.json"
    val source = Source
      .fromURL(
        f"$url_base/search/person?api_key=$api_key&language=en-US&page=1&include_adult=false&query=$name%%20$surname"
      )
      .mkString

    // parse api response, update file and return id if found
    parse(source) \ "results" \ "id" match
      case JArray(JInt(id) :: _) =>
        val pw = new PrintWriter(new File(cache_path))
        pw.write(f"{\"id\": ${id.toInt}}")
        pw.close
        actorIdMap += ((name, surname) -> id.toInt)
        Some(id.toInt)
      case _ => None

  def findActorMovies(actorId: Int): Set[(Int, String)] =
    actorMoviesMap.get(actorId) match
      case None => findActorMoviesFromAPIOrFile(actorId)
      case xs   => xs.get

  private def findActorMoviesFromAPIOrFile(actorId: Int): Set[(Int, String)] =
    // get contents either from api or file
    val source = getContents(
      f"data/actor$actorId.json",
      f"$url_base/person/$actorId/movie_credits?api_key=$api_key&language=en-US"
    )

    // help to format result
    implicit val formats: Formats = DefaultFormats
    case class Movie(id: Int, title: String)

    // parse result, update cache map and return result
    val actorMovies = (parse(source) \ "cast")
      .extract[List[Movie]]
      .map(m => (m.id, m.title))
      .toSet
    actorMoviesMap += (actorId -> actorMovies)
    actorMovies

  def findMovieDirector(movieId: Int): Option[(Int, String)] =
    movieDirectorMap.get(movieId) match
      case None => findMovieDirectorFromAPIOrFromFile(movieId)
      case Some(id: Int, name: String) => Some(id, name)

  private def findMovieDirectorFromAPIOrFromFile(
      movieId: Int
  ): Option[(Int, String)] =
    // get contents either from api or file
    val source = getContents(
      f"data/movie$movieId.json",
      f"$url_base/movie/$movieId/credits?api_key=$api_key&language=en-US"
    )

    // help to format result
    implicit val formats: Formats = DefaultFormats
    case class Person(id: Int, name: String, job: String)

    // parse result, update cache map and return result
    (parse(source) \ "crew")
      .extract[List[Person]]
      .find(p => p.job.equals("Director")) match
      case Some(Person(id: Int, name: String, _)) =>
        movieDirectorMap += (movieId -> (id, name))
        Some(id, name)
      case _ => None

  def collaboration(actor1: FullName, actor2: FullName): Set[(String, String)] =
    val actor1Movies = findActorMovies(
      findActorId(actor1.name, actor1.surname).get
    )
    val actor2Movies = findActorMovies(
      findActorId(actor2.name, actor2.surname).get
    )

    // find movies in common and format result with director's name
    actor1Movies
      .filter(m1 => actor2Movies.find(m2 => m2._1 == m1._1) != None)
      .map((id, title) =>
        (
          findMovieDirector(id) match {
            case Some(_: Int, name: String) => name; case None => ""
          },
          title
        )
      )

  // actors to be checked
  val actors = List(
    FullName("Angelina", "Jolie"),
    FullName("Brad", "Pitt"),
    FullName("Leonardo", "DiCaprio"),
    FullName("Margot", "Robbie"),
    FullName("Tom", "Hanks"),
    FullName("Matt", "Damon"),
    FullName("Anne", "Hathaway"),
    FullName("Matthew", "McConaughey"),
    FullName("Jared", "Leto")
  )

  // populate the cache maps with ids and movies
  actors
    .map(fullname => findActorId(fullname.name, fullname.surname))
    .map(id =>
      id match { case Some(i) => findActorMovies(i); case None => None }
    )

  // get all the possible combinations and their movies together
  val collabs =
    for {
      actor1 <- actors
      actor2 <- actors
      if actor1 != actor2
    } yield ((actor1, actor2) -> collaboration(actor1, actor2).map(c => c._2))

  // show duo with the most movies together
  val maxCollabs = collabs.maxBy(c => c._2.size)
  println(
    f"Duo with more movies together: ${maxCollabs._1._1} and ${maxCollabs._1._2}"
  )
  println("Movies:")
  maxCollabs._2.foreach(println)

}
