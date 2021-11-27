import entity.{Anime, History}
import repository.{AnimeRepository, HistoryRepository}

import java.util.Calendar
import scala.collection.JavaConverters.asScalaBufferConverter

object AnicodeService {
  def getAnimeList: Seq[Anime] = AnimeRepository.getAnimeRepository.getAnimeList.asScala

  def getAnimeById(id: Int): Option[Anime] = {
    val anime = AnimeRepository.getAnimeRepository.getAnimeById(id)
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
    HistoryRepository.getHistoryRepository.save(anime, history)
  }
}
