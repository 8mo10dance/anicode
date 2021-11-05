import entity.History;
import repository.AnimeRepository;
import repository.HistoryRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/** anicode ver2.4 -Unicorn-
 * 2015/10/30 launch */
public class Anicode {
	public List<History> getHistoriesByAnimeId(int id, int limit) throws FileNotFoundException, ParseException {
		var anime = AnimeRepository.getAnimeRepository().getAnimeById(id);
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

	public Optional<File> getAnimeFilePath(int id, int ep) throws FileNotFoundException, ParseException {
		var anime = AnimeRepository.getAnimeRepository().getAnimeById(id);

		return anime.getAnimeFilePath(ep);
	}

	public void save(entity.Anime anime, int ep) throws FileNotFoundException {
		Calendar c = Calendar.getInstance();
		var history = new History(ep, c.getTime());
		HistoryRepository.getHistoryRepository().save(anime, history);
	}

    public void save(int id, int ep) throws FileNotFoundException, ParseException {
		var anime = AnimeRepository.getAnimeRepository().getAnimeById(id);
		save(anime, ep);
    }

    public Optional<entity.Anime> getLastWatchedAnime() throws FileNotFoundException, ParseException {
		return AnimeRepository.getAnimeRepository().getAnimeList().stream().max(Comparator.comparing(a -> a.getUpdatedAt()));
	}

	public List<entity.Anime> getOnGoingAnimeList() throws FileNotFoundException, ParseException {
		return AnimeRepository.getAnimeRepository().getAnimeList().stream().filter(a -> a.isOnGoing()).collect(Collectors.toList());
	}

}
