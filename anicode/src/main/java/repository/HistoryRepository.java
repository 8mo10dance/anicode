package repository;

import java.io.File;
import java.io.FileNotFoundException;

public class HistoryRepository {
  private final File rootDirectory;
  private HistoryRepository historyRepository;

  private HistoryRepository(File rootDirectory) {
    this.rootDirectory = rootDirectory;
  }

  public static HistoryRepository createHistoryRepository(String recordPath) throws Exception {
    var rootDirectory = new File(recordPath);
    if (!rootDirectory.exists()) throw new FileNotFoundException("ディレクトリが存在しません。");
    if (!rootDirectory.isDirectory()) throw new Exception("ディレクトリが存在しません。");

    return new HistoryRepository(rootDirectory);
  }

  public static HistoryRepository getHistoryRepository() {
    return HistoryRepository.historyRepository;
  }
}
