package com.chudolab;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;


public class Controller {
    private double count;
    private Timeline tl;
    private double fileDuration;
    private double startDrag;
    private double endDrag;
    private MediaPlayer mediaPlayer;

    @FXML
    private javafx.scene.shape.Rectangle highlighted;
    @FXML
    private LineChart<Number, Number> chart;
    @FXML
    private Button buttonPlay;

    @FXML
    protected void initialize() throws IOException {

        DrawChart drawChart = new DrawChart();
        ArrayList<XYChart.Series> arrSeries = drawChart.createChart("kafe_01_wav_nums.txt","kafe_01_wav_period.txt");

        chart.setTitle("Kafe");
        chart.setCreateSymbols(false);
        chart.setAnimated(false); // to make sure the png wouldn't be blank

        for (XYChart.Series series : arrSeries) {
            chart.getData().add(series);
        }
        getMediaPlayer("audio_resources/kafe01.wav");

        chart.setOnMousePressed(event -> {
            highlighted.setWidth(0);
            startDrag = 0;
            endDrag = 0;
            buttonPlay.setText("Play");
        });
        chart.setOnDragDetected(event -> {
            buttonPlay.setText("Play part");
            startDrag = event.getX();
        });
        chart.setOnMouseReleased(event -> {
            if (startDrag > 0) {
                if (event.getX() > 35) {
                    endDrag = event.getX();
                    highlighted.setX(startDrag - 164.5); // border reasons
                    highlighted.setY(-9.5); // border reasons too
                    if (startDrag > endDrag) {
                        highlighted.setWidth(startDrag - endDrag + 5);
                        highlighted.setX(endDrag - 164.5 + 5);
                    } else {
                        highlighted.setWidth(endDrag - startDrag + 5);
                    }
                }
            }
        });

        buttonPlay.setOnAction(event -> {
            mediaPlayer.setStartTime(new Duration(0.0));
            mediaPlayer.setStopTime(new Duration(fileDuration));
            mediaPlayer.seek(new Duration(0.0));

            if (highlighted.getWidth() == 0) {
                tl = new Timeline();
                XYChart.Series<Number, Number> timeCursor = chart.getData().get(2); // time cursor line
                XYChart.Data<Number, Number> data = timeCursor.getData().get(0);
                XYChart.Data<Number, Number> data2 = timeCursor.getData().get(1);

                count = 0.0000;
                tl.getKeyFrames().add(new KeyFrame(Duration.millis(0.6),
                        actionEvent -> {

                            if (count <= (fileDuration / 1000)) {
                                data.setXValue(count);
                                data2.setXValue(count);
                                count += 0.006;
                            } else {
                                tl.stop();
                            }
                        }));
                tl.setCycleCount(Animation.INDEFINITE);
                tl.play();
                mediaPlayer.play();

            } else if (highlighted.getWidth() != 0) {
                buttonPlay.setText("Play part");
                mediaPlayer.setStartTime(new Duration(startDrag - 40)); //for border reasons
                mediaPlayer.setStopTime(new Duration(endDrag - 40));
                mediaPlayer.play();
                mediaPlayer.seek(new Duration(startDrag));
            }
        });
    }
    public MediaPlayer getMediaPlayer(String pathToResource) {
        final URL resource = getClass().getResource(pathToResource);
        Media file = new Media(resource.toString());
        mediaPlayer = new MediaPlayer(file);

        mediaPlayer.setOnReady(new Runnable() {  // to get file duration
            @Override
            public void run() {
                fileDuration = file.getDuration().toMillis();
                for (Map.Entry<String, Object> entry : file.getMetadata().entrySet()) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
            }
        });
        return mediaPlayer;
    }
}

