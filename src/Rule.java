public class Rule {
    private int number;
    private double internalAffiliation;
    private double outsideAffiliation;
    private double ignition;

    public Rule(int numer, double przynaleznoscDoWewnetrznej, double przynaleznoscDoZewnetrznej){
        this.number = numer;
        this.internalAffiliation = przynaleznoscDoWewnetrznej;
        this.outsideAffiliation = przynaleznoscDoZewnetrznej;
        ignition = wyznaczZaplon();
    }

    private double wyznaczZaplon(){
        return Math.min(internalAffiliation, outsideAffiliation);
    }

    public double getIgnition() {
        return ignition;
    }

    public int getNumber() {
        return number;
    }

}
