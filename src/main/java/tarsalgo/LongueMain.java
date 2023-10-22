package tarsalgo;

import java.nio.file.Path;
import java.time.LocalTime;
import java.util.Scanner;

public class LongueMain {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Lounge lounge = new Lounge();


//      1. Olvassa be és tárolja el az ajto.txt fájl tartalmát!
        lounge.loadFromFile(Path.of("src", "main", "resources", "ajto.txt"));


//      2. Írja a képernyőre annak a személynek az azonosítóját, aki a vizsgált időszakon belül először
//         lépett be az ajtón, és azét, aki utoljára távozott a megfigyelési időszakban!

        LocalTime start = LocalTime.of(9, 45);
        LocalTime end = LocalTime.of(10, 28);

        System.out.println("2. feladat");
        System.out.printf("Az első belépő: %s", lounge.getFirstEntrant(start))
                .println();

        System.out.printf("Az utolsó kilépő: %s", lounge.getLastExit(end))
                .println(System.lineSeparator());


//      3. Határozza meg a fájlban szereplő személyek közül, ki hányszor haladt át a társalgó ajtaján!
//         A meghatározott értékeket azonosító szerint növekvő sorrendben írja az athaladas.txt
//         fájlba! Soronként egy személy azonosítója, és tőle egy szóközzel elválasztva az áthaladások
//         száma szerepeljen

        lounge.writeToFile(Path.of("src", "main", "resources", "athaladas.txt"));


//      4. Írja a képernyőre azon személyek azonosítóját, akik a vizsgált időszak végén a társalgóban
//         tartózkodtak!

        System.out.println("4. feladat");
        lounge.printListInFinish();
        System.out.println(System.lineSeparator());


//      5. Hányan voltak legtöbben egyszerre a társalgóban? Írjon a képernyőre egy olyan időpontot
//         (óra:perc), amikor a legtöbben voltak bent!

        System.out.println("5. feladat");
        lounge.printMostInLoungeTime();
        System.out.println(System.lineSeparator());


//      6. Kérje be a felhasználótól egy személy azonosítóját! A további feladatok megoldásánál ezt
//         használja fel!
//         Feltételezheti, hogy a megadott azonosítóhoz tartozik adat a forrásfájlban.

        System.out.println("6. feladat");
        System.out.printf("Kérek egy azonosítót:".concat(System.lineSeparator()));
        int id = scanner.nextInt();


//      7. Írja a képernyőre, hogy a beolvasott azonosítóhoz tartozó személy mettől meddig
//         tartózkodott a társalgóban!
//         A kiírást az alábbi, 22-es személyhez tartozó példának megfelelően alakítsa ki!
//        11:22-11:27
//        13:45-13:47
//        13:53-13:58
//        14:17-14:20
//        14:57-

        System.out.println(System.lineSeparator().concat("7. feladat"));
        lounge.printInLoungeById(id);


//      8. Határozza meg, hogy a megfigyelt időszakban a beolvasott azonosítójú személy összesen
//         hány percet töltött a társalgóban! Az előző feladatban példaként szereplő 22-es személy
//         5 alkalommal járt bent, a megfigyelés végén még bent volt. Róla azt tudjuk, hogy 18 percet
//         töltött bent a megfigyelés végéig. A 39-es személy 6 alkalommal járt bent, a vizsgált időszak
//         végén nem tartózkodott a helyiségben. Róla azt tudjuk, hogy 39 percet töltött ott. Írja ki,
//         hogy a beolvasott azonosítójú személy mennyi időt volt a társalgóban, és a megfigyelési
//         időszak végén bent volt-e még!

        System.out.println(System.lineSeparator().concat("8. feladat"));
        lounge.printMinutesInLoungeById(id);
    }
}
