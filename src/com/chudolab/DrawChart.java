package com.chudolab;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Chudo on 19.01.2016.
 */
public class DrawChart {

    public ArrayList<XYChart.Series> createChart(String firstFile, String secondFile)throws IOException{
        DownloadTXT downloadTXT = new DownloadTXT();
        ArrayList yNums = downloadTXT.fileRead(firstFile);
        ArrayList yPeriod = downloadTXT.fileRead(secondFile);

        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        XYChart.Series series3 = new XYChart.Series();

        series1.setName("Numbers");
        series2.setName("Period");

        ObservableList<XYChart.Data> datas = FXCollections.observableArrayList();
        ObservableList<XYChart.Data> datas2 = FXCollections.observableArrayList();
        ObservableList<XYChart.Data> timeline = FXCollections.observableArrayList();

        double startX = 0.00000;
        for(int i=0; i<yNums.size(); i++){
            startX += 0.00006;
            datas.add(new XYChart.Data(startX,yNums.get(i)));
            datas2.add(new XYChart.Data(startX,yPeriod.get(i)));
        }

        timeline.add(new XYChart.Data(0,-1));
        timeline.add(new XYChart.Data(0,1) );

        ArrayList<XYChart.Series> arrSeries = new ArrayList<>();
        series1.setData(datas);
        series2.setData(datas2);
        series3.setData(timeline);

        arrSeries.add(series1);
        arrSeries.add(series2);
        arrSeries.add(series3);

        return arrSeries;

    }
    public void savePngChart( LineChart numberLineChart){

        WritableImage image = numberLineChart.snapshot(new SnapshotParameters(), null);

        // TODO:  file chooser here
        File file = new File("chart.png");

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
