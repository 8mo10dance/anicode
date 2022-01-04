import scala.io.Source

object AnicodeProfile {
  def parse(profilePath: String): Option[AnicodeProfile] = {
    val config = Source.fromFile(profilePath).getLines().map(_.split("=") match {
      case Array(key, value) => (key, value)
    }).toMap

    accumlateOptionList(Seq("ANIME_DIR", "RECORD_DIR", "PLAYER").map(config.get(_))).flatMap {
      case animeDirPath +: recordDirPath +: playerPath +: Nil =>
        Some(AnicodeProfile(animeDirPath, recordDirPath, playerPath))
      case _ => None
    }
  }

  private def accumlateOptionList[A](options: Seq[Option[A]]): Option[Seq[A]] =
    options.foldRight[Option[List[A]]](Some(Nil))((option, acc) => map2(option, acc)(_ +: _))

  private def map2[A, B, C](aOpt: Option[A], bOpt: Option[B])(f: (A, B) => C): Option[C] =
    aOpt.flatMap(a => bOpt.map(b => f(a, b)))
}

case class AnicodeProfile(animeDirPath: String, recordDirPath: String, playerPath: String)
