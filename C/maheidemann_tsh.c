#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "makeargv.h"
#include <sys/wait.h>
#include <ctype.h>

// Code von dll.c für die Verwaltung der Prozesse
struct list_head {
    struct list_head *next, *prev;
};

void list_init(struct list_head *head) {
    head->next = head;
    head->prev = head;
}

void list_add(struct list_head *new, struct list_head *head) {
    new->prev = head;
    new->next = head->next;
    head->next->prev = new;
    head->next = new;
}

void list_add_tail(struct list_head *new, struct list_head *head) {
    new->next = head;
    new->prev = head->prev;
    head->prev->next = new;
    head->prev = new;
}

void list_del(struct list_head *entry) {
    entry->next->prev = entry->prev;
    entry->prev->next = entry->next;
    entry->next = NULL;
    entry->prev = NULL;
}

void list_move(struct list_head *entry, struct list_head *head) {
    list_del(entry);
    list_add(entry, head);
}

void list_move_tail(struct list_head *entry, struct list_head *head) {
    list_del(entry);
    list_add_tail(entry, head);
}

int list_empty(struct list_head *head) {
    return head->next == head;
}

// Struktur um Job zu speichern
struct Job{
    struct list_head head;
    int jobNumber;
    pid_t pid;
    char **args;
    int status;
    char *name;
    int running;
};

// Jobliste
struct list_head jobList;

// Methode um den Prompt zu printen
void prompt() {
    printf("tsh> ");
}

// Methode um den Funktionsaufruf festzulegen
int processInput(char* input) {
    if (strcmp(input, "job") == 0) {
        return 0;
    } else if(strcmp(input, "list") == 0) {
        return 1;
    } else if(strcmp(input, "info") == 0) {
        return 2;
    } else if(strcmp(input, "wait") == 0) {
        return 3;
    } else if(strcmp(input, "kill") == 0) {
        return 4;
    } else if(strcmp(input, "quit") == 0) {
        return 5;
    } else if (strcmp(input, "cd") == 0) {
        return 6;
    } else if (strcmp(input, "pwd") == 0) {
        return 7;
    } else {
        return -1;
    }
}

// Globaler Job Counter, startet bei 0 
int jobCounter = 0;

// Vorderground Job pid
pid_t fpid = 0;

// Methode um eingabe auf Positive Zahl zu prüfen
int isPositiveDigit(char *args) {

    //Solange *args nicht auf NULL zeigt
    while (*args) {
        char c = *args;
        switch(c) {
            // Negative Zahl
            case '-':
                return 1;
            default:
                // ASCII '0' .. '9'
                if (c < '0' || c > '9') {
                    return 1;
                }
        }
        // gehe zum nächsten char
        args++;
    }
    return 0;
}

// Methode um Prozess zu killen
void killChild(int searchJob) {

    struct Job *job;
    struct list_head *pos;

    // Int um festzuhalten ob ein passender Job gefunden wurde
    int found = 0;

    // Schleife über alle Jobs
    for (pos = jobList.next; pos != &jobList; pos = pos->next) {

        job = (struct Job *)pos;
        int status = job ->status;

        // Wenn auf diesen Job gewartet werden soll rufe kill auf, um diesen zu beenden
        if(job -> jobNumber == searchJob) {
            // Übergebe pid und SIGKILL an Funktion und beende damit den Prozess
            if(!waitpid(job ->pid, &status, WNOHANG)) {
                job -> running = 1;
                int killResult = kill(job ->pid, SIGKILL);
                if(killResult == -1) {
                    printf("Fehler beim ausführen von kill()\n");
                }
            }
            found = 1;
        }
    }
    // Wenn es diese Job-Nummer nicht gibt, gib dies aus
    if(found == 0) {
        printf("[job not found]\n");
    }
}

// Methode um die Shell zu quitten
void quitShell() {

    struct Job *job;
    struct list_head *pos;

    // Schleife über alle Jobs und beende diese
    for (pos = jobList.next; pos != &jobList; pos = pos->next) {
        job = (struct Job *)pos;
        // Beende Prozess
        killChild(job -> jobNumber);
    }
    exit(0);
}

// Methode um Job hinzuzufügen
void addJob(char **args, int argc) {
    struct Job* job = malloc(sizeof(struct Job));
    job->jobNumber = jobCounter;
    job->name = strdup(args[0]);
    job-> running = 0;
    
    // Reserviere Speicher für die Argumente
    job->args = malloc((argc + 1) * sizeof(char*));
    
    // Kopiere die Argumente
    for (int i = 0; i < argc; i++) {
        job->args[i] = strdup(args[i]);
    }
    
    // Forken und den Job im Hintergrund ausführen
    pid_t pid = fork();
    

    if (pid < 0) {
        printf("Fehler beim Forken");
        quitShell();
    } else if (pid == 0) {
        // Ausführen des Programms im Hintergrund
        int result = execvp(args[0], args);

        // Wenn execvp Fehlschlägt
        if(result == -1) {
            printf("[Programm %s ist nicht ausführbar]\n", args[0]);
        }
        exit(1);
    } else {
        // Aktualisiere den Job in der Jobliste
        job->pid = pid;
        list_add_tail(&job->head, &jobList);
        jobCounter++;
    }
}

// Sigint Handler für Strg + C
void sigint_handler(int sig) {
    if(fpid > 0) {
        // Beenden des vordergrundprozesses
        kill(fpid,SIGINT);
        fpid = 0;         
    } else {
        // Starte eine neue Schleifeniteration
        return;
    }
}

// Methode um Job zu printen
void printJob(struct Job *job) {
    int status;
    //Gucke ob Job nach aktuellem Stand noch läuft, so wird waitpid nur einmal auf beendeten prozess angewendet 
    if((job -> running)== 0) {
        int result = waitpid(job->pid, &status, WNOHANG);
   
        if (result == 0) {
            job -> status = WEXITSTATUS(status);
            // Der Job läuft noch
            printf("%d (pid %d running status= 0): ", job->jobNumber, job->pid);
            // Printe alle Argumente des Jobs
            char **args = job->args;
            while (*args != NULL) {
                printf("%s ", *args);
                args++;
            }
            printf("\n");
        } else if(WEXITSTATUS(result))  {
            job -> running = 1;
            printf("%d (pid %d finished  status= %d):", job->jobNumber, job->pid, job->status);
             // Printe alle Argumente des Jobs
            char **args = job->args;
            while (*args != NULL) {
                printf("%s ", *args);
                args++;
            }
            printf("\n");
        }
    } else if ((job -> running) == 1) {
        printf("%d (pid %d finished  status= %d):", job->jobNumber, job->pid, job->status);
        // Printe alle Argumente des Jobs
        char **args = job->args;
        while (*args != NULL) {
            printf("%s ", *args);
            args++;
        }
        printf("\n");
    }
    
}


// Methode für List
void printJobList() {

    struct Job *job;
    struct list_head *pos;

   // Schleife über alle Jobs
    for (pos = jobList.next; pos != &jobList; pos = pos->next) {
        job = (struct Job *)pos;
        printJob(job);
    }
}

// Methode für info
void printJobInfo(int searchJob) {

    struct Job *job;
    struct list_head *pos;

    // Int um festzuhalten ob ein passender Job gefunden wurde
    int found = 0;

    // Schleife über alle Jobs
    for (pos = jobList.next; pos != &jobList; pos = pos->next) {
        job = (struct Job *)pos;
        if(job -> jobNumber == searchJob) {
            printJob(job);
            found = 1;
        }
    }
    // Wenn kein Job mit dieser Nummer exisitiert printe dies
    if(found == 0) {
        printf("[job not found]\n");
    }
}

// Methode für wait
void waitChild(int searchJob) {

    struct Job *job;
    struct list_head *pos;

    // Int um festzuhalten ob ein passender Job gefunden wurde
    int found = 0;

    // Schleife über alle Jobs
    for (pos = jobList.next; pos != &jobList; pos = pos->next) {

        job = (struct Job *)pos;

        // Wenn auf diesen Job gewartet werden soll rufe waitpid auf damit Elternporzess auf dieses Ende wartet
        if(job -> jobNumber == searchJob) {
            int status;
            if((job -> running)== 0) {
                int wait = waitpid(job -> pid, &status, 0);
                if(wait == -1) {
                    printf("[Fehler beim warten auf Child\n]");
                }
                if(WEXITSTATUS(status)) {
                    job -> running = 1;
                    job -> status = WEXITSTATUS(status);
                }
            }
            found = 1;
        }
    }
    // Wenn es diese Job-Nummer nicht gibt, gib dies aus
    if(found == 0) {
        printf("[job not found]\n");
    }
}


// Methode für PWD
void pwd() {
    // Buffer für den Pfad (Max wert ist 1024 für einen Pfad)
    char cwd[1024];
    // Wenn getcwd != NULL ist gebe Pfad aus
    if (getcwd(cwd, sizeof(cwd)) != NULL) {
        printf("Aktuelles Verzeichnis: %s\n", cwd);
    } else {
        // Wenn getcwd Fehlschlägt
        printf("Fehler beim finden vom aktuellen Verzeichnis.\n");
    }
}

// Methode für CD
void cd(char * args) {
    // Bei keinemm Argument wechsel ins Home verzeichnis
    if(args == NULL) {
        //getenv("HOME") gibt die "HOME" environment variable zurück
        int result = chdir(getenv("HOME"));
        if(result == -1) {
        printf("Fehler beim wechseln des Verzeichnisses.\n");
        }
    } else {
        // Wechsel Pfad
        int result = chdir(args);
        // Wenn Wechsel nicht klappt
        if(result == -1) {
            printf("Fehler beim wechseln des Verzeichnisses.\n");
        }
    }
    
}

int main() {

    char *input = NULL;
    size_t input_size = 0;

    struct sigaction sa;

    sa.sa_handler = sigint_handler;
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = 0;

    if (sigaction(SIGINT, &sa, NULL) == -1) {
        perror("[sigaction error]");
    }
    
    
    // Initalisieren der Jobliste
    list_init(&jobList);

    // Endlosschleife damit die Shell solange läuft bis "quit" angegeben wird
    while (1) {
        // Gebe den prompt aus
        prompt();
        
        // Einlesen der Eingabe
        ssize_t read = getline(&input, &input_size, stdin);

        if (read == -1) {
            // Fehler beim Einlesen der Eingabe
            printf("Fehler beim lesen der Eingabe\n");
            quitShell();
        }

        // Tausche Zeilenumbruch gegen Null-Endung
        input[strcspn(input, "\n")] = '\0';

        // Bei leerer Eingabe gehe direkt zur nächsten Schleifen Iteration über (So wird direkt ein neuer Prompt ausgegeben)
        if (strlen(input) == 0) {
            continue;
        }

        char **args;
        int argc = makeargv(input, " ", &args);

        //Eingabe von Leerzeichen
        if(argc == 0) {
            continue;
        }

        int result = processInput(args[0]);

        // Ausführen der geforderten Funktion
        switch (result) {
            // JOB
            case 0:
                // Job erwartet mindestens 2 Argumente
                if (argc > 1) {
                    // Füge den aktuellen Befehl zur Job-Liste hinzu, mit seinen Argumenten
                    addJob(args + 1, argc - 1);
                } else {
                    printf("[Falsche Anzahl an Argumenten]\n");
                }
                break;
            // LIST
            case 1:
                // Check ob es genau ein Argument gab
                if(argc == 1) {
                    // Printe die Job-Liste
                    printJobList();
                } else {
                    printf("[Falsche Anzahl an Argumenten]\n");
                }
                break;
            // INFO
            case 2:
                // Check ob es genau 2 Argumente gab
                if(argc == 2) {
                    // Check ob das 2. Argument ein Positiver Int war
                    if((isPositiveDigit(args[1]) == 0)) {
                        int searchNumber = atoi(args[1]);
                        // Printe die Info
                        printJobInfo(searchNumber);
                    } else {
                        printf("[Argument war kein positiver Int Wert]\n");
                    }
                } else {
                    printf("[Falsche Anzahl an Argumenten]\n");
                }
                break;
            // WAIT
            case 3:
                // Check ob es genau 2 Argumente gab
                if(argc == 2) {
                    // Check ob 2. Argument positiver Int ist
                    if(isPositiveDigit(args[1]) == 0) {
                        int searchNumber = atoi(args[1]);
                        // Führe wait aus
                        waitChild(searchNumber);
                    } else {
                        printf("[Argument war kein positiver Int Wert]\n");
                    }
                } else {
                    printf("[Falsche Anzahl an Argumenten]\n");
                }
                break;
            // KILL
            case 4:
                // Check ob es genau 2 Argumenten gibt
                if(argc == 2) {
                    // Check ob es eine Zahl ist
                    if(isPositiveDigit(args[1]) == 0) {
                        int searchNumber = atoi(args[1]);
                        // Führe kill aus
                        killChild(searchNumber);
                    } else {
                        printf("[Argument war kein positiver Int Wert]\n");
                    }
                } else {
                    printf("[Falsche Anzahl an Argumenten]\n");
                }
                break;
            // QUIT
            case 5:
                if(argc == 1) {
                    quitShell();
                } else {
                    printf("[Falsche Anzahl an Argumenten]\n");
                }
                break;
            // CD
            case 6:
                if(argc >=1  && argc <= 2) {
                    cd(args[1]);
                } else {
                     printf("[Falsche Anzahl an Argumenten]\n");
                }
                break;
            // PWD
            case 7:
                if(argc == 1) {
                    pwd();
                } else {
                    printf("Falsche Anzahl an Argumenten. \n");
                }
                break;
            default:

                // Keiner der Build-in Jobs, versuche Programm im Vordergrund auszuführen.
                // Forke Child-Prozess
                // Überprüfen ob Forken fehlgeschlagen ist
                if ((fpid = fork()) < 0) {
                    printf("Fehler beim Forken.\n");
                    quitShell();
                } else if (fpid == 0) {
                    // Ausführen des Programms
                    int result = execvp(args[0], args);

                    // Wenn execvp fehlschlägt, gebe einen Fehler aus
                    if(result == -1) {
                        printf("[invalid command]\n");
                        exit(1);
                    }
                } else {
                    // Warten auf Beendigung des Kindprozesses und Ausgabe
                    int status;
                    int result = waitpid(fpid, &status, 0);
    	            if(result == -1) {
                        printf("Fehler beim warten auf Child (Vordergrund).\n");
                    }
                    if (WIFEXITED(status)) {
                        printf("[status=%d]\n", WEXITSTATUS(status));
                    }
                }
                break;
        }
        // Gib den Speicher für die Argumente frei
        freemakeargv(args);
    }

    return 0;
}

/*
Fragen:

1. Warum funktioniert z.B. der Wechsel des aktuellen Verzeichnisses mit cd nicht?

CD funktioniert nicht, da es in einem Kindprozess ausgeführt wird und das Kind die Umgebung des parent übergeben bekommt und diese nicht verändern kann.
Es würde also ein Wechsel stattfinden, von dem der parent nichts wüsste.

2. Warum können Sie den laufenden Vordergrund-Auftrag nicht mit ^C beenden, was passiert statt dessen?

Das SIGINT Signal wird an Vordergrund Prozess Gruppe gesendet und somit alle Porzesse diese Gruppe beendet. In diesem Fall ist das auch die Shell selbst.
Man muss dieses Signal also abfangen und dann genau den Prozess nennn der beendet werden soll.

3. Warum funktioniert die Ein-/Ausgabe-Umleitung mit > und < sowie die Verknüpfung von Kommandos
über “Pipes” mit | nicht?

'|' wird als Zeichen und nicht als Pipe von der Shell interpretiert. Execvp führt nur ein einzelnen Programm aus und dabei wird '|' einfach nur
als ein weiteres Argument betrachtet. Um die Ein-/Ausgabe-Umelitung zu realisieren müsste man dies als speziell in der Shell implementieren.
*/