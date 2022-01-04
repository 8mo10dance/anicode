case class AnicodeDispatcher(controller: AnicodeController) {
  def dispatch(action: AnicodeAction): Unit = action match {
    case GetAnimeList => controller.displayAnimeList()
    case GetEpisodeList(id) => controller.displayEpisodeList(id)
    case NormalPlay(id, ep) => controller.normalPlay(id, ep)
    case SequentialPlay => controller.sequentialPlay()
    case RandomPlay => controller.randomPlay()
  }
}
