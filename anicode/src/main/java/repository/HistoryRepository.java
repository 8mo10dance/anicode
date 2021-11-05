package repository;

import entity.Anime;
import entity.History;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HistoryRepository {
  private final File rootDirectory;
  private static HistoryRepository historyRepository;

  private HistoryRepository(File rootDirectory) {
    this.rootDirectory = rootDirectory;
  }

  public List<History> getHistoryListByAnimeName(String animeName) throws FileNotFoundException, ParseException {
    var historyFileOpt = getHistoryFile(animeName);
    if (historyFileOpt.isEmpty()) {
      return List.of();
    }
    var historyFile = historyFileOpt.get();
    var historyList = new ArrayList<History>();
    for (History history: readHistory(historyFile)) {
      historyList.add(history);
    }

    return historyList;
  }

  public void save(Anime anime, History history) throws FileNotFoundException {
    var historyFile = getHistoryFile(anime.name).orElse(new File(rootDirectory.getPath() + "/" + anime.name + ".txt"));
    PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(historyFile, true)));
    pw.println(formatHistory(history));
    pw.close();
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

  private Optional<File> getHistoryFile(String animeName) {
    return Arrays.stream(rootDirectory.listFiles()).filter(f -> f.getName().equals(animeName + ".txt")).findAny();
  }

  private List<History> readHistory(File animeHistoryFile) throws FileNotFoundException, ParseException {
    var br = new BufferedReader(new FileReader(animeHistoryFile));
    var historyList = new ArrayList<History>();
    for (String line: br.lines().collect(Collectors.toList())) {
      historyList.add(toHistory(line));
    }

    return historyList;
  }

  private History toHistory(String line) throws ParseException {
    var strs = line.split(":");
    var sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    var createdAt = sdf.parse(strs[0]);
    var ep = Integer.parseInt(strs[1]);

    return new History(ep, createdAt);
  }

  private String formatHistory(History history) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    return sdf.format(history.createdAt) + ":" + history.ep;
  }
}
