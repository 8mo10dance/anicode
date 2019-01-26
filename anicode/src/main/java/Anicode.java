import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/** anicode ver2.4 -Unicorn-
 * 2015/10/30 launch */
public class Anicode {

    private final File player;
    private final File animeDir;
    private final File recordDir;
    private List<File> animeList = new ArrayList<File>();
    private List<File> episodes = new ArrayList<File>();
    private File record = null;
    private int ep = 0;

    public Anicode(String playerPath, String animeDirPath, String recordDirPath) {
	this.player = new File(playerPath);
	this.animeDir = new File(animeDirPath);
	this.recordDir = new File(recordDirPath);	
    }

    public void readAnimeList() {
	animeList.clear();
	for (File f : animeDir.listFiles()) {
	    if (f.isDirectory())
		animeList.add(f);
	}
    }

    public List<File> getAnimeList() {
	return animeList;
    }

    public void selectAnime(int n) {
	if (n <= 0 || n > animeList.size())
	    return;
	File anime = animeList.get(n - 1);
	readEpisodes(anime);
	record = null;
	for (File f : recordDir.listFiles()) {
	    if (f.getName().equals(anime.getName() + ".txt"))
		record = f;
	}
	if (record == null) {
	    record = new File(recordDir.getPath() + "/" + anime.getName() + ".txt");
	}
    }

    public void nextEpisode(File anime) {
	if (record == null) {
	    record = new File(recordDir.getPath() + "/" + anime.getName() + ".txt");
	    this.ep = 1;
	    return;
	}
	try {
	    BufferedReader br = new BufferedReader(new FileReader(record));
	    String line;
	    String next = null;
	    while ((line = br.readLine()) != null) {
		next = line;
	    }
	    br.close();
	    if (next == null)
		this.ep = 0;
	    else
		this.ep = Integer.parseInt(next.split(":")[1]) + 1;
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public void randomPlay() {
	Random rnd = new Random();
	File anime;
	while (true) {
	    anime = animeList.get(rnd.nextInt(animeList.size()));
	    record = null;
	    for (File f : recordDir.listFiles()) {
		if (f.getName().equals(anime.getName() + ".txt"))
		    record = f;
	    }
	    readEpisodes(anime);
	    nextEpisode(anime);
	    if (ep <= episodes.size())
		break;
	}
    }

    public void sequentialPlay() {
	if (record == null)
	    return;
	File anime = null;
	for (File f : animeDir.listFiles()) {
	    if (record.getName().equals(f.getName() + ".txt")) {
		anime = f;
	    }
	}
	if (anime == null)
	    return;
	readEpisodes(anime);
	nextEpisode(anime);
	if (ep > episodes.size())
	    return;
    }

    public void readEpisodes(File anime) {
	episodes.clear();
	if (!anime.exists())
	    return;
	Pattern p = Pattern.compile(".+[mp4|mkv]");
	for (File f : anime.listFiles()) {
	    if (p.matcher(f.getName()).find())
		episodes.add(f);
	}
	Collections.sort(episodes);
    }

    public File getRecord() {
	return record;
    }

    public void selectEpisodes(int ep) {
	if (ep <= 0 || ep > episodes.size())
	    return;
	this.ep = ep;
    }

    public void play() {
	if (ep == 0)
	    return;
	Runtime rt = Runtime.getRuntime();
	String[] str = new String[2];
	str[0] = player.getPath();
	str[1] = episodes.get(ep - 1).getPath();
	try {
	    rt.exec(str);
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public void save() {
	if (ep == 0)
	    return;
	Calendar c = Calendar.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	try {
	    PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(record, true)));
	    pw.println(sdf.format(c.getTime()) + ":" + ep);
	    pw.close();
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

}
