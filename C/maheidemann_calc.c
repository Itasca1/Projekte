#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>

int main(int argc, char *argv[]) {
    if(argc != 4) { //Check ob 2 floats und 1 operator übergeben wurden (passende Anzahl von Argumenten)
        printf("ERROR: Bitte 2 Floats mit einem der  Operatoren +,-,x und / dazwischen benutzen.");
        return 1;
    }

    if(strlen(argv[2]) != 1) {
        printf("Error: Kein gültiger Operand.");
        return 1;
    }

    char cal_symbol = argv[2][0]; //es ist "operator" und nicht 'operator' deshalb die [0]
    float a = atof(argv[1]);
    float b = atof(argv[3]);
    float result;

    switch (cal_symbol) { //Check für die cases und gebe ergbenis zurück, falls kein Fall zutrifft wirf error wegen operator
            case '+':
                result = a + b;
                printf("%f", result);
                printf("\n");
                break;
            case '-':
                result = a - b;
                printf("%f", result);
                printf("\n");
                break;
            case 'x':
                result = a * b;
                printf("%f", result);
                printf("\n");
                break;
            case '/':
                if(b != 0) {
                    result = a / b;
                    printf("%f", result);
                    printf("\n");
                    break;
                } else {
                    printf("Error: Durch 0 darf nicht geteilt werden.");
                    break;
                }
            default:
                printf("ERROR: Bitte 2 Floats mit einem der  Operatoren +,-,x und / dazwischen benutzen.");
                return 1;
                break;
        }
    return 0;
}