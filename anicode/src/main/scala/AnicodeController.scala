import repository.AnimeRepository
import view.AnicodeView

import collection.JavaConverters._
import scala.util.Random

case class AnicodeController(view: AnicodeView) {
  val anicode = new Anicode()

  def randomPlay(): Unit = {
    val animeList = anicode.getOnGoingAnimeList.asScala.toSeq
    animeList match {
      case Nil => {
        view.renderNotFoundOnGoingAnime
      }
      case _ => {
        val index = Random.nextInt(animeList.length)
        val anime = animeList(index)
        val ep = anime.getNextEpisode.get()
        view.renderPlay(anime, ep)
        Player.getPlayer.play(anime.getAnimeFilePath(ep).get())
        anicode.save(anime, ep)
      }
    }
  }

  def sequentialPlay(): Unit = {
    val animeOpt = anicode.getLastWatchedAnime
    if (animeOpt.isPresent) {
      val anime = animeOpt.get()
      val nextEpOpt = anime.getNextEpisode
      if (nextEpOpt.isPresent) {
        val ep = nextEpOpt.get()
        val pathOpt = anime.getAnimeFilePath(ep)
        if (pathOpt.isPresent) {
          view.renderPlay(anime, ep)
          Player.getPlayer.play(pathOpt.get())
          anicode.save(anime, ep)
        } else {
          view.renderNotFoundNextEpisode
        }
      } else {
        view.renderNotFoundNextEpisode
      }
    } else {
      view.renderNotFoundNextEpisode
    }
  }

  def normalPlay(id: Int, ep: Int): Unit = {
    val anime = AnimeRepository.getAnimeRepository.getAnimeById(id)
    val epOpt = anime.getEpidodeByEp(ep)
    if (epOpt.isPresent) {
      Player.getPlayer.play(epOpt.get().file)
      anicode.save(anime, ep)
    } else {
      view.renderNotFoundEpisode(anime, ep)
    }
  }

  def displayEpisodeList(id: Int): Unit = {
    val historyList = anicode.getHistoriesByAnimeId(id, 3).asScala
    view.renderHistoryList(historyList)
  }

  def displayAnimeList(): Unit = {
    val animeList = AnimeRepository.getAnimeRepository.getAnimeList.asScala
    view.renderAnimeList(animeList)
  }
}
