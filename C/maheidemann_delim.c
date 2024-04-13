#include <stdio.h>

int main() {
    char buffer[100];  //Buffer für die Eingabe

    int num_of_chars = fread(buffer, sizeof(char), 101, stdin); //Einlesen der Zeichen und merken der Anzahl von chars
    
    if(num_of_chars > 100) { //Bei mehr als 100 Zeichen wirf Error
        printf("ERROR");
        return 1;
    }
    
    
    int i = 0;
    int j = 0;

    while(buffer[i] != '\0') { //solange chars überprüfen bis char '\0' erreicht wird
        switch (buffer[i]) {
            case '\t':
            case '\n':
            case ' ':
                for (j = i; j < num_of_chars; j++) { //Shifte alle Zeichen nach dem Trennzeichen um eine Position nach links, um Zeichen zu löschen
                    buffer[j] = buffer[j+1];
                }
                break;
            default: //Gehe zum nächsten char
                i++;
                break;
        }
    }
    
    printf("%s", buffer); //Gib veränderten String aus
    return 0;
}