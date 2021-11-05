package entity;

import java.util.List;

public class Anime {
  public final String name;
  public final List<Episode> episodeList;
  public final List<History> historyList;

  public Anime(String name, List<Episode> episodeList, List<History> historyList) {
    this.name = name;
    this.episodeList = episodeList;
    this.historyList = historyList;
  }
}
