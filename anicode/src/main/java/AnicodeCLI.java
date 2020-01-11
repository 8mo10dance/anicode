import java.util.Random;

/** Anicode2.4 -Unicorn-
 * 2015/10/30 launch
 *  */
public class AnicodeCLI extends Anicode {

	private Player player;

	public AnicodeCLI(String animeDirPath, String recordDirPath, String playerPath) {
		super(animeDirPath, recordDirPath);
		if (playerPath.equals("mock")) {
			this.player = new MockPlayer();
		} else {
			this.player = new ExternalPlayer(playerPath);
		}
	}

  public void displayAnimeList() {
		int n = 1;
		for (Anime anime : getAnimeList()) {
			System.out.println(n + " : " + anime.getName() + " latestEps: " + anime.getLatestEpisode().orElse(0));
			n++;
		}
	}

	public void getEpisodeList(int id) {
		getHistoriesByAnimeId(id, 3).forEach(System.out::println);
	}

	public void normalPlay(int id, int ep) {
		var pathOpt = getAnimeFilePath(id, ep);
		if (pathOpt.isPresent()) {
			player.play(pathOpt.get());
			save(id, ep);
		} else {
			System.out.println("NO SUCH ANIME or EPISODE !");
		}
	}

    public void randomPlay() {
		var animeList = getOnGoingAnimeList();
		if (animeList.isEmpty()) {
			System.out.println("no ongoing anime");
			return;
		}

		var random = new Random();
		int index = random.nextInt(animeList.size());
		var anime = animeList.get(index);
		int ep = anime.getNextEpisode().get();
		System.out.println("now play " + ep);
		player.play(anime.getAnimeFilePath(ep).get());
		save(anime, ep);
    }

    public void sequentialPlay() {
		var animeOpt = getLastWatchedAnime();
		var nextEpOpt = animeOpt.flatMap(Anime::getNextEpisode);
		var pathOpt = nextEpOpt.flatMap(ep -> animeOpt.flatMap(anime -> anime.getAnimeFilePath(ep)));
		if (pathOpt.isPresent()) {
			System.out.println("now play " + nextEpOpt.get());
			player.play(pathOpt.get());
			save(animeOpt.get(), nextEpOpt.get());
		} else {
			System.out.println("NO NEXT EPISODE");
		}
    }
}
