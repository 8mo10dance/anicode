object Main {
  def parseOpt(args: Seq[String], boolOpts: Set[String], valueOpts: Map[String, String]): (Set[String], Map[String, String]) = {
    val optPattern = """-(\w+)""".r
    val valuePattern = """(\S+)""".r
    args match {
      case Nil => (boolOpts, valueOpts)
      case optPattern(opt) +: valuePattern(value) +: rest => parseOpt(rest, boolOpts, valueOpts updated (opt, value))
      case optPattern(opt) +: rest => parseOpt(rest, boolOpts + opt, valueOpts)
      case arg +: _ => throw new Exception(s"invalid args ${arg}")
    }
  }

  def getAction(boolOpts: Set[String], valueOpts: Map[String, String]): AnicodeAction = {
    if (boolOpts.contains("s")) {
      SequentialPlay
    } else if (boolOpts.contains("r")) {
      RandomPlay
    } else if (boolOpts.contains("l")) {
      GetAnimeList
    } else if (valueOpts.contains("id")) {
      if (valueOpts.contains("ep")) {
        NormalPlay(valueOpts.get("id").get.toInt, valueOpts.get("ep").get.toInt)
      } else {
        GetEpisodeList(valueOpts.get("id").get.toInt)
      }
    } else {
      GetAnimeList
    }
  }

  def main(args: Array[String]) {
    val (boolOpts, valueOpts) = parseOpt(args.toSeq, Set.empty, Map.empty)
    val profile = valueOpts.getOrElse("profile", ".anicode_profile")
    val client = AnicodeClient(profile)
    val action = getAction(boolOpts, valueOpts)
    client.dispatch(action)
  }
}
