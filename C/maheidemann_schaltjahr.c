#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>

int main(int argc, char *argv[]) {
    if(argc != 2) {   //überprüfe ob ein Argument übergeben wurde
        printf("ERROR");
        return 1;
    }
    
    for(int i = 0; i < strlen(argv[1]); i++) { //Überprüfe ob alle Chars digits sind, wenn if eintritt wirf error.
        if(isdigit(argv[1][i]) == 0) {
            printf("Error");
            return 1;
        }
    }

    int year = atoi(argv[1]); //Wandel string zu int um

    if(year % 100 == 0 && year % 400 == 0) { //Überprüfe ob es ein Schaltjahr ist
        printf("JA");
        printf("\n");
    } else if (year % 4 == 0 && year % 100 != 0) {
        printf("JA");
        printf("\n");
    } else {
        printf("NEIN");
        printf("\n");
    }
    
    return 0;
}