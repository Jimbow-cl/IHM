package com.crisis.ihm;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class IHMController {


    @FXML
    private CategoryAxis xAxish;
    @FXML
    private NumberAxis yAxish;
    @FXML
    private CategoryAxis xAxist;
    @FXML
    private NumberAxis yAxist;
    @FXML
    private LineChart<String, Number> humidityChart;
    @FXML
    private LineChart<String, Number> temperatureChart;

    // Initialisation de la méthode visualisationGraph()
    public void initialize() {
        visualisationGraph();
    }

    @FXML
    private void visualisationGraph() {
        // Humidité
        humidityChart.setTitle("Humidité");
        yAxish.setLabel("Pourcentage");
        xAxish.setLabel("Seconde");

        // Température
        temperatureChart.setTitle("Température");
        yAxist.setLabel("Degré");
        xAxist.setLabel("Seconde");

        XYChart.Series<String, Number> humiditySeries = new XYChart.Series<>();
        humiditySeries.setName("Humidité");

        XYChart.Series<String, Number> temperatureSeries = new XYChart.Series<>();
        temperatureSeries.setName("Température");

        // Format de l'heure
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        final int WINDOW_SIZE = 12;

        // Execution d'un délai pour le ré-affichage de la donnée

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            Esp32Data esp32Data = null;
            try {
                esp32Data = Esp32DataConnection.getEsp32Data();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (esp32Data != null) {
                long humidityBdd = esp32Data.getHumidity();
                long temperatureBdd = esp32Data.getTemperature();

                Platform.runLater(() -> {
                    Date now = new Date();

                    if (humiditySeries.getData().size() > WINDOW_SIZE)
                        humiditySeries.getData().remove(0);

                    if (temperatureSeries.getData().size() > WINDOW_SIZE)
                        temperatureSeries.getData().remove(0);

                    humiditySeries.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), humidityBdd));
                    temperatureSeries.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), temperatureBdd));
                    // humiditySeries.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), humidityRandom));
                    //  temperatureSeries.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), temperatureRandom));
                });
            }
        }, 0, 1, TimeUnit.SECONDS);

        humidityChart.getData().clear();
        humidityChart.getData().add(humiditySeries);

        temperatureChart.getData().clear();
        temperatureChart.getData().add(temperatureSeries);
    }
}
