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
public class AnicodeMain {

	static final String PROFILE = "/home/tomoya/.anicode_profile";
	private static Anicode anicode = new Anicode();

	public static void setting() {
		try {
			BufferedReader config = new BufferedReader(new FileReader(new File(PROFILE)));
			anicode.setPlayer(config.readLine().split("=")[1]);
			anicode.setAnimeDir(config.readLine().split("=")[1]);
			anicode.setRecordDir(config.readLine().split("=")[1]);
			config.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	public static void selectAnime() {
		anicode.readAnimeList();
		int n = 1;
		for (File f : anicode.getAnimeList()) {
			System.out.println(n + " : " + f.getName());
			n++;
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String str;
		System.out.println("何見んの？");
		try {
			str = br.readLine();
			Pattern p = Pattern.compile("[0-9]+");
			while (!p.matcher(str).find())
				System.out.println("数字のみ入力");
			anicode.selectAnime(Integer.parseInt(str));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void selectEpisodes() {
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
		System.out.println("何話見んの？");
		try {
			str = br.readLine();
			Pattern p = Pattern.compile("[0-9]+");
			while (!p.matcher(str).find())
				System.out.println("数字のみ入力");
			anicode.selectEpisodes(Integer.parseInt(str));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void randomPlay() {
		anicode.readAnimeList();
		anicode.randomPlay();
	}

	public static void sequentialPlay() {
		anicode.readAnimeList();
		anicode.sequentialPlay();
	}

	public static void play() {
		anicode.play();
		anicode.save();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		setting();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			System.out.println("見る[y/N]");
			try {
				if (!br.readLine().equals("y"))
					break;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("再生モード[ランダム:r/シーケンシャル:s/ノーマル:n]");
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
