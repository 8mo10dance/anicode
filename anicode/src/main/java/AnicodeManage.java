import java.util.Random;

/** Anicode2.4 -Unicorn-
 * 2015/10/30 launch
 *  */
public class AnicodeManage {

    private Anicode anicode;
	private Player player;

	public AnicodeManage(Anicode anicode, Player player) {
    	this.anicode = anicode;
    	this.player = player;
    }

    public void getAnimeList() {
		displayAnimeList();
	}

	public void getEpisodeList(int id) {
		displayEpisodeList(id);
	}

	public void normalPlay(int id, int ep) {
		var pathOpt = anicode.getAnimeFilePath(id, ep);
		if (pathOpt.isPresent()) {
			player.play(pathOpt.get());
			anicode.save(id, ep);
		} else {
			System.out.println("NO SUCH ANIME or EPISODE !");
		}
	}

    public void randomPlay() {
		var animeList = anicode.getOnGoingAnimeList();
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
		anicode.save(anime, ep);
    }

    public void sequentialPlay() {
		var animeOpt = anicode.getLastWatchedAnime();
		var nextEpOpt = animeOpt.flatMap(Anime::getNextEpisode);
		var pathOpt = nextEpOpt.flatMap(ep -> animeOpt.flatMap(anime -> anime.getAnimeFilePath(ep)));
		if (pathOpt.isPresent()) {
			System.out.println("now play " + nextEpOpt.get());
			player.play(pathOpt.get());
			anicode.save(animeOpt.get(), nextEpOpt.get());
		} else {
			System.out.println("NO NEXT EPISODE");
		}
    }

	private void displayAnimeList() {
		int n = 1;
		for (String animeName : anicode.getAnimeNameList()) {
			System.out.println(n + " : " + animeName);
			n++;
		}
	}

	private void displayEpisodeList(int id) {
		anicode.getHistoriesByAnimeId(id, 3).forEach(System.out::println);
	}
}
