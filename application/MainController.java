package application;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class MainController {
	FileInputStream FIS;
	BufferedInputStream BIS;

	private Player player;

	private long lastPosition;
	private long musicSize;
	private String fileLocation;

	/*flags*/
	private boolean isPlaying = false;
	private boolean wasPaused = false;

	public void Stop() {
		if(this.player != null) {
			this.player.close();
			this.isPlaying = false;
			this.wasPaused = false;
			this.lastPosition = 0;
			this.musicSize = 0;
		}
	}

	public void Pause() {
		if(this.player != null) {

			try {
				this.lastPosition = FIS.available();
				this.wasPaused = true; 
				this.player.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void PlayButton() {
		if(!this.isPlaying) {
			this.Play();
			this.isPlaying = true;
		}
		if(this.wasPaused) {
			this.Resume();
			this.wasPaused = false;
		}
	}

	public void Play() {
		String path = "/home/rafael/eclipse-workspace/MusicPlayer2/musics/Beck-HitInTheUsa.mp3";

		try {
			this.FIS = new FileInputStream(path);
			this.BIS = new BufferedInputStream(this.FIS);

			this.player = new Player(this.BIS);
			this.musicSize = this.FIS.available();

			this.fileLocation = path + "";
		} catch (JavaLayerException | IOException e) {

		}

		new Thread() {
			@Override
			public void run() {
				try {
					player.play();
					Stop();
				} catch (JavaLayerException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void Resume() {
		//String path = "/home/rafael/eclipse-workspace/MusicPlayer2/musics/Beck-HitInTheUsa.mp3";

		try {
			this.FIS = new FileInputStream(this.fileLocation);
			this.BIS = new BufferedInputStream(this.FIS);

			this.player = new Player(this.BIS);

			this.FIS.skip(this.musicSize - this.lastPosition);
		} catch (JavaLayerException | IOException e) {

		}

		new Thread() {
			@Override
			public void run() {
				try {
					player.play();
					Stop();
				} catch (JavaLayerException e) {
					e.printStackTrace();
				}
			}
		}.start();
		
	}
}
