import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

/** Anicode2.4 -Unicorn-
 * 2015/10/30 launch
 *  */
public class AnicodeManage {

    public static AnicodeManage createAnicodeManage(String profile) {
	Anicode anicode = new Anicode();
	try {
	    BufferedReader config = new BufferedReader(new FileReader(new File(profile)));
	    anicode.setPlayer(config.readLine().split("=")[1]);
	    anicode.setAnimeDir(config.readLine().split("=")[1]);
	    anicode.setRecordDir(config.readLine().split("=")[1]);
	    config.close();

	    return new AnicodeManage(anicode);
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return null;
    }

    private Anicode anicode;

    public AnicodeManage(Anicode anicode) {
	this.anicode = anicode;
    }
    
    public void selectAnime() {
	anicode.readAnimeList();
	int n = 1;
	for (File f : anicode.getAnimeList()) {
	    System.out.println(n + " : " + f.getName());
	    n++;
	}
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	String str;
	System.out.println("choose anime");
	try {
	    str = br.readLine();
	    Pattern p = Pattern.compile("[0-9]+");
	    while (!p.matcher(str).find())
		System.out.println("require number input");
	    anicode.selectAnime(Integer.parseInt(str));
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public void selectEpisodes() {
	File record = anicode.getRecord();
	BufferedReader br;
	try {
	    br = new BufferedReader(new FileReader(record));
	    int n = 0;
	    String line;
	    while ((line = br.readLine()) != null && n < 3)
		System.out.println(line);
	    br.close();
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	br = new BufferedReader(new InputStreamReader(System.in));
	String str;
	System.out.println("choose episode");
	try {
	    str = br.readLine();
	    Pattern p = Pattern.compile("[0-9]+");
	    while (!p.matcher(str).find())
		System.out.println("require number input");
	    anicode.selectEpisodes(Integer.parseInt(str));
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public void randomPlay() {
	anicode.readAnimeList();
	anicode.randomPlay();
    }

    public void sequentialPlay() {
	anicode.readAnimeList();
	anicode.sequentialPlay();
    }

    public void play() {
	anicode.play();
	anicode.save();
    }

    public void start() {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	while (true) {
	    System.out.println("play[y/N]");
	    try {
		if (!br.readLine().equals("y"))
		    break;
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    System.out.println("mode[random:r/sequential:s/normal:n]");
	    try {
		String str = br.readLine();
		if (str.equals("r"))
		    randomPlay();
		else if (str.equals("s"))
		    sequentialPlay();
		else {
		    selectAnime();
		    selectEpisodes();
		}
		play();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }

}
