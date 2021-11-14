import java.io.File;

public abstract class Player {
  protected static Player player;

  protected Player() {
    super();
  }

  public abstract void play(File target);

  public static Player getPlayer() {
    return Player.player;
  }
}
