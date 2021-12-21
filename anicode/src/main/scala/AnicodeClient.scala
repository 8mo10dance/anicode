import repository.{AnimeRepository, HistoryRepository}
import view.AnicodeView

import scala.io.Source

object AnicodeClient {
  def apply(profile: String, view: AnicodeView): AnicodeClient = {
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
    } match {
      case Some(anicode) => new AnicodeClient(AnicodeService(anicode, view))
      case None => null // TODO
    }
  }

  private def accumlateOptionList[A](options: Seq[Option[A]]): Option[Seq[A]] =
    options.foldRight[Option[List[A]]](Some(Nil))((option, acc) => map2(option, acc)(_ +: _))

  private def map2[A, B, C](aOpt: Option[A], bOpt: Option[B])(f: (A, B) => C): Option[C] =
    aOpt.flatMap(a => bOpt.map(b => f(a, b)))
}

case class AnicodeClient(anicode: AnicodeService) {
  def dispatch(action: AnicodeAction): Unit = action match {
    case GetAnimeList => anicode.displayAnimeList()
    case GetEpisodeList(id) => anicode.displayEpisodeList(id)
    case NormalPlay(id, ep) => anicode.normalPlay(id, ep)
    case SequentialPlay => anicode.sequentialPlay()
    case RandomPlay => anicode.randomPlay()
  }
}
