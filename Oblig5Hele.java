import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class Oblig5Hele {

    public static void main(String[] args) {

        final long startTime = System.nanoTime();
        Monitor2 sykdomT = new Monitor2();
        Monitor2 sykdomF = new Monitor2();
        String lokasjon = "data";
        File filen = new File(lokasjon + "/metadata.csv");
        Scanner metaLeser = null;
        ArrayList<String> falseArr = new ArrayList<>();
        ArrayList<String> trueArr = new ArrayList<>();
        ArrayList<Thread> threads = new ArrayList<>();

        try {
            metaLeser = new Scanner(filen);
        }
        catch (Exception e) {
        }

        while (metaLeser.hasNextLine()) {
            String holder = metaLeser.nextLine();
            String[] split = holder.split(",");
            System.out.println("leser fil: " + split[0] + " . . .");
            if (split[1].equals("True")) {
                trueArr.add(split[0]);
                LeseTrad testtrad = new LeseTrad(lokasjon + "/" + split[0], sykdomT);
                Thread trad = new Thread(testtrad);
                threads.add(trad);
                trad.start();
            } else if (split[1].equals("False")) {
                falseArr.add(split[0]);
                LeseTrad testtrad = new LeseTrad(lokasjon + "/" + split[0], sykdomF);
                Thread trad = new Thread(testtrad);
                threads.add(trad);
                trad.start();
            } else {
                System.out.println(split[0] + " Hadde ikke True / False i seg");
            }
        }

        for (Thread i : threads) {
            try {
                i.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        threads.clear();
        //System.out.println("Antall maps i sykdomT: " + sykdomT.antallMaps());
        //ystem.out.println("Antall maps i sykdomF: " + sykdomF.antallMaps());

        CountDownLatch nedtellingsykdomT = new CountDownLatch(sykdomT.hentStorrelse() - 1);
        CountDownLatch nedtellingsykdomF = new CountDownLatch(sykdomF.hentStorrelse() - 1);

        // fletting av sykdomT
        for (int i = 0; i < 8; i++) {
            FletteTrad fletteTrad = new FletteTrad(sykdomT, nedtellingsykdomT);
            Thread traad = new Thread(fletteTrad);
            threads.add(traad);
            traad.start();
        }
        // fletting av sykdomF
        for (int i = 0; i < 8; i++) {
            FletteTrad fletteTrad = new FletteTrad(sykdomF, nedtellingsykdomF);
            Thread traad = new Thread(fletteTrad);
            threads.add(traad);
            traad.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        threads.clear();

        //System.out.println("\nsykdomT forekomst:"+ sykdomT.finnHøyestefremkomster(sykdomT.subsekvensRegister.SubsekvensRegister.get(0)));
        //System.out.println("\nsykdomF forekomst:"+ sykdomF.finnHøyestefremkomster(sykdomF.subsekvensRegister.SubsekvensRegister.get(0)));

        HashMap<String, Subsekvens> sykdomTmap = sykdomT.hentHash();
        HashMap<String, Subsekvens> sykdomFmap = sykdomF.hentHash();


        boolean finnes = false;
        ArrayList<Subsekvens> tempSubHolder = new ArrayList<>();

        for (Subsekvens subF : sykdomFmap.values()) {
            tempSubHolder.add(subF);
        }

        for (Subsekvens subT : sykdomTmap.values()) {
            Subsekvens SubHolder = null;
            for (Subsekvens subF : tempSubHolder) {
                if (subF.subsekvens.equals(subT.subsekvens)) {
                    finnes = true;
                }
            }
            if (finnes) {
                finnes = false;
                tempSubHolder.add(subT);
            } else if (!finnes) {
                finnes = false;
                Subsekvens nullSub = new Subsekvens(subT.subsekvens);
                nullSub.settnullForekomst();
                tempSubHolder.add(nullSub);
            }

        }

        //System.out.println("\nAntall subT i sykdomTmap: " + sykdomTmap.size());
        //System.out.println("\nAntall subF i sykdomFmap: " + sykdomFmap.size());
        //System.out.println("\nAntall forskjellige sub i sykdomTmap + sykdomFmap: " + tempSubHolder.size());
        //System.out.println("\nAlle forskjellige Subsekvenser (subF + subT): \n" + tempSubHolder);

        for(Subsekvens subT : sykdomTmap.values()){
            for(Subsekvens subFN : tempSubHolder){
                if(subT.subsekvens.equals(subFN.subsekvens)){
                    subT.forekomster = subT.forekomster - subFN.forekomster;
                }
            }
        }

        final long duration = System.nanoTime() - startTime;
        System.out.println("Nanosekunder brukt: " + duration);
        float durationSec = duration / 1000000000;
        System.out.println("Sekunder brukt: " + durationSec);

        for(Subsekvens subT : sykdomTmap.values()){
            if(subT.forekomster > 6){
                System.out.println(subT.subsekvens + " med " + subT.forekomster + " forekomster");
            }
        }


        //System.out.println(sykdomF.finnHøyestefremkomster(sykdomTmap));


    }
}
