class Interval {
    double ug;
    double og;

    public double getUg() {      // holt den floatwert von ug
        return ug;
    }

    public void setUg(double ug){   // setzt ug auf gewünschten float
        this.ug = ug;
    }

    public double getOg() {     // holt den floatwert von og
        return og;
    }

    public void setOg(double og){    // setzt Og auf gewünschten Float
        this.og = og;
    }

    public boolean isEmpty() {               // Prüft ob das Intervall leer ist
        if(ug == 0 && og == 0) {
            return true;
        }
        return false;
    }

    public Interval (double w, double t){    // Erstellt ein Intervall in dem passenden Größenverhältnis von Ug und Og
        if(t>0){                             // Prüft ob t>0 ist
            double ug = w - t;
            double og = w + t;
            if (ug <= og) {                      // Prüft ob ug <= og gilt
                this.ug = ug;
                this.og = og;
            }
            else {
                this.ug = og;
                this.og = ug;
            }
        }
        else{
            Out.println("Toleranz t muss > 0 sein, für den Fall t=0 bitte den anderen Konstruktor verwenden");
        }
    }

    public Interval (double w){         // Erstellt ein Intervall für präzise Rechnungen
        this.ug = w;
        this.og = w;
    }

    public Interval copyInterval() {   //Kopiert ein Intervall
        double middle = (this.ug+this.og)/2;
        double t2 = this.og-middle;
        return new Interval(middle, t2);
    }

    public void negate(Interval a){      // Negiert die Vorzeichen eines Intervalls
        double w1 = a.getUg();
        double t1 = a.getOg();
        a.setUg(-t1);
        a.setOg(-w1);
    }

    public void add(Interval a){     // Addiert 2 Intervalle
        this.ug= this.ug +a.getUg();
        this.og= this.og +a.getOg();
    }

    public void sub(Interval a){ // Subtrahiert 2 Intervalle
        negate(a);
        add(a);
    }

    public void mult(Interval a) {  // Multipliziert 2 Intervalle
        double min = this.ug*a.getUg();
        double max = this.og*a.getOg();
        if(this.ug*a.getUg()<min){
            min = this.ug*a.getUg();
        }
        if(this.ug*a.getOg()<min){
            min = this.ug*a.getOg();
        }
        if(this.og*a.getUg()<min){
            min = this.og*a.getUg();
        }
        if(this.og*a.getOg()<min){
            min = this.og*a.getOg();
        }
        if(this.ug*a.getUg()>max){
            max = this.ug*a.getUg();
        }
        if(this.ug*a.getOg()>max){
            max = this.ug*a.getOg();
        }
        if(this.og*a.getUg()>max){
            max = this.og*a.getUg();
        }
        if(this.og*a.getOg()>max){
            max = this.og*a.getUg();
        }
        this.og= max;
        this.ug = min;


    }

    public boolean contains(double x, Interval a){  // Prüft ob ein float in dem Intervall liegt
        if(x >= a.getUg() && x <= a.getOg()){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean neverEquals(Interval a){    // Prüft ob sich die beiden Intervalle niemals überschneiden
        if(a.getOg()<this.ug || a.getUg()>this.og){ // Wenn OG des Vergleichsintervalls < UG ist könnnen keine Werte übereinstimmen
            return true;                            // Und wenn die UG des Vergleichsintervalls > als die OG ist können auch keine Werte übereinstimmen
        }
        else {
            return false;
        }
    }

    public boolean possiblyEquals(Interval a){          //Prüft ob sich die beiden Intervalle überschneiden
        if(a.getUg()<= this.og && this.og<=a.getOg()){   //Obere Grenze liegt in dem neuen Vergleichsintervall
            return true;
        }
        else if(a.getUg()<=this.ug && this.ug<= a.getOg()){  // Untere Grenze liegt in dem Vergleichsintervall
            return true;
        }
        else {
            return false;
        }
    }

    public boolean neverGreaterThan(Interval a){  //Prüft ob alle werte kleiner als das Vergleichsintervall sind
        if(this.og < a.getUg()){                  // Wenn die OG kleiner als die UG des Vergleichsintervalls ist, kann es keine Punkte geben die größer sind
            return true;
        }
        else{
            return false;
        }
    }

    public boolean possiblyGreaterThan(Interval a){   //Prüft ob es größere Werte als im Vergleichsintervall geben kann
        if(a.getOg()<= this.og){   // Wenn die OG des Vergleichsintervalls <  OG ist, könnnen größere werte auftreten
            return true;
        }
        else {
           return false;
        }
    }

    public String asString(){            // Returned einen String des Intervalls
        if(isEmpty()== true){
            return "[]";
        }
        return ("["+this.getUg()+".."+this.getOg()+"]");
    }

}

public class WoodShelfShipment {

    public static void main (String[] args) {
        Out.println("Zum beenden geben Sie exit ein um die Versandart eines Schranks zu klären geben Sie bitte schrank ein");
        while (In.done()) {
            String input = In.readLine();
            if (input.equals("exit")||input.equals("Exit")) {
                return;
            }
            else if (input.equals("schrank")||input.equals("Schrank")) {
                Out.println("Geben Sie die Höhe, Breite, Tiefe und die Stärke der Bretter in cm, sowie die Anzahl an Fächern an:");
                double hight = In.readDouble();         // Liest Eingabe ein und speichert die Werte
                double width = In.readDouble();
                double depth = In.readDouble();
                double thickness = In.readDouble();
                int drawers = In.readInt();
                double weightPerCubicCM = 0.00072;
                if (hight > 0 && width > 0 && depth > 0 && thickness > 0 && drawers > 0) {
                    Out.println("Ihre Eingabe: "+hight+"cm Höhe "+width+"cm Breite "+ depth+" cm Tiefe "+thickness+" cm Stärke "+drawers+" Schubladen");
                    Interval shelfHeight = new Interval(hight, 0.5);     // Erstelle alle benötigten Intervalle mit der dazugehörigen Toleranz
                    Interval shelfWidth = new Interval(width, 0.5);
                    Interval shelfDepth = new Interval(depth, 0.5);
                    Interval shelfThickness = new Interval(thickness, 0.1);
                    Interval shelfDrawers = new Interval(drawers);
                    Interval shelfWalls = new Interval(2);
                    Interval shelfWeightCal = new Interval(weightPerCubicCM);
                    Interval shippingDecision = new Interval(25);
                    Interval thicknessOriginial = new Interval(0.5);
                    shelfThickness.add(shelfThickness); //Breite verdoppeln 2
                    shelfWidth.sub(shelfThickness);  // Breite minus Stärke(akutell 2*Stärke) um passend zu verringern
                    shelfThickness.negate(shelfThickness);  //Stärke wieder negieren um wieder postiiv zu sein
                    shelfThickness.mult(thicknessOriginial);  // Stärke /2
                    shelfHeight.mult(shelfDepth);    // Höhe * Tiefe
                    shelfHeight.mult(shelfWalls);    // Cm^2 von einer Seite mal Seiten anzahl
                    shelfWidth.mult(shelfDepth);     // Breite * Tiefe
                    shelfWidth.mult(shelfDrawers);   // Cm^2 von einem Fach * Anzahl Fächer
                    shelfWidth.add(shelfHeight);     // cm^2 Seite + cm^2 Fächer
                    shelfWidth.mult(shelfThickness);   // cm^2 (Seite+Fächer)* Stärke bretter = cm^3 gesamter Schrank
                    shelfWidth.mult(shelfWeightCal);  // cm^3 * gewicht pro cm^3 = Gesamtgewichtschrank
                    if (shelfWidth.neverGreaterThan(shippingDecision)) {
                        Out.println("Schrank wird als Paket verschickt, Gewicht liegt zwischen: " + shelfWidth.asString() + " kg. Schreiben Sie exit zum beenden und schrank für die nächste Anfrage.");
                    }
                    else if (shippingDecision.possiblyEquals(shelfWidth)) {
                        Out.println("Versandart unklar, Gewicht liegt zwischen: " + shelfWidth.asString() + " kg. Schreiben Sie exit zum beenden und schrank für die nächste Anfrage.");
                    }
                    else if (shippingDecision.neverGreaterThan(shelfWidth)) {
                        Out.println("Schrank wird mit einer Spedition versand, Gewicht liegt zwischen: " + shelfWidth.asString() + " kg. Schreiben Sie exit zum beenden und schrank für die nächste Anfrage.");
                    }
                }
                else {
                    Out.println("Einer der Eingabe Parameter war <= 0 bzw Anzahl der Schubladen <0, solche Schränke existieren nicht. Schreiben Sie exit zum beenden und schrank für die nächste Anfrage");
                }
            }
        }

    }
}
// Ab einer Breite von 75 cm wird das Paket per Spedition versandt und ab einer Breite von 54,5 cm als Paket (Breite vorher war 60 cm)