import java.util.ArrayList;
import java.util.LinkedList;

public class FuzzyLogic {

    double d, c, m, k, powerMax;
    double[] insideTemp, tempIncrease, power, Δt, ΔT;
    double xIn, xOut;
    double tempInMissVLowBottom = 0,
            tempInMissVLowTop = 2,
            tempInMissLowBottom = 1,
            tempInMissLowTop = 5,
            tempInMissMediumBottom = 3.5,
            tempInMissMediumTop = 7,
            tempInMissHighBottom = 6,
            tempInMissHighTop = 15,
            tempInMissVHighBottom = 10,
            tempInMissVHighTop = 70;

    double heating_vVerylow, heating_vlow, heating_low, heating_littleLow, heating_medium, heating_littleHigh, heating_High, heating_vHigh, heating_vVeryHigh;
    double tempOutMissingLowBottom, tempOutMissingLowTop, tempOutMissingMediumBottom, tempOutMissingMediumTop, tempOutMissingHighBottom, tempOutMissingHighTop;
    double[] outsideIgnition, insideIgnition;

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

            insideIgnition = new double[5];
            outsideIgnition = new double[3];


//---------------------------------------  R E G U Ł Y    R O Z M Y T E  ------------------------------------------------------------

            xIn = OptT - insideTemp[i];
            xOut = OptT - outsideT[i];

            LinkedList<Rule> ruleListWithIgnite = new LinkedList<>();
            LinkedList<Rule> ruleList;

            // Wyznaczanie reguł z zapłonem

            //wew bardzo mala
            if(affiliationToInsideMissingVeryLow()>0 && affiliationToOutMissingLow() >0){
                ruleListWithIgnite.add(new Rule(1, affiliationToInsideMissingVeryLow(), affiliationToOutMissingLow()));
            }
            if(affiliationToInsideMissingVeryLow() > 0 && affiliationToOutMissingMedium() > 0){
                ruleListWithIgnite.add(new Rule(2, affiliationToInsideMissingVeryLow(), affiliationToOutMissingMedium()));
            }

            if(affiliationToInsideMissingVeryLow() > 0 && affiliationToOutMissingHigh() > 0){
                ruleListWithIgnite.add(new Rule(3, affiliationToInsideMissingVeryLow(), affiliationToOutMissingHigh()));
                //  ruleListWithIgnite.add(new Regula());
            }
            //wew mala
            if(affiliationToInsideMissingLow() > 0 && affiliationToOutMissingLow() > 0){
                ruleListWithIgnite.add(new Rule(4, affiliationToInsideMissingLow(), affiliationToOutMissingLow()));
            }
            if(affiliationToInsideMissingLow() > 0 && affiliationToOutMissingMedium() > 0){
                ruleListWithIgnite.add(new Rule(5, affiliationToInsideMissingLow(), affiliationToOutMissingMedium()));
            }
            if(affiliationToInsideMissingLow() > 0 && affiliationToOutMissingHigh() > 0){
                ruleListWithIgnite.add(new Rule(6, affiliationToInsideMissingLow(), affiliationToOutMissingHigh()));
            }
            //srednia
            if(affiliationToInsideMissingMedium() > 0 && affiliationToOutMissingLow() > 0){
                ruleListWithIgnite.add(new Rule(7, affiliationToInsideMissingMedium(), affiliationToOutMissingLow()));
            }
            if(affiliationToInsideMissingMedium() > 0 && affiliationToOutMissingMedium() > 0){
                ruleListWithIgnite.add(new Rule(8, affiliationToInsideMissingMedium(), affiliationToOutMissingMedium()));
            }
            if(affiliationToInsideMissingMedium() > 0 && affiliationToOutMissingHigh() > 0){
                ruleListWithIgnite.add(new Rule(9, affiliationToInsideMissingMedium(), affiliationToOutMissingHigh()));
            }
            //duza
            if(affiliationToInsideMissingHigh() > 0 && affiliationToOutMissingLow() > 0){
                ruleListWithIgnite.add(new Rule(10, affiliationToInsideMissingHigh(), affiliationToOutMissingLow()));
            }
            if(affiliationToInsideMissingHigh() > 0 && affiliationToOutMissingMedium() > 0){
                ruleListWithIgnite.add(new Rule(11, affiliationToInsideMissingHigh(), affiliationToOutMissingMedium()));
            }
            if(affiliationToInsideMissingHigh() > 0 && affiliationToOutMissingHigh() > 0){
                ruleListWithIgnite.add(new Rule(12, affiliationToInsideMissingHigh(), affiliationToOutMissingHigh()));
            }
            //bardzo duza
            if(affiliationToInsideMissingVeryHigh() > 0 && affiliationToOutMissingLow() > 0){
                ruleListWithIgnite.add(new Rule(13, affiliationToInsideMissingVeryHigh(), affiliationToOutMissingLow()));
            }
            if(affiliationToInsideMissingVeryHigh() > 0 && affiliationToOutMissingMedium() > 0){
                ruleListWithIgnite.add(new Rule(14, affiliationToInsideMissingVeryHigh(), affiliationToOutMissingMedium()));
            }
            if(affiliationToInsideMissingVeryHigh() > 0 && affiliationToOutMissingHigh() > 0){
                ruleListWithIgnite.add(new Rule(15, affiliationToInsideMissingVeryHigh(), affiliationToOutMissingHigh()));
            }

            ruleList = solveConflict(ruleListWithIgnite);

            double metodaCiezkosciLicznik=0;
            double metodaCiezkosciMianownik=0;
            power[i] = 0;
            for (int j=0; j<ruleList.size(); j++){
                metodaCiezkosciLicznik+= ruleWeight(ruleList.get(j));
            }
            for (int j=0; j<ruleList.size(); j++){
                metodaCiezkosciMianownik+=ruleList.get(j).getIgnition();
            }
            if(metodaCiezkosciMianownik!=0) {
                power[i] = metodaCiezkosciLicznik / metodaCiezkosciMianownik;
            }




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

    private double ruleWeight(Rule rule){
        if(rule.getNumber() == 1){
            return rule.getIgnition()*heating_vVerylow;
        }
        else if(rule.getNumber() == 2){
            return rule.getIgnition()*heating_vlow;
        }
        else if(rule.getNumber() == 3){
            return rule.getIgnition()*heating_low;
        }
        else if(rule.getNumber() == 4){
            return rule.getIgnition()*heating_vlow;
        }
        else if(rule.getNumber() == 5){
            return rule.getIgnition()*heating_low;
        }
        else if(rule.getNumber() == 6){
            return rule.getIgnition()*heating_littleLow;
        }
        else if(rule.getNumber() == 7){
            return rule.getIgnition()*heating_medium;
        }
        else if(rule.getNumber() == 8){
            return rule.getIgnition()*heating_medium;
        }
        else if(rule.getNumber() == 9){
            return rule.getIgnition()*heating_littleHigh;
        }
        else if(rule.getNumber() == 10){
            return rule.getIgnition()*heating_littleHigh;
        }
        else if(rule.getNumber() == 11){
            return rule.getIgnition()*heating_High;
        }
        else if(rule.getNumber() == 12){
            return rule.getIgnition()*heating_vHigh;
        }
        else if(rule.getNumber() == 13){
            return rule.getIgnition()*heating_High;
        }
        else if(rule.getNumber() == 14){
            return rule.getIgnition()*heating_vHigh;
        }
        else if(rule.getNumber() == 15){
            return rule.getIgnition()*heating_vVeryHigh;
        }
        else return 0;
    }





    private LinkedList<Rule> solveConflict(LinkedList<Rule> ruleList){
        LinkedList<Rule> newRules = new LinkedList<>();
        ArrayList<Integer> ruleNumbersToDelete = new ArrayList<>();
        int[] conflictVeryLow = isConflict(ruleList, 2,6);
        int[] conflictLow = isConflict(ruleList, 1,5);
        int[] conflictMedium = isConflict(ruleList, 7,8);
        int[] conflictLittleHigh = isConflict(ruleList, 7,12);
        int[] conflictHigh = isConflict(ruleList, 11,15);
        int[] konfliktBDuzo = isConflict(ruleList, 10,14);

        if(conflictVeryLow!= null){
            newRules.add(chooseMethodWithMoreIgnite(ruleList.get(conflictVeryLow[0]), ruleList.get(conflictVeryLow[1])));
            ruleNumbersToDelete.add(ruleList.get(conflictVeryLow[0]).getNumber());
            ruleNumbersToDelete.add(ruleList.get(conflictVeryLow[1]).getNumber());
        }
        if(conflictLow!= null){
            newRules.add(chooseMethodWithMoreIgnite(ruleList.get(conflictLow[0]), ruleList.get(conflictLow[1])));
            ruleNumbersToDelete.add(ruleList.get(conflictLow[0]).getNumber());
            ruleNumbersToDelete.add(ruleList.get(conflictLow[1]).getNumber());
        }
        if(conflictMedium!= null){
            newRules.add(chooseMethodWithMoreIgnite(ruleList.get(conflictMedium[0]), ruleList.get(conflictMedium[1])));
            ruleNumbersToDelete.add(ruleList.get(conflictMedium[0]).getNumber());
            ruleNumbersToDelete.add(ruleList.get(conflictMedium[1]).getNumber());
        }
        if(conflictLittleHigh!= null){
            newRules.add(chooseMethodWithMoreIgnite(ruleList.get(conflictLittleHigh[0]), ruleList.get(conflictLittleHigh[1])));
            ruleNumbersToDelete.add(ruleList.get(conflictLittleHigh[0]).getNumber());
            ruleNumbersToDelete.add(ruleList.get(conflictLittleHigh[1]).getNumber());
        }
        if(conflictHigh!= null){
            newRules.add(chooseMethodWithMoreIgnite(ruleList.get(conflictHigh[0]), ruleList.get(conflictHigh[1])));
            ruleNumbersToDelete.add(ruleList.get(conflictHigh[0]).getNumber());
            ruleNumbersToDelete.add(ruleList.get(conflictHigh[1]).getNumber());
        }

        if(konfliktBDuzo!= null){
            newRules.add(chooseMethodWithMoreIgnite(ruleList.get(konfliktBDuzo[0]), ruleList.get(konfliktBDuzo[1])));
            ruleNumbersToDelete.add(ruleList.get(konfliktBDuzo[0]).getNumber());
            ruleNumbersToDelete.add(ruleList.get(konfliktBDuzo[1]).getNumber());
        }

        for (Integer integer : ruleNumbersToDelete) {
            removeRuleFromList(ruleList, integer);
        }

        ruleList.addAll(newRules);



        return ruleList;
    }

    //zwraca regule o wiekszym zapłonie
    private Rule chooseMethodWithMoreIgnite(Rule rule1, Rule rule2){
        return (rule1.getIgnition() > rule2.getIgnition()) ? rule1 : rule2;
    }

    //usuwa regule o podanym id i zwraca linkedliste
    private void removeRuleFromList(LinkedList<Rule> ruleList, int ruleNumber){
        for(int i = 0; i<ruleList.size();i++){
            if(ruleList.get(i).getNumber() == ruleNumber){
                ruleList.remove(i);
            }
        }
    }

    //zwraca indeksy regul, w ktorych jest konflikt
    private int[] isConflict(LinkedList<Rule> ruleList, int r1test, int r2test){
        int R1index = -1;
        int R2index = -1;

        for(int i=0; i<ruleList.size(); i++){
            if(ruleList.get(i).getNumber() == r1test){
                R1index = i;
            }
            if(ruleList.get(i).getNumber() == r2test){
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
            return slopeFalling(tempInMissVLowTop, xIn, tempInMissLowBottom);
        }
        else return 0;
    }

    private double affiliationToInsideMissingLow(){
        if(insideMissingBetweenVeryLowAndLow()){
            return slopeRising(tempInMissLowBottom, xIn, tempInMissVLowTop);
        }
        else if (insideMissingLow()){
            return 1;
        }
        else if(insideMissingBetweenLowAndMedium()){
            return slopeFalling(tempInMissLowTop, xIn, tempInMissMediumBottom);
        }
        else return 0;
    }

    private double affiliationToInsideMissingMedium(){
        if(insideMissingBetweenLowAndMedium()){
            return slopeRising(tempInMissMediumBottom, xIn, tempInMissLowTop);
        }
        else if(insideMissingMedium()){
            return 1;
        }
        else if (insideMissingBetweenMediumAndHigh()){
            return slopeFalling(tempInMissMediumTop, xIn, tempInMissHighBottom);
        }
        else return 0;
    }
    private double affiliationToInsideMissingHigh(){
        if(insideMissingBetweenMediumAndHigh()){
            return slopeRising(tempInMissHighBottom, xIn, tempInMissMediumTop);
        }
        else if (insideMissingHigh()){
            return 1;
        }
        else if(insideMissingBetweenHighAndVeryHigh()){
            return slopeFalling(tempInMissHighTop, xIn, tempInMissVHighBottom);
        }
        else return 0;
    }
    private double affiliationToInsideMissingVeryHigh(){
        if(insideMissingBetweenHighAndVeryHigh()){
            return slopeRising(tempInMissVHighBottom, xIn, tempInMissHighTop);
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
        return xIn >= tempInMissVLowBottom && xIn < tempInMissLowBottom;
    }
    private boolean insideMissingBetweenVeryLowAndLow(){
        return xIn >= tempInMissLowBottom && xIn < tempInMissVLowTop;
    }
    private boolean insideMissingLow(){
        return xIn >= tempInMissVLowTop && xIn < tempInMissMediumBottom;
    }
    private boolean insideMissingBetweenLowAndMedium(){
        return xIn >= tempInMissMediumBottom && xIn < tempInMissLowTop;
    }
    private boolean insideMissingMedium(){
        return xIn >= tempInMissLowTop && xIn < tempInMissHighBottom;
    }
    private boolean insideMissingBetweenMediumAndHigh(){
        return xIn >= tempInMissHighBottom && xIn < tempInMissMediumTop;
    }
    private boolean insideMissingHigh(){
        return xIn >= tempInMissMediumTop && xIn < tempInMissVHighBottom;
    }
    private boolean insideMissingBetweenHighAndVeryHigh(){
        return xIn >= tempInMissVHighBottom && xIn < tempInMissHighTop;
    }
    private boolean insideMissingVeryHigh(){
        return xIn >= tempInMissHighTop && xIn < tempInMissVHighTop;
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




