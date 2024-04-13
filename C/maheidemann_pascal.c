#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>

int main(int argc, char *argv[]) {
    if(argc != 2) { //Überprüfen ob ein argument übergeben wurde
        printf("ERROR");
        return 1;
    }

    for(int i = 0; i < strlen(argv[1]); i++) { //Überprüfe ob alle Chars digits sind, wenn if eintritt wirf error.
        if(isdigit(argv[1][i]) == 0) {
            printf("Error");
            return 1;
        }
    }

    int depth = atoi(argv[1]); //Tiefe aus der Kommandozeile

    long pascal_triangle [depth] [depth]; //Größe des Dreiecks wird auf Tiefe gesetzt

    for (int i = 0; i < depth; i++) { //i Zeilen
        for (int j = 0; j <= i; j++) { // j Spalten
            if (j == i || j == 0) { //Basisfall und der Rand sind immer 1
                pascal_triangle[i][j] = 1;
            } else { //Berechnung der inneren Werte iterativ über vorherige Ergebnisse
                pascal_triangle[i][j] = pascal_triangle[i-1][j-1] + pascal_triangle[i-1][j];
            }
            printf("%ld", pascal_triangle[i][j]); //Schreibe den Wert an stelle i, j
            printf(" ");
        }
        printf("\n"); //Schreibe neue Zeile für die nächste Ebene
    }
    return 0;
}