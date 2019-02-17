trait AnicodeAction

object GetAnimeList extends AnicodeAction

case class GetEpisodeList(id: Int) extends AnicodeAction

case class NormalPlay(id: Int, ep: Int) extends AnicodeAction

object SequentialPlay extends AnicodeAction

object RandomPlay extends AnicodeAction
