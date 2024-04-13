#include <stdlib.h>
#include <stdio.h>
#include "dll.h"

struct proc_info {
    struct list_head head;
    int pid;
    int counter;
    int priority;
};

void
print_proc_info(struct proc_info *pi)
{
  printf("(%d,%d,%d)\n", pi->pid, pi->counter, pi->priority);
}

int
main()
{
  //Initalisieren der Liste
  struct list_head *anker;
  list_init(anker);

  //Erstellen der 5 Elemente der Test Liste und hinzufügen.
  for(int i = 0; i < 5; i++) {
    struct proc_info *new_pi= malloc(sizeof(struct proc_info));
    new_pi -> pid = i;
    new_pi -> counter = 2 * i;
    new_pi -> priority = 3 * i;
    /*
    Benutze add_tail, damit das erste hinzugefügte Element das ist worauf anker  mit next zeigt.
    Bei list_add würde anker am ende mit next auf das letzt eingefügte zeigen.
    */
    list_add_tail(&(new_pi -> head), anker);
  }

  //Ausgeben der Liste
  struct list_head *tmp_position;
  tmp_position = anker -> next;

  //Printe solnage bis position auf den anker zeigt.
  while(tmp_position != anker) {
    //Position wird in zeiger auf proc_info umgewandelt und dann geprintet.
    //Geht da head erstes Element der proc_info ist und beide Zeiger die gleiche Speicheradresse haben.
    print_proc_info((struct proc_info *) tmp_position);
    //Zeige auf das nächste Element der Liste.
    tmp_position = tmp_position -> next;
  }

  return 0;
}

