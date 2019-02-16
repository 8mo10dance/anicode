import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

/** Anicode2.4 -Unicorn-
 * 2015/10/30 launch
 *  */
public class AnicodeClient {

	public static AnicodeClient apply(String profile) {
		try {
			BufferedReader config = new BufferedReader(new FileReader(new File(profile)));
			var playerPath = config.readLine().split("=")[1];
			var animeDirPath = config.readLine().split("=")[1];
			var recordDirPath = config.readLine().split("=")[1];
			config.close();
			Anicode anicode = Anicode.apply(animeDirPath, recordDirPath);
			Player player = new Player(playerPath);
			return new AnicodeClient(anicode, player);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
    }

    private Anicode anicode;
	private Player player;

	public AnicodeClient(Anicode anicode, Player player) {
    	this.anicode = anicode;
    	this.player = player;
    }

	public void start() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("play[y/N]");
		try {
			var s = br.readLine();
			if (s.equals("y")) {
				selectMode();
			} else {
				System.exit(0);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public void normalPlay() {
		displayAnimeList();
		int id = selectAnimeId();
		displayEpisodeList(id);
		int ep = selectEpisode();
		var pathOpt = anicode.getAnimeFilePath(id, ep);
		if (pathOpt.isPresent()) {
			player.play(pathOpt.get());
			anicode.save(id, ep);
		} else {
			System.out.println("NO SUCH ANIME or EPISODE !");
		}
		start();
	}

    public void randomPlay() {
		// TODO
    }

    public void sequentialPlay() {
		var animeOpt = anicode.getLastWatchedAnime();
		var nextEpOpt = animeOpt.flatMap(Anime::getNextEpisode);
		var pathOpt = nextEpOpt.flatMap(ep -> animeOpt.flatMap(anime -> anime.getAnimeFilePath(ep)));
		if (pathOpt.isPresent()) {
			System.out.println("now play " + nextEpOpt.get());
			player.play(pathOpt.get());
			anicode.save(animeOpt.get(), nextEpOpt.get());
		} else {
			System.out.println("NO NEXT EPISODE");
		}
    }


    public void selectMode() {
		System.out.println("mode[random:r/sequential:s/normal:n]");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			String str = br.readLine();
			switch (str) {
				case "r":
					randomPlay();
					break;
				case "s":
					sequentialPlay();
					break;
				default:
					normalPlay();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int selectAnimeId() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String str;
		System.out.println("choose anime");
		try {
			str = br.readLine();
			Pattern p = Pattern.compile("[0-9]+");
			while (!p.matcher(str).find()) {
				System.out.println("require number input");
			}
			return Integer.parseInt(str);
		} catch (IOException e) {
			System.out.println("your input is invalid");
		}
		return selectAnimeId();
	}

	public int selectEpisode() {
		var br = new BufferedReader(new InputStreamReader(System.in));
		String str;
		System.out.println("choose episode");
		try {
			str = br.readLine();
			Pattern p = Pattern.compile("[0-9]+");
			while (!p.matcher(str).find()) {
				System.out.println("require number input");
			}
			return Integer.parseInt(str);
		} catch (IOException e) {
			System.out.println("your input is invalid");
		}
		return selectAnimeId();
	}

	private void displayAnimeList() {
		int n = 1;
		for (String animeName : anicode.getAnimeNameList()) {
			System.out.println(n + " : " + animeName);
			n++;
		}
	}

	private void displayEpisodeList(int id) {
		anicode.getHistoriesByAnimeId(id, 3).forEach(System.out::println);
	}
}
