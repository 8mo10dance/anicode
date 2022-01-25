package view

import entity.{Anime, History}

object CLIView extends AnicodeView {
  override def renderAnimeList(animeList: Seq[Anime]): Unit = {
    animeList.zipWithIndex.foreach {
      case (anime, index) => {
        val id = index + 1
        val latestHistoryOpt = anime.getLatestHistory
        val latestEpisode = if (latestHistoryOpt.isPresent()) latestHistoryOpt.get().ep else 0
        println(s"${id} : ${anime.name} latestEps: ${latestEpisode}")
      }
    }
  }

  override def renderHistoryList(historyList: Seq[History]): Unit = {
    historyList.foreach { h => println(s"Episode ${h.ep}") }
  }

  override def renderPlay(anime: Anime, ep: Int): Unit = {
    println(s"${anime.name} 第${ep}話を再生します。")
  }

  override def renderNotFoundOnGoingAnime: Unit = {
    println("視聴中のアニメは見つかりませんでした。")
  }

  override def renderNotFoundNextEpisode: Unit = {
    println("次の回はありませんでした。")
  }

  override def renderNotFoundEpisode(anime: Anime, ep: Int): Unit = {
    println(s"${anime.name} 第${ep}話は見つかりませんでした。")
  }
}
