#include <stdio.h>
#include <stdlib.h>


//Aus Aufgabe 21 von Möller übernommen
// pass appropriate format specifier e.g. %g to print a double
#define PRINT_ARRAY2D(ARRAY, DIM1, DIM2, FORMAT) \
  {                                              \
    int i, j;                                    \
    if (ARRAY != NULL)                           \
    {                                            \
      for (i = 0; i < DIM1; i++)                 \
      {                                          \
        for (j = 0; j < DIM2; j++)               \
        {                                        \
          printf(#FORMAT "", ARRAY[i][j]);      \
        }                                        \
        putchar('\n');                           \
      }                                          \
    }                                            \
  }

//Haben wir einen negativen Modulo, rechnen wir entweder die breite oder Höhe drauf und sind so wieder in unserem Feld
int mod(int x, int y) {
    int result = x % y;
    if(result < 0) {
        return result + y;
    } else {
        return result;
    }
}

//Count Nachbarn für ein Feld
int neighbours(char** field, int x, int y, int rows, int columns) {
    int neighbours = 0;
    int i = 0;
    for(int k = -1; k <= 1; k++) {
        for(int l = -1; l <= 1; l++) {
            if(!((k == 0) && (l == 0))) {
                i ++;
                char c = field[mod((k + x), rows)][mod((l + y), columns)];
                if(c == '*') {
                    neighbours ++;
                }
            }
        }
    }
    return neighbours;
}

//Ändert das Symbol abhägig von den Nachbarn
char new_symbol(char** field, int x, int y, int neighbours) {
    char c = field[x][y];
    if((c == ' ' && neighbours == 3)) {
        c = '*';
        return c;
    } else if((c == '*' && neighbours < 2)) {
        c = ' ';
        return c;
    } else if ((c == '*' && neighbours > 3)) {
        c = ' ';
        return c;
    } else {
        return c;
    }
}

//Kopie des Feldes mit den neuesten Änderungen erstellen
char** update_field(char** field, int rows, int columns) {
    char** copy = (char**) malloc((rows) * sizeof(char*)); 
    for (int i = 0; i < rows; i++) {
        copy[i] = (char*) malloc((columns) * sizeof(char));
        for (int j = 0; j < columns; j++) {
            int n = neighbours(field, i, j, rows, columns);
            copy[i][j] = new_symbol(field, i, j, n);
        }
    }
    return copy;
}


int main(int argc, char *argv[]) {
    int generations = atoi(argv[1]);
    int delay = atoi(argv[2]);
    int rows;
    int columns;
    int i;
    int j;
    char** field;
    FILE *input;

    //Check für richtige Anzahl Argumente
    if(argc != 4) {
        printf("Error: Falsche Parameter Anzhal");
        return 1;
    }

    //Check für positive Anzahl Generationen
    if(atoi(argv[1])<= 0) {
        printf("Generationen waren Negativ oder 0");
        return 1;
    }
    
    //Check für positve Zeit an delay
    if(atoi(argv[2]) < 0) {
        printf("Delay war negativ");
        return 1;
    }

    input = fopen(argv[3], "r");
    if(input == NULL) {
        printf("Fehler beim einlesen der Datei.");
        return 1;
    }
    

    //Einlesen von Breite und Höhe
    char buffer[256];
    fgets(buffer, sizeof(buffer), input);
    
    sscanf(buffer, "%d %d", &columns, &rows);

    //Befüllen mit Startkonfig
    field = (char**) malloc((rows) * sizeof(char*)); 
    for (i = 0; i < rows; i++) {
        field[i] = (char*) malloc((columns) * sizeof(char));
        fgets(buffer, sizeof(buffer), input);
        for (j = 0; j < columns; j++) {
            char c = buffer[j];
            //Wenn Ende der Zeile
            if (c == '\n') {
                c = buffer[j];
            }
            if (c == '*') {
                field[i][j] = '*';
            } else if (c == ' ') {
            field[i][j] = ' ';
            }
        }
    } 
    //Schliessen der Datei  
    fclose(input);
    

    int start = 1;
    printf("Generation 1: \n");
    PRINT_ARRAY2D(field, rows, columns, %c);
    printf("\n");
    start ++;
    while(start <= generations) {
        char** copy = update_field(field, rows, columns);
        //Feld auf Kopie setzen für aktuellen Stand
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                field[i][j] = copy[i][j];
            }
        }
        //Kopie löschen für nächste Iteration
        free(copy);
        printf("Generation %d:\n", start);
        PRINT_ARRAY2D(field, rows, columns, %c);
        printf("\n");
        start++;
    }
    return 0;
}