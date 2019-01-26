object Main {
  def main(args: Array[String]) {
    val manage = AnicodeManage.createAnicodeManage(
      "/home/dexmon/.anicode_profile"
    )
    manage.start
  }
}
