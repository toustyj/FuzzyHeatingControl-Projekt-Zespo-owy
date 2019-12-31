import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RealTimeChart{

    RealTimeChart(double[] getTempOrPower, String chartTitle, int sleepMilisecond) {
        try{
        Stage stage = new Stage();
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();

        xAxis.setAnimated(false);
        xAxis.setLabel("Czas");
        yAxis.setAnimated(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        if (chartTitle == "Moc pieca w czasie"){
            yAxis.setLabel ("P [kW]");
            series.setName("Moc pieca");

        }

        else if (chartTitle == "Temperatury zewnętrzne w czasie"){
            yAxis.setLabel ("T [°C]");
            series.setName("Temperatury zewnętrzne");

        }
        else {
            yAxis.setLabel ("T [°C]");
            series.setName("Temperatury wewnętrzne");

        }

        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setAnimated(false);
        chart.getData().add(series);

        Scene scene = new Scene(chart, 800, 600);
        stage.setScene(scene);
        stage.show();
        stage.setTitle(chartTitle);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Thread updateThread = new Thread(() -> {

                try {
                    Calendar cal = Calendar.getInstance();

                    cal.set(Calendar.MILLISECOND, 0);
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);

                    for(int j=0; j<=1439; j++){
                        cal.add(Calendar.MINUTE, 1);
                        Thread.sleep(sleepMilisecond);
                        Date time = cal.getTime();
                        int finalI = j;
                        Platform.runLater(() -> series.getData().add(new XYChart.Data<>(simpleDateFormat.format(time), getTempOrPower[finalI])));
                    }

                } catch (Exception e) {
                }

        });
        updateThread.setDaemon(true);
            updateThread.start();}catch (Exception e){}

    }
}