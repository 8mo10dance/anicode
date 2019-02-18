import scala.io.Source

object AnicodeClient {
  def apply(profile: String): AnicodeClient = {
    val config = Source.fromFile(profile).getLines().map(_.split("=") match {
      case Array(key, value) => (key, value)
    }).toMap

    accumlateOptionList(Seq("ANIME_DIR", "RECORD_DIR", "PLAYER").map(config.get(_))).map {
      case animeDirPath +: recordDirPath +: playerPath +: Nil => new AnicodeManage(Anicode.apply(animeDirPath, recordDirPath), new Player(playerPath))
    } match {
      case Some(manage) => new AnicodeClient(manage)
      case None => null // TODO
    }
  }

  private def accumlateOptionList[A](options: Seq[Option[A]]): Option[Seq[A]] =
    options.foldRight[Option[List[A]]](Some(Nil))((option, acc) => map2(option, acc)(_ +: _))

  private def map2[A, B, C](aOpt: Option[A], bOpt: Option[B])(f: (A, B) => C): Option[C] =
    aOpt.flatMap(a => bOpt.map(b => f(a, b)))
}

case class AnicodeClient(manage: AnicodeManage) {
  def dispatch(action: AnicodeAction): Unit = action match {
    case GetAnimeList => manage.getAnimeList()
    case GetEpisodeList(id) => manage.getEpisodeList(id)
    case NormalPlay(id, ep) => manage.normalPlay(id, ep)
    case SequentialPlay => manage.sequentialPlay()
    case RandomPlay => manage.randomPlay()
  }
}