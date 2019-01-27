object Main {
  def main(args: Array[String]) {
    val client = AnicodeClient.apply(
      "/home/dexmon/.anicode_profile"
    )
    client.start
  }
}
