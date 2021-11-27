import repository.AnimeRepository
import view.AnicodeView

import scala.util.Random

case class AnicodeController(view: AnicodeView) {
  def randomPlay(): Unit = {
    val animeList = AnicodeService.getOnGoingAnimeList
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
        AnicodeService.save(anime, ep)
      }
    }
  }

  def sequentialPlay(): Unit = {
    val anime = AnicodeService.getLastWatchedAnime
    val nextEpOpt = anime.getNextEpisode
    if (nextEpOpt.isPresent) {
      val ep = nextEpOpt.get()
      val pathOpt = anime.getAnimeFilePath(ep)
      if (pathOpt.isPresent) {
        view.renderPlay(anime, ep)
        Player.getPlayer.play(pathOpt.get())
        AnicodeService.save(anime, ep)
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
      AnicodeService.save(anime, ep)
    } else {
      view.renderNotFoundEpisode(anime, ep)
    }
  }

  def displayEpisodeList(id: Int): Unit = {
    val historyList = AnicodeService.getHistoryListByAnimeId(id).reverse.take(3)
    view.renderHistoryList(historyList)
  }

  def displayAnimeList(): Unit = {
    val animeList = AnicodeService.getAnimeList
    view.renderAnimeList(animeList)
  }
}
