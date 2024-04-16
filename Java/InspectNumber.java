import java.util.ArrayList;

public class Bewertungsaufgabe2 {

    public static void main(String[] args) {
        ArrayList<Integer> Eingabe = new ArrayList<Integer>();   // Konsoleneingabe wird in eine ArrayList gespeichert

        if (args.length > 0) {
            In.open(args[0]);
            int x = In.readInt();                                   // Liest Konsole und speichert Werte in eine Integer ArrayLIst
            while (In.done()) {
                Eingabe.add(x);
                x = In.readInt();
            }
            if (Eingabe.isEmpty()) {                                  // Hier wird geprüft ob etwas eingegeben wurde
                Out.println("Es wurde nichts eingegeben und somit wurde keine Berechnung durchgeführt!");
            }
            else{
                if (Eingabe.get(0) <= 0) {                     // Hier wird überprüft ob die Anzahl größer 0 ist
                    Out.println("Die Anzahl der Elemente war nicht größer als 0, es wurden keine Berechnungen duchgeführt!");
                }
                else {
                    if (Eingabe.get(0) < Eingabe.size() - 1 || Eingabe.get(0) > Eingabe.size() - 1) {  // Hier wird ausgeschlossen das n größer oder kleiner als die darauf folgende Folge ist
                        Out.println("Die Anzahl der Elemente war größer/kleiner als n, es wurden keine Berechnungen durchgeführt!");
                    }
                    else {
                        int[] sequence = new int[Eingabe.get(0)];     // Array erstellen
                        for (int i = 1; i < Eingabe.size(); i++) {  // Da ArrayList um ein Element größer als mein Array
                            sequence[i - 1] = Eingabe.get(i);
                        }
                        Out.println("Die Anzahl der negativen Elemente ist: " + countNegatives(sequence));
                        Out.println("Die längste positive Teilfolge hat die Länge: " + longestPositiveSubsequence(sequence));
                        Out.println("Die Teilfolge mit der größten Summe hat die Summe: " + maxPartialSum(sequence));
                    }
                }
            }
        }
        else{
                Out.println("Es wurde keine Datei zum einlesen bereitgestellt, bitte eine Datei in args[] einfügen!");
            }

    }




    static int  countNegatives(int[] array) {
        int anzahlNegativerElemente = 0;
        for (int i = 0; i < array.length; i++){ // Geht das ganze Array durch.
            if( array[i] < 0) {                // Wenn eine negative Zahl auftritt, erhöhe um 1.
                anzahlNegativerElemente++;
            }
        }
      return anzahlNegativerElemente;
     }

    static  int longestPositiveSubsequence (int[] array) {
        int längsteTeilfolge = 0;
        for (int i = 0; i < array.length; i++) {
                int teilfolge = 0;
                for( int j = i; j < array.length; j++){         // Durchlaufen das ganze Array
                    if (array[j] >= 0) {                        // wenn der Eintrag an der Stelle j >= 0 ist
                     teilfolge++;                               // erhöhen wir die länge der Teilfolge um 1
                        if (teilfolge > längsteTeilfolge) {     // ist die Teilfolge größer als die bisher größte
                            längsteTeilfolge = teilfolge;       // so ist sie nun die größte Teilfolge
                        }
                    }
                    else {
                        teilfolge = 0;
                    }
                }
        }
        return längsteTeilfolge;
    }

    static int maxPartialSum (int[] array) {
        int maxTeilsumme = 0;
        for( int i = 0; i < array.length; i++) {
                int teilsumme = 0;
                for(int j = i; j < array.length; j++) {             // Durchlaufen das ganze Array
                    if (array[j] >= 0) {                            // wenn eintrag bei j >= 0 addiere wert bei j auf teilsumme
                        teilsumme += array[j];                      // wenn teilsumme görßer als bisher größte teilsumme
                        if (teilsumme > maxTeilsumme) {             // so ist sie nun die größte Teilsumme
                            maxTeilsumme = teilsumme;
                        }
                    }
                    else {
                        teilsumme = 0;
                    }
                }
        }
        return maxTeilsumme;
    }


}
