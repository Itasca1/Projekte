#include <stdio.h>
#include <stdlib.h>
/* modifiziert 14. Jun 22 (rm) */

/* entry of elem list */
typedef struct _elem_t {
  int i; /* data */
  float f; /* data */
  struct _elem_t *next; /* pointer to next elem */
} elem_t; /* alias type name */

/* create (and allocate) elem_t, assign initial values */
elem_t*
create_elem(int i, float f)
{
  elem_t *e = malloc(sizeof(elem_t));
  e->i = i;
  e->f = f;
  e->next = NULL; /* just to be sure */
  return e;
}

/* append elem to list */
elem_t*
append_elem(elem_t *list, elem_t *e)
{
  e->next = list;
  return e;
}

/* number of list elements */
int
list_length(elem_t *list)
{
  if (!list)
    return 0;
  return 1 + list_length(list->next);
}

/* print list element */
void
print_elem(elem_t *e)
{
  if (e)
    printf("this=%p, i=%d, f=%g, next=%p\n", e, e->i, e->f,
        e->next);
  else
    printf("this=NULL\n");
}

/* print list */
void
print_list(elem_t *list)
{
  if (!list)
    return;
  print_elem(list);
  print_list(list->next);
}

/* return value indicates whether e1 and e2 are identical */
int
identical(elem_t *e1, elem_t *e2)
{
  return (e1->i == e2->i) && (e1->f == e2->f);
}

/* search for first item in list which is equal to e */
elem_t *
search_forward(elem_t *list, elem_t *e)
{
  if (!list)
    return NULL;
  if (identical(list, e))
    return list;
  return search_forward(list->next, e);
}

/*Alles obige ist Code aus der bereitgestellten Datei.
 Ab hier kommt meine Bearbeitung. */

 void delete_list(elem_t **list) { //Double pointer weil sonst nur locale copy verändert wird und nicht der originale pointer
    if(*list) { //Gucken ob es noch Elemente in der Liste gibt
        if((*list)->next) { //Gibt es von diesem Startpunkt einen Nachfolger
            delete_list(&((*list)->next)); //rekursiver aufruf
        }
        free(*list); //Löschen durch free bei malloc
        *list = NULL; //Damit pointer nicht auf etwas zeigt was dereferenziert wurde
    }
}

void print_reverse(elem_t *list) {
  if(list -> next) { //Liste mit Nachfolger
    print_reverse(list -> next);
    print_elem(list);
  } else  { //Letztes oder leere Liste
    print_elem(list);
  } 
}

elem_t * search_backwards(elem_t *list, elem_t *e) {
  elem_t *result = NULL;

  if(!list) { //Leere Liste
    return NULL;
  }

  if(list -> next) { //Liste mit Nachfolger
    result = search_backwards(list -> next, e);
    if(!result) {//Wenn es noch keinen Fund gibt guck nach
      if (identical(list, e)) {
        result = list;
        return result;
      }
    } else if(result) { //Ansonsten gib den ersten Fund an oberen rekursiv Call weiter
      return result;
    }
    
  } else if(list) { //Letztes Element oder Liste mit nur einem Element 
    if(identical(list, e)) {
      result = list;
      return result;
    }
  } else {
    return NULL;
  }
  
}

int
main()
{
  elem_t *anker = NULL;
  elem_t *to_find = create_elem(2, 2.22);

  anker = append_elem(anker, create_elem(1, 1.11));
  anker = append_elem(anker, create_elem(2, 2.22));
  anker = append_elem(anker, create_elem(2, 2.22));
  anker = append_elem(anker, create_elem(3, 3.33));
  print_list(anker);
  printf("length=%d\n", list_length(anker));
  puts("Rückwärts suchen: ");
  print_elem(search_backwards(anker, to_find));
  free(to_find);
  printf("length=%d\n", list_length(anker));
  print_list(anker);
  printf("Liste reverse geprintet: \n");
  print_reverse(anker);
  printf("\n");
  delete_list(&anker);
  printf("Länge nach Delete Call: ");
  printf("length=%d\n", list_length(anker));
  return 0;
}