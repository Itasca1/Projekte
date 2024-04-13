#include <stdlib.h>
#include <stdio.h>
#include "dll.h"

/* initialize "shortcut links" for empty list */
//Leere Liste war nach Aufgabe wenn das Anker Element nur auf sich selbst zeigt.
void
list_init(struct list_head *head)
{
    head -> next = head;
    head -> prev = head;
}

/* insert new entry after the specified head */
/*next soll nach head eingefügt werden, zeiht mit prev also auf head und mit next auf den next von head.
head zeigt mit next jetzt auf new und der vorherige next von head zeigt mit prev auf new.
new liegt somit zwischen head und dem vorherigen next von head.*/ 
void 
list_add(struct list_head *new, struct list_head *head)
{   
    new -> prev = head;
    new -> next = head -> next;
    head -> next -> prev = new;
    head -> next = new;
}

/* insert new entry before the specified head */
/*
Setze next von new auf head und den prev von new auf den prev von head. Dannach setze den next von dem prev von head auf new und den prev von head auf new.
*/
void 
list_add_tail(struct list_head *new, struct list_head *head)
{
    new -> next = head;
    new -> prev = head -> prev;
    head -> prev -> next = new;
    head -> prev = new;  
}

/* deletes entry from list and reinitialize it (next = prev = 0), 
   and returns pointer to entry */
/*Setze prev von next von entry auf den prev von entry und setze next von prev von entry auf den next von netry, somit zeigt kein Element mehr auf entry.
Setze next und prev von entry auf NULL und gebe es zurück. Somit ist es aus der Liste entfernt.*/
struct list_head* 
list_del(struct list_head *entry)
{
    entry -> next -> prev = entry -> prev;
    entry -> prev -> next = entry -> next;
    entry -> next = NULL;
    entry -> prev = NULL;
    return entry;
}

/* delete entry from one list and insert after the specified head */
//Rufe erst list_del mit entry auf um es in der einen Liste zu löschen und dannach list_add um es hinter dem head der anderen einzufügen.
void 
list_move(struct list_head *entry, struct list_head *head)
{
    list_del(entry);
    list_add(entry, head);
}

/* delete entry from one list and insert before the specified head */
//Lösche zuerst entry aus der einen Liste mit list_del(entry) und füge es mit add tail vor head der anderen hinzu
void 
list_move_tail(struct list_head *entry, struct list_head *head)
{  
    list_del(entry);
    list_add_tail(entry, head);

}

/* tests whether a list is empty */
//Wenn head mit next auf sich selbst zeigt ist die Liste nach definition leer, gebe dann 1 zurück. Ansonsten gib 0 zurück.
int 
list_empty(struct list_head *head)
{
    if(head -> next == head) {
        return 1;
    } else {
        return 0;
    }
 }