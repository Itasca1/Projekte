#include <sys/types.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char * argv[]) {
pid_t cpid;

int numbOfChilds = atoi(argv[1]);

printf("Before the fork\n");

for(int i = 0; i < numbOfChilds; i++ ) {
    cpid = fork();
    if(cpid  < 0) {
        printf("Fehler beim erzeugen des Kindes, negativer Fork-Wert.");+
        fflush(stdout);
        return 1;
    }
    if (cpid == 0) {
        printf("I’m the child. This is my pid: %d, this is my parents pid: %d\n", getpid(), getppid());
        fflush(stdout);
        exit(0);
    }
}


printf("I’m the parent (I’ve got a id %d)\n", getpid());


exit(0);
}
/*
Analysieren Sie die Ausgaben: In welcher Reihenfolge werden Eltern- und Kind-Prozesse
bearbeitet? Sind alle Prozesse Kinder des Elternprozesses, wenn nicht, warum nicht?
A1: Die Prozesse werden nicht immer in einer festen Reihenfolge bearbeitet. Bei 5 Child Prozessen kam es dazu, dass der Parent vor dem 5. Child beendet wurde.

A2: Ja, wie man an der Ausgabe sehen kann sind alle Prozesse Kinder des Elternprozesses.
*/