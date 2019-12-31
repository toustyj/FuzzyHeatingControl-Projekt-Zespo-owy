import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.swing.*;


public class Main extends Application {
    private int compWidth, compHeight;

    @Override
    public void start(Stage window) {

        Label header = new Label("                       Parametry pomieszczenia:");
        Label areaLabel = new Label("                     Powierzchnia[m2]:");
        Label heightLabel = new Label("                            Wysokość[m]:");
        Label powerLabel = new Label("                          Moc pieca[kW]:");
        Label startTempLabel = new Label("   Temperatura początkowa[°C]:");
        Label optTempLabel = new Label("     Optymalna temperatura[°C]:");
        Label isolationLabel = new Label("             Izolacja pomieszczenia:");
        Label seasonLabel = new Label("                                  Pora roku:");
        Label breakTimeLabel = new Label("Czas przerwy między próbkami:  ");

        header.setFont(Font.font("Verdana", 20));

        TextField areaField = new TextField("150");
        TextField heightField = new TextField("2.5");
        TextField powerField = new TextField("5");
        TextField startTempField = new TextField("12");
        TextField optTempField = new TextField("25");

        ChoiceBox seasonChoiceBox = new ChoiceBox();
        seasonChoiceBox.getItems().addAll("Wiosna", "Lato", "Jesień", "Zima");
        seasonChoiceBox.setValue("Zima");

        ChoiceBox isolationChoiceBox = new ChoiceBox();
        isolationChoiceBox.getItems().addAll("Brak","Styropian 15cm","Wełna mineralna 15cm");
        isolationChoiceBox.setValue("Styropian 15cm");

        ChoiceBox breakTimeChoiceBox = new ChoiceBox();
        breakTimeChoiceBox.getItems().addAll("Brak", "1/10 sekundy", "1/5 sekundy", "1/2 sekundy", "1 sekunda" );
        breakTimeChoiceBox.setValue("1/5 sekundy");


        Button startSimulation = new Button();
        Image image = new Image(getClass().getResourceAsStream("info.png"));

        Button showInAndOutSets = new Button("");

        Button infoIsolation = new Button();
        Button infoSeason = new Button();
        Button infoBreakTime = new Button();
        infoIsolation.setGraphic(new ImageView(image));
        infoIsolation.setStyle("-fx-background-color: transparent;");
        infoSeason.setGraphic(new ImageView(image));
        infoSeason.setStyle("-fx-background-color: transparent;");
        infoBreakTime.setGraphic(new ImageView(image));
        infoBreakTime.setStyle("-fx-background-color: transparent;");

        compWidth = 200;
        compHeight = 32;

        startSimulation.setMinSize(compWidth,45);
        startSimulation.setMaxSize(compWidth,45);
        showInAndOutSets.setMinSize(compWidth, 45);
        showInAndOutSets.setMaxSize(compWidth, 45);

        setSize(breakTimeChoiceBox);
        setSize(isolationChoiceBox);
        setSize(seasonChoiceBox);
        setSize(areaField);
        setSize(heightField);
        setSize(powerField);
        setSize(startTempField);
        setSize(optTempField);


        Scanner scannerSpring = null;
        try {
            scannerSpring = new Scanner(new File("wiosna.txt"));
        } catch (FileNotFoundException e){
            System.out.println("File not found");
        }
        Scanner scannerSummer = null;
        try {
            scannerSummer = new Scanner(new File("lato.txt"));
        } catch (FileNotFoundException e){
            System.out.println("File not found");
        }
        Scanner scannerAutumn = null;
        try {
            scannerAutumn = new Scanner(new File("jesień.txt"));
        } catch (FileNotFoundException e){
            System.out.println("File not found");
        }
        Scanner scannerWinter = null;
        try {
            scannerWinter = new Scanner(new File("zima.txt"));
        } catch (FileNotFoundException e){
            System.out.println("File not found");
        }

        double[] tempsfromfileSpring = new double[25];
        double[] tempsfromfileSummer = new double[25];
        double[] tempsfromfileAutumn = new double[25];
        double[] tempsfromfileWinter = new double[25];

        for(int i=1; i<=25; i++) {
            tempsfromfileSpring[i-1] = scannerSpring.nextDouble();
        }

        for(int i=1; i<=25; i++) {
            tempsfromfileSummer[i-1] = scannerSummer.nextDouble();
        }

        for(int i=1; i<=25; i++) {
            tempsfromfileAutumn[i-1] = scannerAutumn.nextDouble();
        }

        for(int i=1; i<=25; i++) {
            tempsfromfileWinter[i-1] = scannerWinter.nextDouble();
        }

        ArrayList<Double> tempsArraySpring = determine(tempsfromfileSpring);
        ArrayList<Double> tempsArraySummer = determine(tempsfromfileSummer);
        ArrayList<Double> tempsArrayAutumn = determine(tempsfromfileAutumn);
        ArrayList<Double> tempsArrayWinter = determine(tempsfromfileWinter);


        double[] tempsSpring = new double[tempsArraySpring.size()];
        for(int i=0; i<tempsArraySpring.size(); i++){
            tempsSpring[i] = tempsArraySpring.get(i);
        }

        double[] tempsSummer = new double[tempsArraySummer.size()];
        for(int i=0; i<tempsArraySummer.size(); i++){
            tempsSummer[i] = tempsArraySummer.get(i);
        }

        double[] tempsAutumn = new double[tempsArrayAutumn.size()];
        for(int i=0; i<tempsArrayAutumn.size(); i++){
            tempsAutumn[i] = tempsArrayAutumn.get(i);
        }

        double[] tempsWinter = new double[tempsArrayWinter.size()];
        for(int i=0; i<tempsArrayWinter.size(); i++){
            tempsWinter[i] = tempsArrayWinter.get(i);
        }


        startSimulation.setOnAction(e->{
            try {
               double[] temps;
                if (seasonChoiceBox.getValue().equals("Wiosna")){
                    temps = tempsSpring;
                }
                else if (seasonChoiceBox.getValue().equals("Lato")){
                    temps = tempsSummer;
                }
                else if (seasonChoiceBox.getValue().equals("Jesień")){
                    temps = tempsAutumn;
                }
                else {
                    temps = tempsWinter;
                }

                double area = Double.parseDouble(areaField.getText());
                double height = Double.parseDouble(heightField.getText());
                double power = Double.parseDouble(powerField.getText());
                double startT = Double.parseDouble(startTempField.getText());
                double OptT = Double.parseDouble(optTempField.getText());

                String breakTime = breakTimeChoiceBox.getValue().toString();
                String isolation = isolationChoiceBox.getValue().toString();

                if (power >= 0 & height > 0 & area > 0 ) {
                    new FuzzyLogic(temps, startT, area, height, OptT, power, isolation, breakTime);
                }
                else {
                    alert("BŁĄD","Powierzchnia, wysokość, oraz moca pieca muszą być dodatnimi wartościami",
                            "Ustaw podane pola na dodatnie wartości i spróbuj ponownie", Alert.AlertType.WARNING);
                }
            }
            catch (Exception error)
            {
                System.out.println(error);
                alert("BŁĄD","Wszystkie pola muszą zawierać wartości liczbowe",
                        "Ustaw pola na wartości liczbowe i spróbuj ponownie", Alert.AlertType.WARNING);
            }

        });

        infoIsolation.setOnAction(e-> {
            alert("Dodatkowe informacje","Ściany pomieszczenia mają grubość 18cm i wykonane są z żelbetonu",
                    "Izolacja jest dodatkowym materiałem izolacyjnym dodawanym do ścian", Alert.AlertType.INFORMATION);
        });

        infoSeason.setOnAction(e-> {
            alert("Dodatkowe informacje","Wybór temperaturych zewnętrznych w ciągu dnia na podstawie przykładowych pomiarów."
                            ,
                    "Wiosna - (Kraków, 5 kwietnia 2019r)\nLato -  (Gdańsk, 15 Lipca 2019r)\n" +
                            "Jesień - (Gdańsk, 10 listopada 2019r)\nZima - (Bielsko-biała,, 19 stycznia 2019r) ", Alert.AlertType.INFORMATION);
        });
        infoBreakTime.setOnAction(e-> {
            alert("Dodatkowe informacje","Czas rzeczywisty odpowiadający następnej próbce pomiaru na wykresie.\n" +
                            "Czas przerwy na wykresie (między próbkami) wynosi 1 minutę "
                    ,
                    "Brak - Natychmiastowe rysowanie przebiegu z całego dnia\n" +
                            "1/10 sekundy - W ciągu sekundy pojawi się 10 próbek pomiarów (10 minut)\n" +
                            "1/5 sekundy - W ciągu sekundy pojawi się 5 próbek pomiarów (5 minut)\n" +
                            "1/2 sekundy - W ciągu sekundy pojawią się 2 próbki pomiarów (2 minuty)\n" +
                            "1 sekunda - W ciągu sekundy pojawi się próbka pomiarów (1 minuta)"
                    , Alert.AlertType.INFORMATION);
        });



        showInAndOutSets.setOnAction(e->{
            new InputAndOutputSets();
        });



        GridPane grid = new GridPane();
        grid.getChildren().addAll(header, areaLabel, heightLabel, isolationLabel, powerLabel, startTempLabel,
                optTempLabel, breakTimeLabel, seasonLabel, areaField, heightField, powerField, startTempField,
                optTempField, isolationChoiceBox, seasonChoiceBox, breakTimeChoiceBox, startSimulation, showInAndOutSets, infoIsolation, infoSeason, infoBreakTime);

        GridPane.setConstraints(header,0,0,3,1);
        GridPane.setConstraints(areaLabel, 0,1);
        GridPane.setConstraints(heightLabel, 0,2);
        GridPane.setConstraints(powerLabel,0,3);
        GridPane.setConstraints(startTempLabel,0,4);
        GridPane.setConstraints(optTempLabel,0,5);
        GridPane.setConstraints(isolationLabel,0,6);
        GridPane.setConstraints(seasonLabel, 0,7);
        GridPane.setConstraints(breakTimeLabel, 0, 8);

        GridPane.setConstraints(infoIsolation,2,6);
        GridPane.setConstraints(infoSeason, 2, 7);
        GridPane.setConstraints(infoBreakTime, 2, 8);
        GridPane.setConstraints(showInAndOutSets, 0, 9);

        GridPane.setConstraints(areaField, 1,1);
        GridPane.setConstraints(heightField,1,2);
        GridPane.setConstraints(powerField,1,3);
        GridPane.setConstraints(startTempField,1,4);
        GridPane.setConstraints(optTempField,1,5);
        GridPane.setConstraints(isolationChoiceBox,1,6);
        GridPane.setConstraints(seasonChoiceBox,1,7);
        GridPane.setConstraints(breakTimeChoiceBox, 1, 8);
        GridPane.setConstraints(startSimulation,1,9);
        GridPane.setConstraints(showInAndOutSets, 1, 10);

        grid.getRowConstraints().add(new RowConstraints(40));
        grid.getRowConstraints().add(new RowConstraints(40));
        grid.getRowConstraints().add(new RowConstraints(40));
        grid.getRowConstraints().add(new RowConstraints(40));
        grid.getRowConstraints().add(new RowConstraints(40));
        grid.getRowConstraints().add(new RowConstraints(40));
        grid.getRowConstraints().add(new RowConstraints(40));
        grid.getRowConstraints().add(new RowConstraints(40));
        grid.getRowConstraints().add(new RowConstraints(40));
        grid.getRowConstraints().add(new RowConstraints(65));
        grid.getRowConstraints().add(new RowConstraints(50));

        grid.setPadding(new Insets(10, 0, 0, 100));
        grid.setVgap(5);    // przerwy miedzy wierszami
       
        grid.setStyle("-fx-background-color: #333333;");

        Label label = new Label("Start symulacji");
        label.setStyle("-fx-effect: dropshadow( one-pass-box , black , 6 , 0.2 , 2 , 2 ) ; -fx-text-fill: #e8e8e8;");
        startSimulation.setGraphic(label);
        startSimulation.setStyle("-fx-background-color: linear-gradient(#dc81ab, #ab4c49)");

        Label label2 = new Label("Pokaż zbiory rozmyte");

        showInAndOutSets.setGraphic(label2);
        showInAndOutSets.setStyle("-fx-background-color: linear-gradient(#9ddca5, #4b9cff)");

        String textColor = "-fx-text-fill: white";
        header.setStyle(textColor);
        header.setStyle(textColor);
        areaLabel.setStyle(textColor);
        heightLabel.setStyle(textColor);
        powerLabel.setStyle(textColor);
        startTempLabel.setStyle(textColor);
        optTempLabel.setStyle(textColor);
        isolationLabel.setStyle(textColor);
        breakTimeLabel.setStyle(textColor);
        seasonLabel.setStyle(textColor);


        Scene scene = new Scene(grid);
        window.setTitle("Symulacja pieca");
        window.setMinWidth(800);
        window.setMinHeight(600);
        window.setMaxHeight(600);
        window.setMaxWidth(800);
        window.setScene(scene);
        window.show();


    }

    public void getTempsFromFiles(){

    }




    public void alert(String Title, String HeaderText, String ContentText, Alert.AlertType at) {
        Alert alert = new Alert(at);
        alert.setTitle(Title);
        alert.setHeaderText(HeaderText);
        alert.setContentText(ContentText);
        alert.showAndWait();
    }

    private void setSize(Control control){
        control.setMinSize(compWidth, compHeight);
        control.setMaxSize(compWidth, compHeight);
    }



    private static ArrayList<Double> determine(double[] temps){
        double T1, T2,t1,t2;
        ArrayList<Double> newTemps = new ArrayList<>();
        for(int i = 0; i<(temps.length-1); i++){
            T1=temps[i];
            T2=temps[i+1];
            t2 = 3600;
            //double[0] to x, double[1] to y
            double[] variables = linearSystem(T1,0,T2,t2);

            //y - temperatura, x - czas
            //T = a*t + b
            for(int j=0; j< t2; j++){
                t1=j;
                double result = variables[0]*t1 + variables[1];
                newTemps.add(result);
            }
        }
        return newTemps;
    }

    /**
     * T1 - temperatura z godziny pierwszej
     * T2 - temperatury z godziny drugiej
     * t1 - czas pierwszy
     * t2 - czas drugi
     * @return wynik[0] = x
     * @return wynik[1] = y
     */
    private static double[] linearSystem(double T1, double t1, double T2, double t2){
        double b1 = 1;
        double b2 = 1;
        double w,wx,wy,x,y;
        w = t1*b2 - b1*t2;
        wx = T1*b2 - b1*T2;
        wy = t1*T2 - T1*t2;
        x = wx/w;
        y = wy/w;
        if(w!= 0){
            return new double[] {x,y};
        }
        else
        {
            return null;
        }
    }
}


