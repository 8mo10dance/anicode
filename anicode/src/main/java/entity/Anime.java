package entity;

import java.util.List;

public class Anime {
  public final String name;
  public final List<Episode> episodeList;

  public Anime(String name, List<Episode> episodeList) {
    this.name = name;
    this.episodeList = episodeList;
  }
}
