import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class InputAndOutputSets {


    InputAndOutputSets(){
        Stage stage = new Stage();
        StackPane root = new StackPane();
        root.getChildren().addAll(infrastructurePane());
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
        int sceneWidth = 800;
        int sceneHeight;
         if ( screenHeight < 768) {
            sceneHeight = 600;
         }
         else if (screenHeight < 900){
             sceneHeight = 768;
         }
         else if (screenHeight < 1080){
             sceneHeight = 900;
         }
         else  {
            sceneHeight = 1020;
        }

        stage.setMinWidth(sceneWidth);
        stage.setMaxWidth(sceneWidth);
        stage.setMinHeight(sceneHeight);
        stage.setMaxHeight(sceneHeight);
    }

    public ScrollPane infrastructurePane() {

        LineChart<Number, Number> InputTermsInsideTemp = createLineChart( "Optymalna temp. - wewnętrzna temp. [°C]");
        LineChart<Number, Number> InputTermsOutsideTemp = createLineChart( "Optymalna temp. - zewnętrzna temp. [°C]");
        LineChart<Number, Number> OutputTermsPower = createLineChart( "Intensywność mocy pieca");

        final FlowPane flow = new FlowPane();
        Label textArea = new Label();
      //  textArea.setStyle("-fx-background-color: transparent;");
        textArea.setText("*Term 'Bardzo dużo' został skrócony w celu wyraźnej demonstracji zbioru.\nJego górna granica w rzeczywistości wynosi 70°C (na wykresie 20°C).");
        flow.setPadding(new Insets(5, 5, 5, 5));
        flow.setVgap(5);
        flow.setHgap(5);
        flow.setAlignment(Pos.CENTER);
        flow.getChildren().addAll(InputTermsInsideTemp, textArea, InputTermsOutsideTemp, OutputTermsPower);

        final ScrollPane scroll = new ScrollPane();

        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);    // Horizontal scroll bar
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);    // Vertical scroll bar
        scroll.setContent(flow);
        scroll.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
                flow.setPrefWidth(bounds.getWidth());
                flow.setPrefHeight(bounds.getHeight());
            }
        });
        return scroll;
    }

    private LineChart<Number, Number> createLineChart( String xAxisName) {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setPrefSize(750,320);
        yAxis.setLabel("μ");
        xAxis.setLabel(xAxisName);
        if (xAxisName == "Optymalna temp. - wewnętrzna temp. [°C]"){
        lineChart.setTitle("Zbiór wejściowy 1: Ile brakuje temperaturom wewnętrznym do optymalnej");

        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Bardzo mało");
        //populating the series with data
        series.getData().add(new XYChart.Data(0, 1));
        series.getData().add(new XYChart.Data(1, 1));
        series.getData().add(new XYChart.Data(2, 0));

        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Mało");
        series2.getData().add(new XYChart.Data(1, 0));
        series2.getData().add(new XYChart.Data(2, 1));
        series2.getData().add(new XYChart.Data(3.5, 1));
        series2.getData().add(new XYChart.Data(5, 0));

        XYChart.Series series3 = new XYChart.Series();
        series3.setName("Średnio");
        series3.getData().add(new XYChart.Data(3.5, 0));
        series3.getData().add(new XYChart.Data(5, 1));
        series3.getData().add(new XYChart.Data(6, 1));
        series3.getData().add(new XYChart.Data(7, 0));

        XYChart.Series series4 = new XYChart.Series();
        series4.setName("Dużo");
        series4.getData().add(new XYChart.Data(6, 0));
        series4.getData().add(new XYChart.Data(7, 1));
        series4.getData().add(new XYChart.Data(10, 1));
        series4.getData().add(new XYChart.Data(15, 0));

        XYChart.Series series5 = new XYChart.Series();
        series5.setName("Bardzo dużo*");
        series5.getData().add(new XYChart.Data(10, 0));
        series5.getData().add(new XYChart.Data(15, 1));
        series5.getData().add(new XYChart.Data(20, 1));

        lineChart.getData().addAll(series, series2, series3, series4, series5);}

        if (xAxisName.equals("Optymalna temp. - zewnętrzna temp. [°C]")){
            lineChart.setTitle("Zbiór wejściowy 2: Ile brakuje temperaturom zewnętrznym do optymalnej");
            XYChart.Series series = new XYChart.Series();
            series.setName("Mało");
            //populating the series with data
            series.getData().add(new XYChart.Data(0, 1));
            series.getData().add(new XYChart.Data(37.5, 0));

            XYChart.Series series2 = new XYChart.Series();
            series2.setName("Średnio");
            //populating the series with data
            series2.getData().add(new XYChart.Data(0, 0));
            series2.getData().add(new XYChart.Data(37.5, 1));
            series2.getData().add(new XYChart.Data(75, 0));

            XYChart.Series series3 = new XYChart.Series();
            series3.setName("Dużo");
            //populating the series with data
            series3.getData().add(new XYChart.Data(37.5, 0));
            series3.getData().add(new XYChart.Data(75, 1));

            lineChart.getData().addAll(series, series2, series3);
        }

        if (xAxisName.equals("Intensywność mocy pieca")){
            lineChart.setTitle("Zbiór wyjściowy: Intensywność mocy pieca                                                  ");

            XYChart.Series series1 = new XYChart.Series();
            XYChart.Series series2 = new XYChart.Series();
            XYChart.Series series3 = new XYChart.Series();
            XYChart.Series series4 = new XYChart.Series();
            XYChart.Series series5 = new XYChart.Series();
            XYChart.Series series6 = new XYChart.Series();
            XYChart.Series series7 = new XYChart.Series();
            XYChart.Series series8 = new XYChart.Series();
            XYChart.Series series9 = new XYChart.Series();

            series1.getData().add(new XYChart.Data(0.3, 0));
            series1.getData().add(new XYChart.Data(0.3, 1));

            series2.getData().add(new XYChart.Data(0.4, 0));
            series2.getData().add(new XYChart.Data(0.4, 1));

            series3.getData().add(new XYChart.Data(0.5, 0));
            series3.getData().add(new XYChart.Data(0.5, 1));

            series4.getData().add(new XYChart.Data(0.6, 0));
            series4.getData().add(new XYChart.Data(0.6, 1));

            series5.getData().add(new XYChart.Data(0.7, 0));
            series5.getData().add(new XYChart.Data(0.7, 1));

            series6.getData().add(new XYChart.Data(0.8, 0));
            series6.getData().add(new XYChart.Data(0.8, 1));

            series7.getData().add(new XYChart.Data(0.9, 0));
            series7.getData().add(new XYChart.Data(0.9, 1));

            series8.getData().add(new XYChart.Data(0.95, 0));
            series8.getData().add(new XYChart.Data(0.95, 1));

            series9.getData().add(new XYChart.Data(1, 0));
            series9.getData().add(new XYChart.Data(1, 1));

            series1.setName("Bardzo bardzo mała");
            series2.setName("Bardzo mała");
            series3.setName("Mała");
            series4.setName("Trochę mała");
            series5.setName("Średnia");
            series6.setName("Trochę dużo");
            series7.setName("Dużo");
            series8.setName("Bardzo dużo");
            series9.setName("Bardzo bardzo dużo");
            lineChart.getData().addAll(series1, series2, series3, series4, series5, series6, series7, series8, series9);

        }



        return lineChart;
    }

}