import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Record {

    private final File rootDirectory;

    public Record(File rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public List<History> getHistoriesByAnimeName(String animeName) {
        return getHistoryFile(animeName).map(this::readHistory).orElse(List.of());
    }

    public void updateHistory(String animeName, History history) {
        var historyFile = getHistoryFile(animeName).orElse(new File(rootDirectory.getPath() + "/" + animeName + ".txt"));
        try {
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(historyFile, true)));
            pw.println(history.toString());
            pw.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private Optional<File> getHistoryFile(String animeName) {
        return Arrays.stream(rootDirectory.listFiles()).filter(f -> f.getName().equals(animeName + ".txt")).findAny();
    }

    private List<History> readHistory(File animeHistoryFile) {
        try {
            var br = new BufferedReader(new FileReader(animeHistoryFile));
            return br.lines().map(this::toHistory).collect(Collectors.toList());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return List.of();
    }

    private History toHistory(String line) {
        var strs = line.split(":");
        var sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            var createdAt = sdf.parse(strs[0]);
            var ep = Integer.parseInt(strs[1]);
            return new History(ep, createdAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
