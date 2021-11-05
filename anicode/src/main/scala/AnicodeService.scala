case class AnicodeService (anicode: AnicodeCLI) {
  def randomPlay(): Unit = {
    anicode.randomPlay()
  }

  def sequentialPlay(): Unit = {
    anicode.sequentialPlay()
  }

  def normalPlay(id: Int, ep: Int): Unit = {
    anicode.normalPlay(id, ep)
  }

  def getEpisodeList(id: Int): Unit = {
    anicode.getEpisodeList(id)
  }

  def displayAnimeList(): Unit = {
    anicode.displayAnimeList()
  }
}
