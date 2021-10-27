import entity.Episode;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Anime {
    private List<History> histories;
  private final entity.Anime anime;

    public Anime(File rootDirectory, List<History> histories) {
      String animeName = rootDirectory.getName();
      Pattern p = Pattern.compile(".+[mp4|mkv]");
      List<File> episodeFileList = rootDirectory == null || !rootDirectory.exists() ? List.of() : Arrays.stream(Objects.requireNonNull(rootDirectory.listFiles())).filter(f -> p.matcher(f.getName()).find()).sorted().collect(Collectors.toList());
      List<Episode> episodeList = new ArrayList<>();
      int ep = 1;
      for (File file: episodeFileList) {
        episodeList.add(new Episode(ep, file));
        ep++;
      }
      this.anime = new entity.Anime(animeName, episodeList);
      this.histories = histories;
    }

    public String getName() {
      return anime.name;
    }

    public List<File> getEpisodeFiles() {
      return anime.episodeList.stream().map(e -> e.file).collect(Collectors.toList());
    }

    public List<History> getHistories() {
        return histories;
    }

    public Optional<File> getAnimeFilePath(int ep) {
        if (containsEpisode(ep)) {
            return Optional.of(getEpisodeFiles().get(ep - 1));
        }

        return Optional.empty();
    }

    public Optional<History> getLatestHistory() {
        return histories.stream().max(Comparator.comparing(History::getCreatedAt));
    }

    public Date getUpdatedAt() {
        return getLatestHistory().map(History::getCreatedAt).orElse(History.defaultCreatedAt());
    }

    public Optional<Integer> getLatestEpisode() {
        return getLatestHistory().map(History::getEp);
    }

    public Optional<Integer> getNextEpisode() {
        var currentEp = getLatestEpisode().orElse(0);
        var nextEp = currentEp + 1;
        if (containsEpisode(nextEp)) {
            return Optional.of(nextEp);
        }

        return Optional.empty();
    }

    public boolean isOnGoing() {
        return getNextEpisode().isPresent();
    }

    private boolean containsEpisode(int ep) {
        return ep > 0 && ep <= getEpisodeFiles().size();
    }

}
