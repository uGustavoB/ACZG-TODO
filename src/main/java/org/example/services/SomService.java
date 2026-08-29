package org.example.services;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SomService {
    public static void tocar(String caminho) {
        try {
            File arquivo = new File(caminho);

            AudioInputStream audio =
                    AudioSystem.getAudioInputStream(arquivo);

            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();

        } catch (UnsupportedAudioFileException |
                 IOException |
                 LineUnavailableException e) {

            e.printStackTrace();
        }
    }
}
