import java.io.File;

public class MockPlayer implements Player {

    @Override
    public void play(File target) {
        System.out.println("Now playing " + target.getAbsolutePath());
    }
}
