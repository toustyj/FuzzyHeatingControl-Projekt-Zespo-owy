import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;



public class Chart {

        public Chart(double [] getTempOrPower, String ChartTitle){

            Stage stage = new Stage();
            stage.setTitle(ChartTitle);


            final NumberAxis xAxis = new NumberAxis();
            final NumberAxis yAxis = new NumberAxis();
            xAxis.setLabel("Numer pomiaru");

            if (ChartTitle == "Moc pieca w czasie"){
                yAxis.setLabel ("kW");
            }
            else{
                yAxis.setLabel ("[°C]");
            }


            final LineChart<Number, Number> lineChart =
                    new LineChart<Number, Number>(xAxis, yAxis);



            XYChart.Series series = new XYChart.Series();

            if (ChartTitle == "Moc pieca w czasie") {
                for (int i = 0; i <= 479; i++) {
                    Double Y = getTempOrPower[i];
                    series.getData().add(new XYChart.Data(i, Y));
                    System.out.println(" Moc pieca ["+i+"]: "+getTempOrPower[i]);
                }
            }

            else{
                for(int i=0; i<=1439; i++){

                Double Y = getTempOrPower[i];
                series.getData().add(new XYChart.Data(i, Y));

                if (ChartTitle == "Temperatury wewnętrzne w czasie"){
                    System.out.println("Wewnętrzna temperatura ["+i+"]: " + getTempOrPower[i]);
                }
                if (ChartTitle == "Temperatury zewnętrzne w czasie"){
                    System.out.println("Zewnętrzna temperatura ["+i+"]: " + getTempOrPower[i]);
                }
                }
            }


            lineChart.setTitle(null);


            lineChart.getData().add(series);

            Scene scene = new Scene(lineChart, 800, 600);
            stage.setScene(scene);
            stage.show();

        }
    }


