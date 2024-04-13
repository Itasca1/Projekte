import java.util.*;
import java.sql.*;
import java.io.*;

public class MIN_HUE_3 {
    static Connection CONNECTION = null;

    public static void main(String[] args) {
        try {
            System.out.print("Verbinde: D-Bank ... ");
            setup_database();
            System.out.println("FERTIG");

            System.out.print("Erstelle: Schema ... ");
            boolean erfolg = build_schema();
            System.out.println("ERFOLG: " + (erfolg ? "Ja" : "Nein"));

            System.out.print("Erstelle: Zeilen ... ");
            int zeilen = fill_database();
            System.out.println("ANZAHL: " + zeilen);

            System.out.print("Ermittle: Routen ... ");
            int routen = connection(5, "Rathaus", "Universität");
            System.out.print("(GESAMT: " + routen + ")");

            CONNECTION.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static void setup_database() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        CONNECTION = DriverManager.getConnection("jdbc:mysql://localhost/mobiel", "root", "NunuRini33649");

        Statement statement = CONNECTION.createStatement();
        statement.executeUpdate("DROP TABLE IF EXISTS Station, Linie, Auskunft, Fahrplan;");
        statement.close();
    }

    // Aufgabe 4 (3 Punkte)
    public static boolean build_schema() {
        boolean result = true;

        //--------------------------------------------------
        try {
            PreparedStatement tableOne = CONNECTION.prepareStatement("CREATE TABLE station(" +
                    "Stationsname VARCHAR(30) NOT NULL," +
                    "Bahnhof BOOLEAN NOT NULL," +
                    "Barrierefreiheit BOOLEAN NOT NULL," +
                    "Unterirdisch BOOLEAN NOT NULL," +
                    "PRIMARY KEY (Stationsname)" +
                    ")");
            PreparedStatement tableTwo = CONNECTION.prepareStatement("CREATE TABLE linie(" +
                    "Anfangsstation VARCHAR(30) NOT NULL," +
                    "Zwischenstationen VARCHAR(100) NOT NULL," +
                    "Endstation VARCHAR(30) NOT NULL," +
                    "Liniennummer INT NOT NULL," +
                    "Farbe VARCHAR(30) NOT NULL," +
                    "PRIMARY KEY (Liniennummer)" +
                    ");");
            PreparedStatement tableThree = CONNECTION.prepareStatement("CREATE TABLE auskunft(" +
                    "Liniennummer INT NOT NULL," +
                    "Richtung VARCHAR(30) NOT NULL," +
                    "Stationsname VARCHAR(30) NOT NULL," +
                    "`Minute` INT NOT NULL," +
                    "PRIMARY KEY (Richtung, `Minute`, Liniennummer, Stationsname)," +
                    "FOREIGN KEY ( Liniennummer ) REFERENCES Linie (Liniennummer) ON DELETE CASCADE ON UPDATE CASCADE," +
                    "FOREIGN KEY (Stationsname ) REFERENCES Station (Stationsname) ON DELETE CASCADE ON UPDATE CASCADE," +
                    "CHECK ( `Minute` >= 0 AND `Minute` < 60)" +
                    ");");
            PreparedStatement tableFour = CONNECTION.prepareStatement("CREATE TABLE fahrplan(" +
                    "Richtung VARCHAR(30) NOT NULL," +
                    "Stationsname VARCHAR(30) NOT NULL," +
                    "Stationsnummer INT NOT NULL," +
                    "Liniennummer INT NOT NULL," +
                    "PRIMARY KEY (Richtung, Stationsname, Liniennummer)," +
                    "FOREIGN KEY ( Liniennummer ) REFERENCES Linie (Liniennummer) ON DELETE CASCADE ON UPDATE CASCADE," +
                    "FOREIGN KEY (Stationsname ) REFERENCES Station (Stationsname) ON DELETE CASCADE ON UPDATE CASCADE" +
                    ");");
            tableOne.executeUpdate();
            tableTwo.executeUpdate();
            tableThree.executeUpdate();
            tableFour.executeUpdate();
        } catch (SQLException e) {
            result = false;
            e.printStackTrace();
        }


        //--------------------------------------------------

        return result;
    }


    // Aufgabe 5 (12 Punkte)
    public static int fill_database(){
        int result = 0;

        //--------------------------------------------------

        List<Map<String, String>> data = mapData("plan_data.csv");
        try {
            PreparedStatement statement = CONNECTION.prepareStatement(" INSERT IGNORE INTO  station (Stationsname, Bahnhof, Barrierefreiheit, Unterirdisch) " +
                    "values (?,?,?,?)");
            PreparedStatement statement1 = CONNECTION.prepareStatement("INSERT IGNORE INTO linie (Anfangsstation, Zwischenstationen, Endstation, Liniennummer, Farbe)" +
                    "values (?, ?, ?, ?, ?)");
            PreparedStatement statement2 = CONNECTION.prepareStatement("INSERT INTO auskunft (Liniennummer, Richtung, Stationsname, Minute)" +
                    "values(?, ?, ?, ?)");
            PreparedStatement statement3 = CONNECTION.prepareStatement("INSERT IGNORE INTO fahrplan (Richtung, Stationsname, Stationsnummer, Liniennummer)" +
                    "values (?, ?, ?, ?)");
            for(Map<String, String> e : data) {
                statement.setString(1, e.get("Station"));
                statement.setBoolean(2, Boolean.parseBoolean(e.get("Bahnhof")));
                statement.setBoolean(3, Boolean.parseBoolean(e.get("Barrierefrei")));
                statement.setBoolean(4, Boolean.parseBoolean(e.get("Unterirdisch")));
                if(statement.executeUpdate()>0) {
                    result++;
                }
                String linie = e.get("LinienName");
                String[] linienname = linie.split("-");
                String anfangsstation = linienname[0];
                String zwischenstationen = "";
                for (int i = 1; i < linienname.length - 1; i++) {
                    if(i == linienname.length-2) {
                        zwischenstationen += linienname[i];
                    } else {
                        zwischenstationen += linienname[i] + "-";
                    }
                }
                String endstation = linienname[linienname.length-1];
                statement1.setString(1, anfangsstation);
                statement1.setString(2, zwischenstationen);
                statement1.setString(3, endstation);
                statement1.setInt(4, Integer.parseInt(e.get("LinienNummer")));
                statement1.setString(5, e.get("Farbe"));
                if(statement1.executeUpdate() > 0) {
                    result++;
                }
                statement2.setInt(1, Integer.parseInt(e.get("LinienNummer")));
                statement2.setString(2, e.get("Richtung"));
                statement2.setString(3, e.get("Station"));
                statement2.setInt(4, Integer.parseInt(e.get("Minute")));
                if(statement2.executeUpdate() > 0) {
                    result++;
                }
                statement3.setString(1, e.get("Richtung"));
                statement3.setString(2, e.get("Station"));
                statement3.setInt(3, Integer.parseInt(e.get("StationNummer")));
                statement3.setInt(4, Integer.parseInt(e.get("LinienNummer")));
                if(statement3.executeUpdate() > 0) {
                    result++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // TODO: YOUR CODE HERE

        //--------------------------------------------------

        return result;
    }

    // Aufgabe 9 (4 Punkte)
    public static int connection(int bereitMinute, String startStation, String zielStation) {
        int result = 0;

        //--------------------------------------------------
        ResultSet rs;
        try {
        PreparedStatement statement = CONNECTION.prepareStatement("""
            SELECT Start_Station, Start_Minute, test.Liniennummer, test.Richtung AS Richtung,  Ziel_Minute, Ziel_Station
            FROM
                (SELECT DISTINCT Start_Station, Start_Minute, auskunft.Liniennummer, final.Richtung AS Richtung, auskunft.Minute AS Ziel_Minute, Ziel_Station
                FROM auskunft,
                      (SELECT Distinct Start_Station, auskunft.Minute AS Start_Minute, auskunft.Liniennummer, streckenstationen.Richtung, streckenstationen.Stationsname, streckenstationen.Stationsnummer,  Ziel_Station
                    FROM(
            
                               SELECT fahrplan.Stationsname, fahrplan.Stationsnummer, fahrplan.Richtung
                               FROM fahrplan
                               WHERE fahrplan.Richtung = (SELECT startstation.Richtung
                                                          FROM
                                                              (
            
                                                                  SELECT fahrplan.Stationsnummer, fahrplan.Richtung
                                                                  FROM fahrplan
                                                                  WHERE Stationsname = ?
                                                                  ORDER BY Stationsnummer
                                                              ) startstation, (SELECT fahrplan.Stationsnummer, fahrplan.Richtung
                                                                               FROM fahrplan
                                                                               WHERE Stationsname = ?
                                                                               ORDER BY Stationsnummer DESC
                                                              ) zielstation
                                                          WHERE startstation.Stationsnummer < zielstation.Stationsnummer && startstation.Richtung = zielstation.Richtung) && fahrplan.Stationsnummer <= (SELECT fahrplan.Stationsnummer
                                                                                                                                                                                                         FROM fahrplan
                                                                                                                                                                                                         WHERE Stationsname = ?
                                                                                                                                                                                                         ORDER BY Stationsnummer DESC
                                                                                                                                                                                                         Limit 1) && fahrplan.Stationsnummer >= (SELECT fahrplan.Stationsnummer
                                                                                                                                                                                                                                                 FROM fahrplan
                                                                                                                                                                                                                                                 WHERE Stationsname = ?
                                                                                                                                                                                                                                                 ORDER BY  Stationsnummer
                                                                                                                                                                                                                                                 LIMIT 1)
                              ORDER BY Stationsnummer) streckenstationen
                              NATURAL JOIN auskunft
                              JOIN (SELECT fahrplan.Stationsname as Start_Station
                                     FROM fahrplan
                                     WHERE Stationsname = ?) startstation
                               JOIN  (SELECT fahrplan.Stationsname as Ziel_Station
                                      FROM fahrplan
                                      WHERE Stationsname = ?) Ziel_Station
                       WHERE (Stationsname = ? && auskunft.Minute >= ?)) final
                 WHERE auskunft.Stationsname = ? && auskunft.Minute >= final.Start_Minute && auskunft.Richtung = final.Richtung
                 ORDER BY Start_Minute, Ziel_Minute) as test
            GROUP BY Start_Minute HAVING COUNT(Start_Minute = 1)
            """);
        statement.setString(1, startStation);
        statement.setString(2, zielStation);
        statement.setString(3, zielStation);
        statement.setString(4, startStation);
        statement.setString(5, startStation);
        statement.setString(6, zielStation);
        statement.setString(7, startStation);
        statement.setInt(8, bereitMinute);
        statement.setString(9, zielStation);
        rs = statement.executeQuery();
        if(rs.next()) {
            System.out.println("Schnellste Verbindung: " + rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4)
                    + " " + rs.getString(5) + " " + rs.getString(6));
            result++;
        } else {
            System.out.println("Es gibt keine Verbindungen");
        }
       while(rs.next()) {
           result++;
       }
        // TODO: YOUR CODE HERE

        //--------------------------------------------------


    } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    // ESL-Zettel 0: Aufgabe 2.1 (Hilfs-Funktion)
    public static List<String[]> readCSV(String fileName) {
        List<String[]> result = new ArrayList<>();

        //--------------------------------------------------

        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader(fileName)
            );

            String line = "";
            while((line = reader.readLine()) != null)
                result.add(line.split(","));

            reader.close();
        } catch(IOException e) {
            e.printStackTrace();
        }

        //--------------------------------------------------

        return result;
    }

    // ESL-Zettel 0: Aufgabe 2.2 (Hilfs-Funktion)
    public static List<Map<String, String>> mapData(String fileName) {
        List<Map<String, String>> result = new ArrayList<>();

        //--------------------------------------------------

        List<String[]> file = readCSV(fileName);
        String[] keys = file.remove(0);

        for(String[] line : file) {
            Map<String, String> map = new HashMap<>();

            for(int i = 0; i < keys.length; i++)
                map.put(keys[i], line[i]);

            result.add(map);
        }

        //--------------------------------------------------

        return result;
    }
}