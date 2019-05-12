import java.io.File;
import java.io.IOException;

public class ExternalPlayer implements Player {
    private final String playerCmd;

    public ExternalPlayer(String playerCmd) {
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
}
