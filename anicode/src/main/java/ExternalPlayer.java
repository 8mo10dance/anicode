import java.io.File;
import java.io.IOException;

public class ExternalPlayer extends Player {
    private final String playerCmd;

  private ExternalPlayer(String playerCmd) {
    super();
    this.playerCmd = playerCmd;
  }

    public void play(File target) {
        Runtime rt = Runtime.getRuntime();
        String[] str = new String[2];
        str[0] = playerCmd;
        str[1] = target.getPath();
        try {
            rt.exec(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createExternalPlayer(String playerCmd) {
      Player.player = new ExternalPlayer(playerCmd);
    }
}
