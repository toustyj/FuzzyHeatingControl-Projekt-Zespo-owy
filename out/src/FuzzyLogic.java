import java.util.ArrayList;
import java.util.LinkedList;

public class FuzzyLogic {

    double d, c, m, k, powerMax;
    double[] insideTemp, tempIncrease, power, Δt, ΔT;
    double xIn, xOut;
    double tempMissingVLowBottom = 0,
            tempMissingVLowTop = 2,
            tempMissingLowBottom = 1,
            tempMissingLowTop = 5,
            tempMissingMediumBottom = 3.5,
            tempMissingMediumTop = 7,
            tempMissingHighBottom = 6,
            tempMissingHighTop = 15,
            tempMissingVHighBottom = 10,
            tempMissingVHighTop = 70;

    double heating_vVerylow, heating_vlow, heating_low, heating_littleLow, heating_medium, heating_littleHigh, heating_High, heating_vHigh, heating_vVeryHigh;
    double tempOutMissingLowBottom, tempOutMissingLowTop, tempOutMissingMediumBottom, tempOutMissingMediumTop, tempOutMissingHighBottom, tempOutMissingHighTop;
    double[] zaplonZewnetrzne, zaplonWewnetrzne;

    public FuzzyLogic(double[] outsideT, double startT, double area, double height, double OptT,
                      double powerMax, String isolation, String breakTime){

        this.powerMax = powerMax;
        power = new double[86400];
        insideTemp = new double[86401];
        Δt = new double[86400];
        ΔT = new double[86400];
        tempIncrease = new double[86400];
        insideTemp[0] = startT;
        power[0] = 0;
        setHeatingPowerPercentage();

        c = 1005;   //ciepło właściwe powietrza
        m = area * height * 1.2; // masa powietrza , 1.2 - gestosc powietrza


        if (isolation.equals("Brak")){
            k = 1.7;    //przewodnosc cieplna dla cegly
            d = 0.18;   // grubosc sciany
        }
        if (isolation.equals("Styropian 15cm")){

            k = (0.545 * 1.7) + (0.455 * 0.03); //45.5% grubości styropian o przewodnosci cieplnej 0.03
            d = 0.33; // 0.18 + 0.15
        }
        if (isolation.equals("Wełna mineralna 15cm")){

            k = (0.545 * 1.7) + (0.455 * 0.04); //45.5% grubości wełna o przewodnosci cieplnej 0.04
            d = 0.33; // 0.18 + 0.15
        }



        for (int i=0; i<=86399; i++){

            tempOutMissingLowBottom = 0;
            tempOutMissingLowTop = 37.5;;
            tempOutMissingMediumBottom = 0;
            tempOutMissingMediumTop = 75;
            tempOutMissingHighBottom = 37.5;
            tempOutMissingHighTop = 75;


            ΔT[i]=outsideT[i] - insideTemp[i];           // ΔT jest to różnica temperatury zewnetrznej i wewnetrznej

            Δt[i]=(k*Math.sqrt(area)*height*ΔT[i])/(m*c*d);  //10 sekund czyli 1 iteracja
            // Δt jest to zmiana temperatury wewnętrznej pod wpływem temperatury zewnętrznej

            zaplonWewnetrzne = new double[5];
            zaplonZewnetrzne = new double[3];


//---------------------------------------  R E G U Ł Y    R O Z M Y T E  ------------------------------------------------------------

            xIn = OptT - insideTemp[i];
            xOut = OptT - outsideT[i];

            LinkedList<Regula> listaRegulzZaplonem = new LinkedList<>();
            LinkedList<Regula> listaRegul;

            // Wyznaczanie reguł z zapłonem

            //wew bardzo mala
            if(affiliationToInsideMissingVeryLow()>0 && affiliationToOutMissingLow() >0){
                listaRegulzZaplonem.add(new Regula(1, affiliationToInsideMissingVeryLow(), affiliationToOutMissingLow()));
            }
            if(affiliationToInsideMissingVeryLow() > 0 && affiliationToOutMissingMedium() > 0){
                listaRegulzZaplonem.add(new Regula(2, affiliationToInsideMissingVeryLow(), affiliationToOutMissingMedium()));
            }

            if(affiliationToInsideMissingVeryLow() > 0 && affiliationToOutMissingHigh() > 0){
                listaRegulzZaplonem.add(new Regula(3, affiliationToInsideMissingVeryLow(), affiliationToOutMissingHigh()));
                //  listaRegulzZaplonem.add(new Regula());
            }
            //wew mala
            if(affiliationToInsideMissingLow() > 0 && affiliationToOutMissingLow() > 0){
                listaRegulzZaplonem.add(new Regula(4, affiliationToInsideMissingLow(), affiliationToOutMissingLow()));
            }
            if(affiliationToInsideMissingLow() > 0 && affiliationToOutMissingMedium() > 0){
                listaRegulzZaplonem.add(new Regula(5, affiliationToInsideMissingLow(), affiliationToOutMissingMedium()));
            }
            if(affiliationToInsideMissingLow() > 0 && affiliationToOutMissingHigh() > 0){
                listaRegulzZaplonem.add(new Regula(6, affiliationToInsideMissingLow(), affiliationToOutMissingHigh()));
            }
            //srednia
            if(affiliationToInsideMissingMedium() > 0 && affiliationToOutMissingLow() > 0){
                listaRegulzZaplonem.add(new Regula(7, affiliationToInsideMissingMedium(), affiliationToOutMissingLow()));
            }
            if(affiliationToInsideMissingMedium() > 0 && affiliationToOutMissingMedium() > 0){
                listaRegulzZaplonem.add(new Regula(8, affiliationToInsideMissingMedium(), affiliationToOutMissingMedium()));
            }
            if(affiliationToInsideMissingMedium() > 0 && affiliationToOutMissingHigh() > 0){
                listaRegulzZaplonem.add(new Regula(9, affiliationToInsideMissingMedium(), affiliationToOutMissingHigh()));
            }
            //duza
            if(affiliationToInsideMissingHigh() > 0 && affiliationToOutMissingLow() > 0){
                listaRegulzZaplonem.add(new Regula(10, affiliationToInsideMissingHigh(), affiliationToOutMissingLow()));
            }
            if(affiliationToInsideMissingHigh() > 0 && affiliationToOutMissingMedium() > 0){
                listaRegulzZaplonem.add(new Regula(11, affiliationToInsideMissingHigh(), affiliationToOutMissingMedium()));
            }
            if(affiliationToInsideMissingHigh() > 0 && affiliationToOutMissingHigh() > 0){
                listaRegulzZaplonem.add(new Regula(12, affiliationToInsideMissingHigh(), affiliationToOutMissingHigh()));
            }
            //bardzo duza
            if(affiliationToInsideMissingVeryHigh() > 0 && affiliationToOutMissingLow() > 0){
                listaRegulzZaplonem.add(new Regula(13, affiliationToInsideMissingVeryHigh(), affiliationToOutMissingLow()));
            }
            if(affiliationToInsideMissingVeryHigh() > 0 && affiliationToOutMissingMedium() > 0){
                listaRegulzZaplonem.add(new Regula(14, affiliationToInsideMissingVeryHigh(), affiliationToOutMissingMedium()));
            }
            if(affiliationToInsideMissingVeryHigh() > 0 && affiliationToOutMissingHigh() > 0){
                listaRegulzZaplonem.add(new Regula(15, affiliationToInsideMissingVeryHigh(), affiliationToOutMissingHigh()));
            }

            listaRegul = rozwiazKonflikty(listaRegulzZaplonem);

            double metodaCiezkosciLicznik=0;
            double metodaCiezkosciMianownik=0;
            double metodaCiezkosci = 0;
            for (int j=0; j<listaRegul.size(); j++){
                metodaCiezkosciLicznik+=ciezarReguly(listaRegul.get(j));
            }
            for (int j=0; j<listaRegul.size(); j++){
                metodaCiezkosciMianownik+=listaRegul.get(j).getZaplon();
            }
            if(metodaCiezkosciMianownik!=0 && metodaCiezkosciLicznik != 0) {
                metodaCiezkosci = metodaCiezkosciLicznik / metodaCiezkosciMianownik;
            }
            power[i] = metodaCiezkosci;



//--------------------------------------KONIEC REGUL ROZMYTYCH------------------------------------------------


            tempIncrease[i] = (power[i] * 1000) / (m * c);     //1kW to 1000W, 10 sekund trwa iteracja
            insideTemp[i+1] = insideTemp[i] + Δt[i]+ tempIncrease[i];
// https://www.physicsclassroom.com/Class/thermalP/u18l1f.cfm?fbclid=IwAR2_gFvWmWkDTSpODqvbuMBw9niOQetVdwZ5idIsIy3hLV6uMY65G7YGCyw
// https://pl.wikipedia.org/wiki/Przewodzenie_ciep%C5%82a
// równanie Fouriera( q = (k * A * deltaT)/d )

//https://pl.khanacademy.org/science/chemistry/thermodynamics-chemistry/internal-energy-sal/a/heat
// Q = m * c * Δt

        }//for


        int sleepMSec;
        switch(breakTime){
            case "1 sekunda" :
            sleepMSec = 1000;
            break;
            case "1/2 sekundy":
            sleepMSec = 500;
            break;
            case "1/5 sekundy":
            sleepMSec = 200;
            break;
            case "1/10 sekundy":
            sleepMSec = 100;
            break;
            default:
            sleepMSec = 0;
            break;}


        new RealTimeChart(getTempIn(), "Temperatury wewnętrzne w czasie", sleepMSec);
        new RealTimeChart(getPower(),"Moc pieca w czasie", sleepMSec);
        new RealTimeChart(getTempOut(outsideT), "Temperatury zewnętrzne w czasie", sleepMSec);
    }

    private double ciezarReguly(Regula regula){
        if(regula.getNumer() == 1){
            return regula.getZaplon()*heating_low;
        }
        else if(regula.getNumer() == 2){
            return regula.getZaplon()*heating_vlow;
        }
        else if(regula.getNumer() == 3){
            return regula.getZaplon()*heating_vVerylow;
        }
        else if(regula.getNumer() == 4){
            return regula.getZaplon()*heating_littleLow;
        }
        else if(regula.getNumer() == 5){
            return regula.getZaplon()*heating_low;
        }
        else if(regula.getNumer() == 6){
            return regula.getZaplon()*heating_vlow;
        }
        else if(regula.getNumer() == 7){
            return regula.getZaplon()*heating_littleHigh;
        }
        else if(regula.getNumer() == 8){
            return regula.getZaplon()*heating_medium;
        }
        else if(regula.getNumer() == 9){
            return regula.getZaplon()*heating_medium;
        }
        else if(regula.getNumer() == 10){
            return regula.getZaplon()*heating_vHigh;
        }
        else if(regula.getNumer() == 11){
            return regula.getZaplon()*heating_High;
        }
        else if(regula.getNumer() == 12){
            return regula.getZaplon()*heating_littleHigh;
        }
        else if(regula.getNumer() == 13){
            return regula.getZaplon()*heating_vVeryHigh;
        }
        else if(regula.getNumer() == 14){
            return regula.getZaplon()*heating_vHigh;
        }
        else if(regula.getNumer() == 15){
            return regula.getZaplon()*heating_High;
        }
        else return 0;
    }





    private LinkedList<Regula> rozwiazKonflikty(LinkedList<Regula> listaRegul){
        LinkedList<Regula> noweReguly = new LinkedList<>();
        ArrayList<Integer> numeryRegulDoUsuniecia = new ArrayList<>();
        int[] konfliktBMalo = czyZawieraKonflikt(listaRegul, 2,6);
        int[] konfliktMalo = czyZawieraKonflikt(listaRegul, 1,5);
        int[] konfliktSrednio = czyZawieraKonflikt(listaRegul, 8,9);
        int[] konfliktTrocheDuzo = czyZawieraKonflikt(listaRegul, 7,12);
        int[] konfliktDuzo = czyZawieraKonflikt(listaRegul, 11,15);
        int[] konfliktBDuzo = czyZawieraKonflikt(listaRegul, 10,14);

        if(konfliktBMalo!= null){
            noweReguly.add(rozwiazKonflikt(listaRegul.get(konfliktBMalo[0]), listaRegul.get(konfliktBMalo[1])));
            numeryRegulDoUsuniecia.add(listaRegul.get(konfliktBMalo[0]).getNumer());
            numeryRegulDoUsuniecia.add(listaRegul.get(konfliktBMalo[1]).getNumer());
        }
        if(konfliktMalo!= null){
            noweReguly.add(rozwiazKonflikt(listaRegul.get(konfliktMalo[0]), listaRegul.get(konfliktMalo[1])));
            numeryRegulDoUsuniecia.add(listaRegul.get(konfliktMalo[0]).getNumer());
            numeryRegulDoUsuniecia.add(listaRegul.get(konfliktMalo[1]).getNumer());
        }
        if(konfliktSrednio!= null){
            noweReguly.add(rozwiazKonflikt(listaRegul.get(konfliktSrednio[0]), listaRegul.get(konfliktSrednio[1])));
            numeryRegulDoUsuniecia.add(listaRegul.get(konfliktSrednio[0]).getNumer());
            numeryRegulDoUsuniecia.add(listaRegul.get(konfliktSrednio[1]).getNumer());
        }
        if(konfliktTrocheDuzo!= null){
            noweReguly.add(rozwiazKonflikt(listaRegul.get(konfliktTrocheDuzo[0]), listaRegul.get(konfliktTrocheDuzo[1])));
            numeryRegulDoUsuniecia.add(listaRegul.get(konfliktTrocheDuzo[0]).getNumer());
            numeryRegulDoUsuniecia.add(listaRegul.get(konfliktTrocheDuzo[1]).getNumer());
        }
        if(konfliktDuzo!= null){
            noweReguly.add(rozwiazKonflikt(listaRegul.get(konfliktDuzo[0]), listaRegul.get(konfliktDuzo[1])));
            numeryRegulDoUsuniecia.add(listaRegul.get(konfliktDuzo[0]).getNumer());
            numeryRegulDoUsuniecia.add(listaRegul.get(konfliktDuzo[1]).getNumer());
        }

        if(konfliktBDuzo!= null){
            noweReguly.add(rozwiazKonflikt(listaRegul.get(konfliktBDuzo[0]), listaRegul.get(konfliktBDuzo[1])));
            numeryRegulDoUsuniecia.add(listaRegul.get(konfliktBDuzo[0]).getNumer());
            numeryRegulDoUsuniecia.add(listaRegul.get(konfliktBDuzo[1]).getNumer());
        }

        for (Integer integer : numeryRegulDoUsuniecia) {
            usunReguleZListy(listaRegul, integer);
        }

        listaRegul.addAll(noweReguly);



        return listaRegul;
    }

    //zwraca regule o wiekszym zapłonie
    private Regula rozwiazKonflikt(Regula regula1, Regula regula2){
        return (regula1.getZaplon() > regula2.getZaplon()) ? regula1 : regula2;
    }

    //usuwa regule o podanym id i zwraca linkedliste
    private void usunReguleZListy(LinkedList<Regula> listaRegul, int numerReguly){
        for(int i = 0; i<listaRegul.size();i++){
            if(listaRegul.get(i).getNumer() == numerReguly){
                listaRegul.remove(i);
            }
        }
    }

    //zwraca indeksy regul, w ktorych jest konflikt
    private int[] czyZawieraKonflikt(LinkedList<Regula> listaRegul, int R1test, int R2test){
        int R1index = -1;
        int R2index = -1;

        for(int i=0; i<listaRegul.size(); i++){
            if(listaRegul.get(i).getNumer() == R1test){
                R1index = i;
            }
            if(listaRegul.get(i).getNumer() == R2test){
                R2index = i;
            }
        }
        if(R1index!=-1 && R2index!=-1){
            return new int[] {R1index, R2index};
        }
        else return null;
    }



// ================  A  G  R  E  G  A  C  J  A   ===========
    private double affiliationToOutMissingLow(){
        if(outsideBetweenLowAndMedium()) {
            return slopeFalling(tempOutMissingLowTop, xOut, tempOutMissingLowBottom);
        }
        else return 0;
    }
    private double affiliationToOutMissingMedium(){
        if(outsideBetweenMediumAndHigh()) {
            return slopeFalling(tempOutMissingMediumTop, xOut, tempOutMissingHighBottom);
        }
        else if (outsideBetweenLowAndMedium()) {
            return slopeRising(tempOutMissingLowBottom, xOut, tempOutMissingLowTop);
        }
        else return 0;
    }
    private double affiliationToOutMissingHigh(){
        if(outsideBetweenMediumAndHigh()){
            return slopeRising(tempOutMissingHighBottom, xOut, tempOutMissingHighTop);
        }
        else return 0;
    }

    private double affiliationToInsideMissingVeryLow(){
        if(insideMissingVeryLow()){
            return 1;
        }
        else if(insideMissingBetweenVeryLowAndLow()){
            return slopeFalling(tempMissingVLowTop, xIn, tempMissingLowBottom);
        }
        else return 0;
    }

    private double affiliationToInsideMissingLow(){
        if(insideMissingBetweenVeryLowAndLow()){
            return slopeRising(tempMissingLowBottom, xIn, tempMissingVLowTop);
        }
        else if (insideMissingLow()){
            return 1;
        }
        else if(insideMissingBetweenLowAndMedium()){
            return slopeFalling(tempMissingLowTop, xIn, tempMissingMediumBottom);
        }
        else return 0;
    }

    private double affiliationToInsideMissingMedium(){
        if(insideMissingBetweenLowAndMedium()){
            return slopeRising(tempMissingMediumBottom, xIn, tempMissingLowTop);
        }
        else if(insideMissingMedium()){
            return 1;
        }
        else if (insideMissingBetweenMediumAndHigh()){
            return slopeFalling(tempMissingMediumTop, xIn, tempMissingHighBottom);
        }
        else return 0;
    }
    private double affiliationToInsideMissingHigh(){
        if(insideMissingBetweenMediumAndHigh()){
            return slopeRising(tempMissingHighBottom, xIn, tempMissingMediumTop);
        }
        else if (insideMissingHigh()){
            return 1;
        }
        else if(insideMissingBetweenHighAndVeryHigh()){
            return slopeFalling(tempMissingHighTop, xIn, tempMissingVHighBottom);
        }
        else return 0;
    }
    private double affiliationToInsideMissingVeryHigh(){
        if(insideMissingBetweenHighAndVeryHigh()){
            return slopeRising(tempMissingVHighBottom, xIn, tempMissingHighTop);
        }
        else if (insideMissingVeryHigh()){
            return 1;
        }
        else return 0;
    }



    private double slopeRising(double a, double x, double xi){
        return (x-a)/(xi-a);
    }


    private double slopeFalling(double b, double x, double xii){
        return (b-x)/(b-xii);
    }


    // =========  warunki w regulach dla wewnetrznych temperatur
    private boolean insideMissingVeryLow(){
        return xIn >= tempMissingVLowBottom && xIn < tempMissingLowBottom;
    }
    private boolean insideMissingBetweenVeryLowAndLow(){
        return xIn >= tempMissingLowBottom && xIn < tempMissingVLowTop;
    }
    private boolean insideMissingLow(){
        return xIn >= tempMissingVLowTop && xIn < tempMissingMediumBottom;
    }
    private boolean insideMissingBetweenLowAndMedium(){
        return xIn >= tempMissingMediumBottom && xIn < tempMissingLowTop;
    }
    private boolean insideMissingMedium(){
        return xIn >= tempMissingLowTop && xIn < tempMissingHighBottom;
    }
    private boolean insideMissingBetweenMediumAndHigh(){
        return xIn >= tempMissingHighBottom && xIn < tempMissingMediumTop;
    }
    private boolean insideMissingHigh(){
        return xIn >= tempMissingMediumTop && xIn < tempMissingVHighBottom;
    }
    private boolean insideMissingBetweenHighAndVeryHigh(){
        return xIn >= tempMissingVHighBottom && xIn < tempMissingHighTop;
    }
    private boolean insideMissingVeryHigh(){
        return xIn >= tempMissingHighTop && xIn < tempMissingVHighTop;
    }

    // ====--- warunki w regulach dla zewnetrznych temperatur

    private boolean outsideBetweenLowAndMedium(){
        return xOut >= tempOutMissingLowBottom && xOut < tempOutMissingHighBottom;
    }
    private boolean outsideBetweenMediumAndHigh(){
        return xOut >= tempOutMissingHighBottom && xOut <= tempOutMissingMediumTop;
    }



    private void setHeatingPowerPercentage(){
                heating_vVerylow = powerMax * 0.3;
                heating_vlow = powerMax * 0.4;
                heating_low = powerMax * 0.5;
                heating_littleLow = powerMax * 0.6;
                heating_medium = powerMax * 0.7;
                heating_littleHigh = powerMax * 0.8;
                heating_High = powerMax * 0.9;
                heating_vHigh = powerMax * 0.95;
                heating_vVeryHigh = powerMax;
    }



    public double[] getTempIn(){
        double[] newInsideTemp = new double[1440];
        int numer = 0;
        for(int i=0; i<86400; i=i+60){      //co minute pobieramy temperature wewnetrzna
            newInsideTemp[numer] = insideTemp[i];
            numer++;
        }
        return newInsideTemp;
    }

    public double[] getPower(){
        double[] newPower = new double[1440];
        int numer = 0;
        for(int i=0; i<86400; i=i+60){     //co minute pobieramy moc
            newPower[numer] = power[i];
            numer++;
        }
        return newPower;
    }

    public double[] getTempOut(double[] outsideT) {
        double[] newOutsideTemp = new double[1440];
        int numer = 0;
        for(int i=0; i<86400; i=i+60){      //co minute pobieramy temperature zewnetrzna
            newOutsideTemp[numer] = outsideT[i];
            numer++;
        }
        return newOutsideTemp;
    }
}




