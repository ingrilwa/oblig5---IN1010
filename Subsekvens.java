public class Subsekvens {
  
    public final String subsekvens;
    public int forekomster = 1;

    public Subsekvens(String subsekvens) {
        this.subsekvens = subsekvens;
    }

    public String hentSubsekvens() {
        return subsekvens;
    }

    public int hentforekomster() {
        return forekomster;
    }

    public void settnullForekomst() {
        forekomster = 0;
    }

    @Override
    public String toString() {
        return "(" + subsekvens + ", " + forekomster + ")";
    }
}
