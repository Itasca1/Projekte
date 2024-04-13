//Header für die Funktionen ALLOC_ARRAY2D und DEL_ARRAY2D

/*
Reserviere Speicherplatz für DIM1 viele Objekte vom Typ: TYPE*
1.For: Speicherplatz für DIM2 viele OBjekte vom Typ: Type pro Zeile reservieren
2. For: Initalisiere die TYPE Ojekte der row mit angegebenen Wert
*/ 

#define ALLOC_ARRAY2D(ARRAY,TYPE,DIM1,DIM2,INIT) {          \
    int row;                                                \
    int column;                                             \
                                                            \
    ARRAY = (TYPE**) malloc((DIM1) * sizeof(TYPE*));        \
    for(row = 0; row < DIM1; row++) {                       \
        ARRAY[row] = (TYPE*) malloc((DIM2) * sizeof(TYPE)); \
        for(column = 0; column < DIM2; column++) {          \
            ARRAY[row][column] = (INIT);                    \
        }                                                   \
    }                                                       \
}

/*
Verwende free um malloc initalisierung freizugeben    
Gibt jede Zeile des 2D Arrays frei, reicht da nur die Zeilen dynamisch mit malloc reserviert wurden.

Anschließend das ganze Array
 */

#define DEL_ARRAY2D(ARRAY, DIM1) {                          \
    int row;                                                \
                                                            \
    for(row = 0; row < DIM1; row++) {                       \
        free((ARRAY[row]));                                 \
    }                                                       \
    free(ARRAY);                                            \
} 
  
                                                            