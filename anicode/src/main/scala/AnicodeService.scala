import entity.{Anime, History}
import repository.{AnimeRepository, HistoryRepository}

import java.util.Calendar
import scala.collection.JavaConverters.asScalaBufferConverter

case class AnicodeService(animeRepository: AnimeRepository, historyRepository: HistoryRepository) {
  def getAnimeList: Seq[Anime] = animeRepository.getAnimeList.asScala

  def getAnimeById(id: Int): Option[Anime] = {
    val anime = animeRepository.getAnimeById(id)
    if (anime == null) {
      None
    } else {
      Some(anime)
    }
  }

  def getOnGoingAnimeList: Seq[Anime] = getAnimeList.filter(_.isOnGoing)

  def getLastWatchedAnime: Anime = getAnimeList.maxBy(_.getUpdatedAt)

  def getHistoryListByAnimeId(id: Int): Seq[History] = getAnimeById(id) match {
    case Some(anime) => anime.historyList.asScala
    case None => Seq.empty
  }

  def save(anime: Anime, ep: Int): Unit = {
    val c = Calendar.getInstance()
    val history = new History(ep, c.getTime)
    historyRepository.save(anime, history)
  }
}
