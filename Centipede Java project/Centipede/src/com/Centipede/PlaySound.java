package com.Centipede;
import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.DataLine;


public class PlaySound {
 
    private final int BUFSIZE = 4096;
     
    public synchronized void play(String audioFilePath) {
    	new Thread(new Runnable() {
    		
    		public void run() {
		        File audioFile = new File(audioFilePath);
		        int read = -1;
		        byte[] bBuf = new byte[BUFSIZE];
		        try {
		            AudioInputStream as = AudioSystem.getAudioInputStream(audioFile);
		            AudioFormat format = as.getFormat();
		            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
		            SourceDataLine al = (SourceDataLine) AudioSystem.getLine(info);
		            al.open(format);
		            al.start();

		            while ((read = as.read(bBuf)) != -1) {
		                al.write(bBuf, 0, read);
		            }
		             
		            al.drain();
		            al.close();
		            as.close();
		             
		        } catch (Exception e) {
		        	e.printStackTrace();
		        }
    		}
    	}).start();
    }
     
}
