public class Regula {
    private int numer;
    private double przynaleznoscDoWewnetrznej;
    private double przynaleznoscDoZewnetrznej;
    private double zaplon;

    public Regula(int numer, double przynaleznoscDoWewnetrznej, double przynaleznoscDoZewnetrznej){
        this.numer = numer;
        this.przynaleznoscDoWewnetrznej = przynaleznoscDoWewnetrznej;
        this.przynaleznoscDoZewnetrznej = przynaleznoscDoZewnetrznej;
        zaplon = wyznaczZaplon();
    }

    private double wyznaczZaplon(){
        return Math.min(przynaleznoscDoWewnetrznej,przynaleznoscDoZewnetrznej);
    }

    public double getZaplon() {
        return zaplon;
    }

    public int getNumer() {
        return numer;
    }

}
