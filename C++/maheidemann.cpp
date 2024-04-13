#include <iostream>
#include <iomanip> 
#include <cmath>
using namespace std;

float harmonischReiheVorwaertsf(long n) {
        float sum = 0.0f;
        int i = 1;
        while (i < n+1) {
            sum += 1.0f/i;
            i++;
        }
        return sum;
    }

float harmonischeReiheRueckwertsf(long n) {
        float sum = 0.0f;
            int i = n;
            while (i > 0) {
                sum += 1.0f/i;
                i--;
            }
            return sum;
}

double harmonischReiheVorwaertsd(long n) {
        double sum = 0.0;
        int i = 1;
        while (i < n+1) {
            sum += 1.0/i;
            i++;
        }
        return sum;
    }

double harmonischeReiheRueckwertsd(long n) {
        double sum = 0.0;
        int i = n;
        while (i > 0) {
            sum += 1.0/i;
            i--;
        }
        return sum;
}

double fac(int n) {
    double result = 1.0;
    int i = 1;
    while(i <= n) {
        result *= i;
        i++;
    }
    return result;
}


int main() {
    //Aufgabe 1:

    long n1 = 10000000;
    long n2 = 100000000;
    

    //Berechne der harmonischen Reihe vorwärts
    cout.precision(15); 
    
    float result = harmonischReiheVorwaertsf(n1); 
    cout << "Summe vorwärts (float), n=10^7: " << result << "\n" << endl;

    result = harmonischeReiheRueckwertsf(n1);
    cout  << "Summe rückwärts (float), n=10^7: " << result << "\n"  << endl;

    result = harmonischReiheVorwaertsf(n2);
    cout  << "Summe vorwärts (float), n=10^8: " << result << "\n" << endl;

    result = harmonischeReiheRueckwertsf(n2);
    cout  << "Summe rückwärts (float), n=10^8: " << result << "\n" << endl;

    double result2 = harmonischReiheVorwaertsd(n1);
    cout  << "Summe vorwärts (double), n=10^7: " << result2 << "\n" << endl;

    result2 = harmonischeReiheRueckwertsd(n1);
    cout  << "Summe rückwärts (double), n=10^7: " << result2 << "\n"  << endl;

    result2 = harmonischReiheVorwaertsd(n2);
    cout  << "Summe vorwärts (double), n=10^8: " << result2 << "\n" << endl;

    result2 = harmonischeReiheRueckwertsd(n2);
    cout  << "Summe rückwärts (double), n=10^8: " << result2 << "\n" << endl;
    
    cout << "Frage1: Die Ergebnisse wurden eben geprintet. Frage2: Die rückwärts und vorwärts Berechnung, sowie Float und Double varieren." << endl;
    cout <<"Frage3: Die gelitkommaarithmetik ist ungenau und dadurch kommt es zu rundungsfehlern, was in verschiedenen Resultaten endet. Frage4: Rückwärtsberechnung mit double liefert den genausten Wert." << endl;

    //Fragen1: Ergebnisse werden ausgegeben, Frage2: Die vorwärts und rückwärts Berechnung liefert verschiedene Werte, Frage3: Die Gleitkommaarithmetik ist ungenau und 
    // dadurch kommt es zu rundungsfehlern, was in verschiedenen Resultaten endet.
    //Frage4: Rückwärtsberechnung mit doubles liefert den genausten Wert


    //Aufgabe 2:
    //e: 2.718281828459
    double e = 1.0; //Startwert gleich1, da 0! = 1
    double term = 1.0; 
    int k = 1;

    while (k <= 16) { //Berechnet die Formel, startet bei 1, da Startwert(1/0! = 1) schon gegeben ist.
        term *= 1.0 / k; 
        e += term; 
        k++;
    }

    cout.precision(15); 
    // Ausgabe mit 15 Nachkommastellen
    cout  << "e = " << e << "\n" << endl;
    //Frage1: Kleinster Wert für die Berechung von e, die auf  12 Stellen  genau ist, ist n = 16.

    //pi: 3.141592653589
    int i = 1;
    double product = 0.5; //Startwert

    while (i <= 21) { //i = 1, da startwert bei n = 0 ist 
        double term = sqrt(2.0);
        int j = 1;
        while(j < i) {
            term = sqrt(2.0 + term);
            j++;
        }
        product *= term / 2.0;
        i++;
    }
    double pi1 = 1 / product;

    cout << "Pi mit der ersten Formel: " << pi1 << "\n" << endl;

    //Frage2: Kleinster Wert für die Berechnung von pi, die auf 12 Stellen genau ist, ist n = 21. (Start bei n = 0  mit dem wert 0.5)

    int z = 0;
    double sum = 0.0;


    while (z <= 1) {
        sum += (fac(4*z)*(1103+26390*z)) / (pow(fac(z), 4)*(pow(396, 4*z)));
        z++;
    }
    double pi2 = 1 / (((2*sqrt(2))/9801) * sum);

    cout << "Pi mit der zweiten Formel:" << pi2 << "\n" << endl;

    cout << "Die kleinsten n sind für e: 16 , für pi mit der ersten Variante: 22 und für pi mit der zweiten variante: 1" << endl;
    //Frage3: Kleinster Wert für die Berechnung von pi, auf dieser Art und Weise,  ist n = 1.
}