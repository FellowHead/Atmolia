package me.fellowhead.atmolia;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Controller {
    @FXML
    private Slider slider1, slider2, slider3, slider4, slider5;
    @FXML
    private Canvas canvas;

    private AudioBuilder builder;
    private Thread thread;
    private final byte bytesPerSample = 2;

    private long pos = 0;

    public boolean playOn = true;

    public void initialize() {
        builder = new AudioBuilder();
        thread = new Thread(this::playSample);
        thread.start();

        Pane parent = (Pane) canvas.getParent();
        canvas.widthProperty().bind(parent.widthProperty());
        canvas.heightProperty().bind(parent.heightProperty());

        new AnimationTimer() {
            @Override
            public void handle(long l) {
                GraphicsContext g = canvas.getGraphicsContext2D();
                g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                final double position = new AdvancedTime(pos, builder.bpm, builder.frequency).beats;

                double centerX = canvas.getWidth() / 2;

                g.setFill(Color.BLACK);
                g.fillRect(centerX, 0, 1, canvas.getHeight());
                double m = 25;
                int y = 20;
                double height = 5;

                int i = 0;
                for (Track t : builder.getTracks()) {
                    g.setFill(Color.BLUE.deriveColor((i++ * 50), 1, 1, 1));
                    Note[] notes = t.notes.toArray(new Note[0]);
                    for (Note n : notes) {
                        //System.out.println(n + " | " + (canvas.getHeight() - (n.abs - y) * height + height));
                        g.fillRect(centerX + (n.start.beats - position) * m, canvas.getHeight() - (n.abs - y) * height + height, n.length.beats * m, height);
                        //g.strokeText("" + n.getHertz(),centerX + (n.start.beats - position) * m, canvas.getHeight() - (n.abs - y) * height + height);
                    }
                }
            }
        }
        .start()
        ;
    }

    @FXML
    private void save() {
        new Thread(() -> {
            playOn = false;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            AudioBuilder.setBytesPerSmp((byte) 4);

            String time = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            boolean ubuntu = false;
            String mainDir = System.getProperty("user.home") + (ubuntu ? "/Downloads/Atmo" : "/Documents/Atmo");
            File audioFile = new File(mainDir + "/Audio/atmo_" + time + ".wav");
            File textFile = new File(mainDir + "/Chords/atmo-chords_" + time + ".txt");

            int size = (int)builder.frequency * 20 * builder.bytesPerSmp;
            int buffer = (int)builder.frequency * builder.bytesPerSmp;
            byte[] data = new byte[size];

            for (int i = 0; i < size; i += buffer) {
                byte[] received = builder.getBytes(i, buffer);
                for (int n = 0; n < buffer; n++) {
                    data[i + n] = received[n];
                }
                System.out.println("Receiving bytes... " + (100 * i / size) + "%");
            }

            Atmo.saveToFile(data, builder.format, audioFile);

            try (PrintWriter writer = new PrintWriter(textFile)) {
                writer.println("Atmolia Chords (" + new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()) + ")");
                for (Chord c : builder.savedChords) {
                    writer.println(c.getName());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            AudioBuilder.setBytesPerSmp(bytesPerSample);
            playOn = true;
        }).start();
    }

    public void playSample() {
        AudioBuilder.setBytesPerSmp(bytesPerSample);
        AudioFormat format = AudioBuilder.format;

        try {
            //Thread.sleep(10000);

            SourceDataLine sdl = AudioSystem.getSourceDataLine(format);
            int buffer = 2048 * AudioBuilder.bytesPerSmp;
            sdl.open(format, buffer);
            sdl.start();

            playOn = true;

            long i = 0;
            while (true){
                if (playOn) {
                    byte[] read = builder.getBytes(i, buffer);
                    pos = sdl.getLongFramePosition();
                    sdl.write(read, 0, buffer);
                    //System.out.println("Write at " + i);
                    i += buffer;
                } else {
                    //System.out.println("paused");
                    Thread.sleep(1000);
                }
            }
        } catch (LineUnavailableException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}