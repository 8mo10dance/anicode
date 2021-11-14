import entity.Anime;
import entity.History;
import repository.AnimeRepository;

import java.text.SimpleDateFormat;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Random;

/** Anicode2.4 -Unicorn-
 * 2015/10/30 launch
 *  */
public class AnicodeCLI extends Anicode {
  public void displayAnimeList() throws FileNotFoundException, ParseException {
		int n = 1;
		for (Anime anime : AnimeRepository.getAnimeRepository().getAnimeList()) {
			System.out.println(n + " : " + anime.name + " latestEps: " + anime.getLatestHistory().map(h -> h.ep).orElse(0));
			n++;
		}
	}

	public void getEpisodeList(int id) throws FileNotFoundException, ParseException {
		getHistoriesByAnimeId(id, 3).forEach(h -> System.out.println(formatHistory(h)));
	}

	public void normalPlay(int id, int ep) throws FileNotFoundException, ParseException {
		var pathOpt = getAnimeFilePath(id, ep);
		if (pathOpt.isPresent()) {
			Player.getPlayer().play(pathOpt.get());
			save(id, ep);
		} else {
			System.out.println("NO SUCH ANIME or EPISODE !");
		}
	}

    public void randomPlay() throws FileNotFoundException, ParseException {
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
		Player.getPlayer().play(anime.getAnimeFilePath(ep).get());
		save(anime, ep);
    }

    public void sequentialPlay() throws FileNotFoundException, ParseException {
		var animeOpt = getLastWatchedAnime();
		var nextEpOpt = animeOpt.flatMap(a -> a.getNextEpisode());
		var pathOpt = nextEpOpt.flatMap(ep -> animeOpt.flatMap(anime -> anime.getAnimeFilePath(ep)));
		if (pathOpt.isPresent()) {
			System.out.println("now play " + nextEpOpt.get());
			Player.getPlayer().play(pathOpt.get());
			save(animeOpt.get(), nextEpOpt.get());
		} else {
			System.out.println("NO NEXT EPISODE");
		}
    }

  private String formatHistory(History history) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    return sdf.format(history.createdAt) + ":" + history.ep;
  }
}
