#include <sys/types.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>

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
for(int i = 0; i < numbOfChilds; i++) {
    wait(NULL);
}

printf("I’m the parent (I’ve got a id %d)\n", getpid());


exit(0);
}