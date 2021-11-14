import java.io.File;

public class MockPlayer extends Player {
  private MockPlayer() {
    super();
  }

    @Override
    public void play(File target) {
        System.out.println("Now playing " + target.getAbsolutePath());
    }

  public static void createMockPlayer() {
    Player.player = new MockPlayer();
  }
}
