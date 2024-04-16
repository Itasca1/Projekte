class Room {                                    // Klasse Room mit den Eigenschaften Raumname, Sitzplatzanzahl,
    String raumname;                           // Computerarbeitsplätze und dem Status der Belegung
    int sitzplätze;
    int computer;
    boolean status;
}
public  class RoomManagment {
    static Room[] room;

    static int roomCounter;

    public static void main(String[] args) {
        if (args.length > 0) {
            In.open(args[0]);
            int x = In.readInt();   // Liest ersten Int Wert der .txt Datei um die Größe von Room[] zu bestimmen
            String name = null;
            int seats = 0;
            int pc = 0;
            room = new Room[x];
            roomCounter = 0;
            while (In.done()) {
                if (roomCounter < room.length) {   // Geht die .txt Datei durch und speichert passende Werte
                    name = In.readString();        // an die Stelle i des Room Array, somit wird ein Array mit
                    seats = In.readInt();          // allen eigenschaften eines Raumes aufgestellt
                    pc = In.readInt();
                    Room rooms = new Room();
                    rooms.raumname = name;
                    rooms.sitzplätze = seats;
                    rooms.computer = pc;
                    if(rooms.sitzplätze == 0 && rooms.computer == 0){  // Wenn der Raum zB wie Lager ist wird er auf belegt gesetzt
                        rooms.status = true;
                    }
                    else{
                        rooms.status = false;
                    }
                    room[roomCounter] = rooms;
                    roomCounter++;
                }
                else {
                    break;
                }

            }
            In.close();                                 // Schließt die Datei damit über die Kommandozeile neue werte eingelesen werden können
            int belegteRäume = 0;                       // Variable zum zählen der belegten Räume
            while (belegteRäume < room.length){
                for(int i = 0; i < room.length; i++) {      // guckt wie viele Räume anfags als belegt zählen
                    if (room[i].status == true) {
                    belegteRäume++;
                    }
                }
                if(belegteRäume<room.length){
                    findBestMatch();
                }
                else if (belegteRäume == room.length) {              // Sind alle Räume belegt folgt der print
                    Out.println("Es sind alle Räume belegt, keine weiteren Anfragen möglich.");
                    return;
                }
                belegteRäume = 0;
            }

        }
        else {
            Out.println("Es wurde keine Datei zum einlesen gefunden!");
        }

    }

    static Room findBestMatch() {
            Out.println("Gebe Sie die benötige Anzahl an Sitzplätzen gefolgt von der Anzahl der Computerarbeitsplätze an: ");
            Out.println("Beenden Sie die Eingabe mit Enter gefolgt von Strg+D");
            int[] suche = new int[2];
            suche[0] = In.readInt();                // Speichert die beiden Eingabevaribalen in ein Array
            suche[1] = In.readInt();
            if (suche[0] == 0) {
                Out.println("Es wurde nach 0 Sitzen gefragt und somit nach keinem Raum gesucht.");    // Suche nach 0 Plätzen abfangen
            }
            else {
                int maxFreeSeats = 0;
                int maxFreePc = 0;
                int bestRoom = -1;
                for (int i = 0; i < room.length; i++) {                 // Bestimmt Maximum von Sitzen und Pc's aller Räume
                    if (room[i].sitzplätze > maxFreeSeats) {
                        maxFreeSeats = room[i].sitzplätze;
                    }
                    if (room[i].computer > maxFreePc) {
                        maxFreePc = room[i].computer;
                    }
                }
                for (int i = 0; i < room.length; i++) {                               // Untersucht ob ein Raum alle gesuchten Kriterien erfüllt und findet dann den besten
                    if (room[i].status == false && room[i].sitzplätze >= suche[0] && room[i].computer >= suche[1]) {
                        int freeSeats = room[i].sitzplätze - suche[0];
                        int freePc = room[i].computer - suche[1];
                        if ((freeSeats < maxFreeSeats && freePc < maxFreePc) || (freeSeats == maxFreeSeats && freePc < maxFreePc) || (freeSeats < maxFreeSeats && freePc == maxFreePc)) {
                            maxFreeSeats = freeSeats;                       //ersetzt den bestRoom soblad einer gefunden wird der weniger sitze benötigt
                            maxFreePc = freePc;
                            bestRoom = i;
                        }
                        else if (freeSeats <= maxFreeSeats && freeSeats + freePc < maxFreeSeats + maxFreePc) {
                            maxFreeSeats = freeSeats;
                            maxFreePc = freePc;                     // bei gleicher oder kleiner anzahl an sitzen, werden noch die gesamtzahl an verschwendet
                            bestRoom = i;                           // Plätzen betrachtet
                        }
                    }
                }
                if (bestRoom == -1) {                                       // Ausgabe falls es kein Match gibt
                    Out.println("Es wurde kein Raum gefunden, der die Kriterien erfüllt!");
                    return null;
                }
                else {                                                                      // gibt den am besten passenden Raum für die Anfrage aus
                     Out.println(room[bestRoom].raumname);
                     room[bestRoom].status = true;
                     return room[bestRoom];
                }
            }
        return null;
    }
}



/*   Beispieleingaben die zum Testen genutzt wurden:

101 0 zuviele sitze
1 26 zuviele pc's
0 0  keine sitze
25 10  gleiche viele sitze-> nach weniger pc's entscheiden
25 0  lieber weniger sitze verschwenden
wdh 100 0 das beim ersten mal Hörsaal 1 kommt und dannach nichts merh gefunden wird :D

 */