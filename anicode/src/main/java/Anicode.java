import entity.Episode;
import entity.History;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/** anicode ver2.4 -Unicorn-
 * 2015/10/30 launch */
public class Anicode {

    private List<entity.Anime> animeList;
	private Record record;

    public Anicode(String animeDirPath, String recordPath) {
		var animeDir = new File(animeDirPath);
		this.record = new Record(new File(recordPath));
		this.animeList = Arrays
				.stream(animeDir.listFiles())
				.filter(File::isDirectory)
				.map(f -> createAnime(f, record.getHistoriesByAnimeName(f.getName())))
				.collect(Collectors.toList());
    }

  public List<entity.Anime> getAnimeList() {
    return animeList;
  }

	public List<History> getHistoriesByAnimeId(int id, int limit) {
		var anime = getAnimeById(id);
		var histories = anime.historyList;
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

	public void save(entity.Anime anime, int ep) {
		Calendar c = Calendar.getInstance();
		var history = new History(ep, c.getTime());
		record.updateHistory(anime.name, history);
	}

    public void save(int id, int ep) {
		var anime = getAnimeById(id);
		save(anime, ep);
    }

    public Optional<entity.Anime> getLastWatchedAnime() {
		return animeList.stream().max(Comparator.comparing(a -> a.getUpdatedAt()));
	}

	public List<entity.Anime> getOnGoingAnimeList() {
		return animeList.stream().filter(a -> a.isOnGoing()).collect(Collectors.toList());
	}

    private entity.Anime getAnimeById(int id) {
		return animeList.get(id - 1);
	}

  private entity.Anime createAnime(File rootDirectory, List<History> histories) {
    String animeName = rootDirectory.getName();
    Pattern p = Pattern.compile(".+[mp4|mkv]");
    List<File> episodeFileList = rootDirectory == null || !rootDirectory.exists() ? List.of() : Arrays.stream(Objects.requireNonNull(rootDirectory.listFiles())).filter(f -> p.matcher(f.getName()).find()).sorted().collect(Collectors.toList());
    List<Episode> episodeList = new ArrayList<>();
    int ep = 1;
    for (File file: episodeFileList) {
      episodeList.add(new Episode(ep, file));
      ep++;
    }

    return new entity.Anime(animeName, episodeList, histories);
  }

}
