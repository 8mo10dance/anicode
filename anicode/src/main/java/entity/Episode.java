package entity;

import java.io.File;

public class Episode {
  public final int ep;
  public final File file;

  public Episode(int ep, File file) {
    this.ep = ep;
    this.file = file;
  }
}
