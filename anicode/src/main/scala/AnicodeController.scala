import repository.AnimeRepository
import view.AnicodeView

import scala.util.Random

case class AnicodeController(anicodeService: AnicodeService, player: Player, view: AnicodeView) {
  def randomPlay(): Unit = {
    val animeList = anicodeService.getOnGoingAnimeList
    animeList match {
      case Nil => {
        view.renderNotFoundOnGoingAnime
      }
      case _ => {
        val index = Random.nextInt(animeList.length)
        val anime = animeList(index)
        val ep = anime.getNextEpisode.get()
        view.renderPlay(anime, ep)
        player.play(anime.getAnimeFilePath(ep).get())
        anicodeService.save(anime, ep)
      }
    }
  }

  def sequentialPlay(): Unit = {
    val anime = anicodeService.getLastWatchedAnime
    val nextEpOpt = anime.getNextEpisode
    if (nextEpOpt.isPresent) {
      val ep = nextEpOpt.get()
      val pathOpt = anime.getAnimeFilePath(ep)
      if (pathOpt.isPresent) {
        view.renderPlay(anime, ep)
        player.play(pathOpt.get())
        anicodeService.save(anime, ep)
      } else {
        view.renderNotFoundNextEpisode
      }
    } else {
      view.renderNotFoundNextEpisode
    }
  }

  def normalPlay(id: Int, ep: Int): Unit = {
    val anime = anicodeService.animeRepository.getAnimeById(id)
    val epOpt = anime.getEpidodeByEp(ep)
    if (epOpt.isPresent) {
      player.play(epOpt.get().file)
      anicodeService.save(anime, ep)
    } else {
      view.renderNotFoundEpisode(anime, ep)
    }
  }

  def displayEpisodeList(id: Int): Unit = {
    val historyList = anicodeService.getHistoryListByAnimeId(id).reverse.take(3)
    view.renderHistoryList(historyList)
  }

  def displayAnimeList(): Unit = {
    val animeList = anicodeService.getAnimeList
    view.renderAnimeList(animeList)
  }
}
