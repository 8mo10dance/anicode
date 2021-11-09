package entity;

import java.util.Date;

public class History {
  public final int ep;
  public final Date createdAt;

  public History(int ep, Date createdAt) {
    this.ep = ep;
    this.createdAt = createdAt;
  }
}
