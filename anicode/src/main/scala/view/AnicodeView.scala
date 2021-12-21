package view

import entity.{Anime, History}

trait AnicodeView {
  def renderAnimeList(animeList: Seq[Anime]): Unit

  def renderHistoryList(historyList: Seq[History]): Unit

  def renderPlay(anime: Anime, ep: Int): Unit

  def renderNotFoundEpisode(anime: Anime, ep: Int): Unit

  def renderNotFoundOnGoingAnime: Unit

  def renderNotFoundNextEpisode: Unit
}
