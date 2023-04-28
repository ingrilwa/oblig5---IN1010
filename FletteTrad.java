import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

public class FletteTrad implements Runnable {

    Monitor2 monitor2;
    HashMap<String, Subsekvens> runMap;
    CountDownLatch nedtelling;

    public FletteTrad(Monitor2 monitor2, CountDownLatch nedtelling){
        this.monitor2 = monitor2;
        this.nedtelling = nedtelling;
    }

    public HashMap<String, Subsekvens> fletteTo(HashMap<String, Subsekvens> map1, HashMap<String, Subsekvens> map2) {

        HashMap<String, Subsekvens> returnMap = monitor2.flett(map1, map2);
        return returnMap;
    }

    int hentet = 0;
    HashMap<String, Subsekvens> one = null;
    HashMap<String, Subsekvens> two = null;

    @Override
    public void run() {

        while(monitor2.hentStorrelse()>1 && nedtelling.getCount() > 0){
            ArrayList<HashMap<String, Subsekvens>> oppdeling = monitor2.hentToHash();

            try {
                one = oppdeling.get(0); two = oppdeling.get(1);
                runMap = fletteTo(one, two);
                monitor2.settInnHash(runMap);
                nedtelling.countDown();
            }
            catch (NullPointerException e) {
            }
        }

    }


}
