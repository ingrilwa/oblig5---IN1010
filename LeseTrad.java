public class LeseTrad implements Runnable{

    String lokasjon;
    Monitor1 monitor;

    public LeseTrad(String fillokasjon, Monitor1 monitor){
        lokasjon = fillokasjon;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        monitor.settInnHash(monitor.lesFil(lokasjon));
    }

}
