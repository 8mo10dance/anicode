package repository;

import entity.Anime;
import entity.Episode;
import entity.History;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

  public static AnimeRepository createAnimeRepository(String directoryPath) throws Exception {
    var directory = new File(directoryPath);
    if (!directory.exists()) throw new FileNotFoundException("ディレクトリではありません。");
    if (!directory.isDirectory()) throw new Exception("ディレクトリではありません。");

    AnimeRepository.animeRepository = new AnimeRepository(directory);
    return animeRepository;
  }

  public static AnimeRepository getAnimeRepository() {
    return AnimeRepository.animeRepository;
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
