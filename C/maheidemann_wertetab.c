#include <stdio.h>
#include <stdlib.h>
#include <math.h> //Das ist hoffentlich libm.a, mit inclucde libm.a habe ich immer einen Fehler bekommen

void table(double (*function)(double)) {
    double start_val = 0.0; 

    while (start_val <= 10.0) { //Bis max Werterreicht ist, rufe übergebene Funktion mit aktuellem wert auf
        double result = (*function)(start_val);
        printf("(%f, %f) ", start_val, result); //Printen für Ausgabe
        start_val += 0.5;
    }
}

int main() {
    //Ausführne der Funtionen mit Sin(x), Cos(x) und sqrt(x)
    printf("Sin(x): \n");
    table(sin);
    printf("\n");
    
    printf("Cos(x): \n");
    table(cos);
    printf("\n");
    
    printf("sqrt(x): \n");
    table(sqrt);
    printf("\n");

    return 0;
}