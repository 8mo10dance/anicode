package entity;

import java.io.File;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Anime {
  public final String name;
  public final List<Episode> episodeList;
  public final List<History> historyList;

  public Anime(String name, List<Episode> episodeList, List<History> historyList) {
    this.name = name;
    this.episodeList = episodeList;
    this.historyList = historyList;
  }

  public Optional<Episode> getEpidodeByEp(int ep) {
    return episodeList.stream().filter(e -> e.ep == ep).findAny();
  }

  public Optional<History> getLatestHistory() {
    return historyList.stream().max(Comparator.comparing(h -> h.createdAt));
  }

  public Optional<File> getAnimeFilePath(int ep) {
    if (containsEpisode(ep)) {
      return Optional.of(episodeList.stream().map(e -> e.file).collect(Collectors.toList()).get(ep - 1));
    }

    return Optional.empty();
  }

  public Date getUpdatedAt() {
    var defaultHistoryCreatedAt=  new Date();
    defaultHistoryCreatedAt.setTime(0);

    return getLatestHistory().map(h -> h.createdAt).orElse(defaultHistoryCreatedAt);
  }

  public Optional<Integer> getNextEpisode() {
    var currentEp = getLatestHistory().map(h -> h.ep).orElse(0);
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
    return ep > 0 && ep <= episodeList.size();
  }
}
