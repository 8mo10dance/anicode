package repository;

import java.io.File;
import java.io.FileNotFoundException;

public class AnimeRepository {
  private final File rootDirectory;
  private static AnimeRepository animeRepository;

  private AnimeRepository(File rootDirectory) {
    this.rootDirectory = rootDirectory;
  }

  public List<Anime> getAnimeList() throws FileNotFoundException, ParseException {
    var animeList = new ArrayList<Anime>();
    for (File file: rootDirectory.listFiles()) {
      if (file.isDirectory()) {
        animeList.add(createAnime(file, HistoryRepository.getHistoryRepository().getHistoryListByAnimeName(file.getName())));
      }
    }

    return animeList;
  }

  public Anime getAnimeById(int id) throws FileNotFoundException, ParseException {
    return getAnimeList().get(id - 1);
  }

  public static void createAnimeRepository(String directoryPath) throws Exception {
    var directory = new File(directoryPath);
    if (!directory.exists()) throw new FileNotFoundException("ディレクトリではありません。");
    if (!directory.isDirectory()) throw new Exception("ディレクトリではありません。");

    AnimeRepository.animeRepository = new AnimeRepository(directory);
  }

  public static AnimeRepository getAnimeRepository() {
    return AnimeRepository.animeRepository;
  }
}
