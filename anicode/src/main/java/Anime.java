import java.io.File;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Anime {

    private File rootDirectory;
    private List<History> histories;

    public Anime(File rootDirectory, List<History> histories) {
        this.rootDirectory = rootDirectory;
        this.histories = histories;
    }

    public String getName() {
        return rootDirectory.getName();
    }

    public List<File> getEpisodeFiles() {
        if (rootDirectory == null || !rootDirectory.exists()) {
            return List.of();
        }

        Pattern p = Pattern.compile(".+[mp4|mkv]");
        return Arrays
                .stream(Objects.requireNonNull(rootDirectory.listFiles()))
                .filter(f -> p.matcher(f.getName()).find())
                .sorted()
                .collect(Collectors.toList());
    }

    public List<History> getHistories() {
        return histories;
    }

    public Optional<File> getAnimeFilePath(int ep) {
        if (containsEpisode(ep)) {
            return Optional.of(getEpisodeFiles().get(ep - 1));
        }

        return Optional.empty();
    }

    public Optional<Integer> getLatestEpisode() {
        if (histories.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(histories.get(histories.size() - 1).getEp());
    }

    public Optional<Integer> getNextEpisode() {
        var currentEpisode = getLatestEpisode().orElse(0);
        var nextEpisode = currentEpisode + 1;

        if (containsEpisode(nextEpisode)) {
            return Optional.of(nextEpisode);
        } else {
            return Optional.empty();
        }
    }

    public boolean isCompleted() {
        return getEpisodeFiles().size() == getLatestEpisode().orElse(0);
    }

    private boolean containsEpisode(int ep) {
        return ep > 0 && ep <= getEpisodeFiles().size();
    }

}
