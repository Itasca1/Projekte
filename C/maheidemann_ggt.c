#include <stdio.h>
#include <stdlib.h>

unsigned int ggt(unsigned int n, unsigned int m) { //Funtion für ggt
    if(m > n) {
        printf("ggt(%u,%u) =", m, n);
        ggt(m,n); //Gedrehter Aufruf
    } else if (m == 0) {
        printf("%u\n", n);
        return n; //Ausgabe des ggt
    } else {
        printf("ggt(%u,%u) =", m, n % m);
        ggt(m, n % m); //Nächsten Fall nach gegebener Vorschrift
    }
}

int main(int argc, char*  argv[]) {
    if(argc != 3) {
        printf("ERROR: Bitte 2 Zahlen getrennt mit Leerzeichen eingeben");
        return 1;
    }
    int n = atoi(argv[1]);

    int m = atoi(argv[2]);

    if(n <0 || m < 0) { //Check ob ein Argument negativ ist.
        printf("ERROR: Mindestens ein Argument war negativ.");
        return 1;
    }

    printf("ggt(%u,%u) =", n,m);
    ggt(n, m);

    return 0;
}