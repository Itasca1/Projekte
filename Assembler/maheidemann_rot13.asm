;;; system calls
%define SYS_WRITE	1
%define SYS_EXIT	60
;;; file ids
%define STDOUT		1

;;; start of data section
section .data
;;; a newline character
newline:
 	db 0x0a
section .bss
   buffer     resb 1 ; 1-byte buffer

section .text
    global _start

;;; writes a newline character to stdout
	
write_newline:
	;; save registers that are used in the code
	push	rax
	push	rdi
	push	rsi
	push	rdx
	;; prepare arguments for write syscall
	mov	rax, SYS_WRITE	; write syscall
	mov	rdi, STDOUT	; fd = 1 (stdout)
	mov	rsi, newline	; string
	mov	rdx, 1		; length
	syscall			; system call
	;; restore registers (in opposite order)
	pop	rdx
	pop	rsi
	pop	rdi
	pop	rax
	ret

;;; subroutine read_char

read_char:
    push rax
    push rdi
    push rsi
    push rdx
    ;read the char
    mov rax, 0 ;sys read
    mov rdi, 0 ;
    mov rsi, buffer
    mov rdx, 1
    syscall
    cmp rax, 0 ;terminate condition
    pop rdx
    pop rsi
    pop rdi
    pop rax
    je exit
    ret

;;; subroutine write_char

write_char:
    push	rax
	push	rdi
	push    rsi
	push	rdx
    ;write char
	mov	rax, SYS_WRITE
	mov	rdi, STDOUT
	mov rsi, buffer
	mov	rdx, 1
	syscall
	pop rdx
	pop rsi
	pop rdi
	pop rax
    ret

;;; subroutine rot13
;;; bytes for comparison are hex numbers for a,z,m,13,A,Z,M

rot13:
    call read_char
check_lowercase: 
    cmp [buffer], byte 0x61;check if below a
    jb not_lower_case
    cmp [buffer], byte 0x7a ;check if above z
    ja not_lower_case
compare_to_m:
    cmp [buffer], byte 0x6d ;check if above or below/equal m
    jbe add_13
    ja  sub_13
add_13:
    add [buffer], byte 0xD ;add 13
    jmp write_char
    ret
sub_13:
    sub [buffer], byte 0xD ;sub 13
    jmp write_char
    ret
not_lower_case:
    cmp [buffer], byte 0x41 ;check if below A
    jb write_char
    cmp [buffer], byte 0x5a ;check if above Z
    ja write_char
compare_to_M:
    cmp [buffer], byte 0x4d ;check if below/equal or above M
    jbe add_13
    ja sub_13

;;;--------------------------------------------------------------------------
;;; main entry
;;;--------------------------------------------------------------------------

_start:
	pop	rbx		; argc (>= 1 guaranteed)
encrypt_char:
    call rot13
    jmp encrypt_char
exit:
    call write_newline
    mov	rax, SYS_EXIT	; exit syscall
	mov	rdi, 0		; exit code 0 (= "ok")
	syscall 		; kernel interrupt: system call
	
