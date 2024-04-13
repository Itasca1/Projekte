import java.util.ArrayList;
import java.util.List;

class Stack {
    int[] data;
    int top;

    Stack (int size) {
        data = new int[size];
        top = -1;
    }

    void push (int x) {
        if (top >= data.length-1)
            Out.println("Stack overflow!");
        else data[++top] = x;
    }

    int pop () {
        if (top < 0) {
            Out.println("Stack underflow!");
            return 0;
        }
        else {
            return data[top--];
        }
    }

    boolean empty () {
        return top < 0;
    }
}

public class PostfixArithmetik {

    public static void main(String[] args){

        Out.println("Geben Sie einen arithmetischen Ausdruck in Postfix-Notation ein");
        while(In.done()){
            String postfix = In.readLine();     // liest Eingabe ein
            if(postfix.equals("exit")){   // Abbruch Bedingung
                return;
            }
            if(postfix.isEmpty()){
                Out.println("Keine Eingabe erfolgt!");
                return;
            }
            String postfixClean1 = postfix.trim();
            String postfixClean = postfixClean1.replaceAll(" +"," ");             //  löscht wiederholte leerzeichen
            String[] postfixSplit = postfixClean.split(" ");// Splittet die Eingabe bei " "
            List<String> input = new ArrayList<>();
            for(int i = 0; i < postfixSplit.length; i++){

                if(postfixSplit[i].chars().anyMatch(Character::isAlphabetic) == true){   // Überprüft ob die Eingabe Buchstaben enthält und beendet, falls dies der Fall ist
                    Out.println("Es wurden Buchstaben eingegeben, somit können keine Berechnungen durchgeführt werden!");
                    return;
                }
                else {
                    input.add(postfixSplit[i]);     // Fügt Wert dem  Array hinzu
                }
            }
            manageString(input);
        }

    }

    static void manageString (List<String> a) {
        Stack stack = new Stack(a.size());
        for(int i = 0; i < a.size(); i++) {
            String x = a.get(i);
            boolean isNumb = x.chars().allMatch(Character::isDigit );
                if (x.equals("+")){              // Addition, obersten so pop, dann addieren und ergebnis push
                    int sum1 = stack.pop();
                    int sum2 = stack.pop();
                    int summe = sum1 + sum2;
                    stack.push(summe);
                }
                else if (x.equals("-")) {      // Differenz, obersten beiden pop, dann subtrahieren und ergebnis push
                    int sub1 = stack.pop();
                    int sub2 = stack.pop();
                    int diff = sub2 - sub1;
                    stack.push(diff);
                }
                else if (x.equals("=")) {     // bei match mit "=" oberste element ausgeben
                    Out.println(stack.pop());
                }
                else if (x.equals("/")) {     // Division, obersten beiden pop, dann dividieren und ergebnis push
                    int div1 = stack.pop();
                    int div2 = stack.pop();
                    if(div1 == 0){
                        Out.println("Teilen durch 0 ist nicht erlaubt.");
                        return;
                    }
                    else {
                        int division = div2 / div1;
                        stack.push(division);
                    }
                }
                else if (x.equals("*")) {    // Multiplikation, obersten beiden pop , dann multiplizieren und ergebnis push
                    int fac1 = stack.pop();
                    int fac2 = stack.pop();
                    int product = fac1 * fac2;
                    stack.push(product);
                }
                else if (isNumb == true ){     // wenn x auf true ist, wird es als integer erkannt und oben auf den stack gepackt
                    Integer numb = Integer.valueOf(x);
                    stack.push(numb);
                }
                else if(x.equals(":")){      // Duplizieren, oberstes element op und dann 2 mal push damit es 2 mal im stack landet
                    int dubli = stack.pop();
                    stack.push(dubli);
                    stack.push(dubli);
                }
        }
    }
}