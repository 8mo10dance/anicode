object AnicodeClient {
  def apply(profile: String): AnicodeClient = {
    new AnicodeClient(AnicodeManage.apply(profile))
  }
}

case class AnicodeClient(manage: AnicodeManage) {
  def dispatch(action: AnicodeAction): Unit = action match {
    case GetAnimeList => manage.getAnimeList()
    case GetEpisodeList(id) => manage.getEpisodeList(id)
    case NormalPlay(id, ep) => manage.normalPlay(id, ep)
    case SequentialPlay => manage.sequentialPlay()
    case RandomPlay => manage.randomPlay()
  }
}