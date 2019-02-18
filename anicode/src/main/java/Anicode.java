import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/** anicode ver2.4 -Unicorn-
 * 2015/10/30 launch */
public class Anicode {

    private List<Anime> animeList;
	private Record record;

    public Anicode(String animeDirPath, String recordPath) {
		var animeDir = new File(animeDirPath);
		this.record = new Record(new File(recordPath));
		this.animeList = Arrays
				.stream(animeDir.listFiles())
				.filter(File::isDirectory)
				.map(f -> new Anime(f, record.getHistoriesByAnimeName(f.getName())))
				.collect(Collectors.toList());
    }

	public List<String> getAnimeNameList() {
		return animeList.stream().map(Anime::getName).collect(Collectors.toList());
	}

	public List<History> getHistoriesByAnimeId(int id, int limit) {
		var anime = getAnimeById(id);
		var histories = anime.getHistories();
		var latestHistories = new ArrayList<History>();
		for (int i = 0; i < limit; i++) {
			if (i >= histories.size()) {
				return latestHistories;
			}
			latestHistories.add(histories.get(histories.size() - i - 1));
		}

		return latestHistories;
	}

	public Optional<File> getAnimeFilePath(int id, int ep) {
		var anime = getAnimeById(id);

		return anime.getAnimeFilePath(ep);
	}

	public void save(Anime anime, int ep) {
		Calendar c = Calendar.getInstance();
		var history = new History(ep, c.getTime());
		record.updateHistory(anime.getName(), history);
	}

    public void save(int id, int ep) {
		var anime = getAnimeById(id);
		save(anime, ep);
    }

    public Optional<Anime> getLastWatchedAnime() {
		return animeList.stream().max(Comparator.comparing(Anime::getUpdatedAt));
	}

	public List<Anime> getOnGoingAnimeList() {
		return animeList.stream().filter(Anime::isOnGoing).collect(Collectors.toList());
	}

    private Anime getAnimeById(int id) {
		return animeList.get(id - 1);
	}

}
