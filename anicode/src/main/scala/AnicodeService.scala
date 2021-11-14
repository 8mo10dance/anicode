import repository.AnimeRepository

import collection.JavaConverters._
import scala.util.Random

case class AnicodeService (anicode: Anicode) {
  def randomPlay(): Unit = {
    val animeList = anicode.getOnGoingAnimeList.asScala.toSeq
    animeList match {
      case Nil => {
        println("no ongoing anime")
      }
      case _ => {
        val index = Random.nextInt(animeList.length)
        val anime = animeList(index)
        val ep = anime.getNextEpisode.get()
        println(s"now play ${ep}")
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
          println(s"now play ${ep}")
          Player.getPlayer.play(pathOpt.get())
          anicode.save(anime, ep)
        } else {
          println("NO NEXT EPISODE")
        }
      } else {
        println("NO NEXT EPISODE")
      }
    } else {
      println("NO NEXT EPISODE")
    }
  }

  def normalPlay(id: Int, ep: Int): Unit = {
    val pathOpt = anicode.getAnimeFilePath(id, ep)
    if (pathOpt.isPresent) {
      Player.getPlayer.play(pathOpt.get())
      anicode.save(id, ep)
    } else {
      println("NO SUCH ANIME or EPISODE !")
    }
  }

  def getEpisodeList(id: Int): Unit = {
    val historyList = anicode.getHistoriesByAnimeId(id, 3).asScala
    historyList.foreach { println }
  }

  def displayAnimeList(): Unit = {
    val animeList = AnimeRepository.getAnimeRepository.getAnimeList.asScala
    animeList.zipWithIndex.foreach {
      case (anime, index) => {
        val id = index + 1
        val latestHistoryOpt = anime.getLatestHistory
        if (latestHistoryOpt.isPresent) {
          println(s"${id} : ${anime.name} latestEps: ${latestHistoryOpt.get().ep}")
        }
      }
    }
  }
}
