import java.util.HashMap;
import java.util.HashSet;

public class Friends {

    public static void main(String[] args) {
        HashMap<String, User> socialNetwork = new HashMap<String, User>();   //HashMap von Username zu User Objekt
        if (args.length > 0) {
            In.open(args[0]);
            while (In.done()) {
                String line = In.readLine();
                if (!line.equals("")) {    //Null Pointer Exception abfangen
                    String[] lineSplit = line.split(" ");  //Line in ein Streing Array Splitten um User und dessen Freund zu erhalten
                    String username = lineSplit[0];
                    String usersFriend = lineSplit[1];
                    if (!socialNetwork.containsKey(username)) {       //Checkt ob der Neue Nutzer und sein Freund schon im Netzwerk sind, und fügt sie hinzu falls nicht
                        socialNetwork.put(username, new User(username));
                    }
                    if (!socialNetwork.containsKey(usersFriend)) {
                        socialNetwork.put(usersFriend, new User(usersFriend));
                    }
                    User username1 = socialNetwork.get(username); //EmpfängerObjekt für username und usersFriend
                    User usersFriend1 = socialNetwork.get(usersFriend);
                    username1.addFriend(usersFriend1);    //Freund dem HashSet von Username hinzufügen
                }
            }
            In.close();
            Out.println("Geben Sie einen Namen an, nachdem gesucht werden soll: ");  //Fordert Namen an nachdem gescuht werden soll
            String input = In.readLine();
            String name = input.toUpperCase();
            while(!name.equals("ENDE")) {
                if (socialNetwork.containsKey(name)) {//Kommt der Name im Netzwerk vor, dann wird findLinkedFriends mit der maximalen Link grenze von 2 ausgeführt
                    User wantedNetwork = socialNetwork.get(name);
                    if (wantedNetwork.getUserFriends().isEmpty()) {   //Gibt es keine weiteren Freunde
                        Out.println(wantedNetwork.getUsername() + " hat keine Freinde.");
                    }
                    else {
                        wantedNetwork.findLinkedFriends(1, 2);
                    }
                }
                else if (!socialNetwork.containsKey(name)) {
                    Out.println("Name wurde im Netzwerk nicht gefunden.");
                }
                Out.println("");
                Out.println("Wenn Sie einen weiteren Namen suchen wollen geben Sie diesen ein, ansonsten geben Sie Ende ein.");
                input = In.readLine();
                name = input.toUpperCase();
            }
        }
    }
}


class User {
    private String username;  //Varibale für Nutzer
    private HashSet<User> userFriends;  //HashSet um verlinkte Freunde festzuhalten

    public User(String newUser){  // Konstruktor
        username = newUser;
        userFriends = new HashSet<User>();
    }

    public void setUsername(String username) {   //Setter Methode für username
        this.username = username;
    }

    public String getUsername() {    //Getter Methode für username
        return username;
    }

    public HashSet<User> getUserFriends() {  //Getter methode HashSet
        return userFriends;
    }

    public void setUserFriends(HashSet<User> userFriends) {   //Setter Methode für HashSet
        this.userFriends = userFriends;
    }

    public void addFriend(User newFriend){   //Fügt neuen Freund zu dem HashSet hinzu
        userFriends.add(newFriend);
    }

    public void findLinkedFriends(int currentLinks, int maximumLinks){
        for(User userFriend : userFriends){
            Out.println(username + " ist befreundet mit "+ userFriend.getUsername()+" ("+currentLinks+")");       //Zeigt alle direkt Freunde für User
            if(currentLinks<maximumLinks){                                                  //Führt Methode rekursiv mit gefundem Freund aus und zeigt dessen direkt Freunde
                userFriend.findLinkedFriends(currentLinks+1, maximumLinks);         // Dies geht rekursiv weiter bis zur gewünschten anzahl an maximalen Links
            }
        }
    }
}