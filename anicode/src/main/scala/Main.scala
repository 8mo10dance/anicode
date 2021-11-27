import repository.{AnimeRepository, HistoryRepository}
import view.{AnicodeView, CLIView}

import scala.io.Source

object Main {
  def parseOpt(args: Seq[String], boolOpts: Set[String], valueOpts: Map[String, String]): (Set[String], Map[String, String]) = {
    val optPattern = """-(\w+)""".r
    val valuePattern = """(\S+)""".r
    args match {
      case Nil => (boolOpts, valueOpts)
      case optPattern(opt) +: valuePattern(value) +: rest => parseOpt(rest, boolOpts, valueOpts updated (opt, value))
      case optPattern(opt) +: rest => parseOpt(rest, boolOpts + opt, valueOpts)
      case arg +: _ => throw new Exception(s"invalid args ${arg}")
    }
  }

  def getAction(boolOpts: Set[String], valueOpts: Map[String, String]): AnicodeAction = {
    if (boolOpts.contains("s")) {
      SequentialPlay
    } else if (boolOpts.contains("r")) {
      RandomPlay
    } else if (boolOpts.contains("l")) {
      GetAnimeList
    } else if (valueOpts.contains("id")) {
      if (valueOpts.contains("ep")) {
        NormalPlay(valueOpts.get("id").get.toInt, valueOpts.get("ep").get.toInt)
      } else {
        GetEpisodeList(valueOpts.get("id").get.toInt)
      }
    } else {
      GetAnimeList
    }
  }

  def main(args: Array[String]) {
    val (boolOpts, valueOpts) = parseOpt(args.toSeq, Set.empty, Map.empty)
    val profile = valueOpts.getOrElse("profile", ".anicode_profile")
    val anicodeOpt = buildAnicode(profile, CLIView)
    anicodeOpt match {
      case Some(anicode) => {
        val dispatcher = AnicodeDispatcher(AnicodeController(anicode, CLIView))
        val action = getAction(boolOpts, valueOpts)
        dispatcher.dispatch(action)
      }
      case None => // TODO
    }
  }

  private def buildAnicode(profile: String, view: AnicodeView): Option[Anicode] = {
    val config = Source.fromFile(profile).getLines().map(_.split("=") match {
      case Array(key, value) => (key, value)
    }).toMap

    accumlateOptionList(Seq("ANIME_DIR", "RECORD_DIR", "PLAYER").map(config.get(_))).map {
      case animeDirPath +: recordDirPath +: playerPath +: Nil => {
        AnimeRepository.createAnimeRepository(animeDirPath)
        HistoryRepository.createHistoryRepository(recordDirPath)
        if (playerPath == "mock") {
          MockPlayer.createMockPlayer()
        } else {
          ExternalPlayer.createExternalPlayer(playerPath)
        }
        new Anicode()
      }
    }
  }

  private def accumlateOptionList[A](options: Seq[Option[A]]): Option[Seq[A]] =
    options.foldRight[Option[List[A]]](Some(Nil))((option, acc) => map2(option, acc)(_ +: _))

  private def map2[A, B, C](aOpt: Option[A], bOpt: Option[B])(f: (A, B) => C): Option[C] =
    aOpt.flatMap(a => bOpt.map(b => f(a, b)))

}
