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

space:
	db 0x20

;;; start of code section
section	.text
	;; this symbol has to be defined as entry point of the program
	global _start

;;;--------------------------------------------------------------------------
;;; subroutine write_newline
;;;--------------------------------------------------------------------------
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
	
;;;--------------------------------------------------------------------------
;;; subroutine write_string
;;;--------------------------------------------------------------------------
;;; address of 0-terminated string passed in rsi
;;; operation: determines length of string and writes it in ONE write
;;; (plus a second write that appends a new-line character)

write_string:
	push rbx ;save rbx argument counter
	mov rbx, 0 ;set rbx to 0 at start
	;; save registers that are used in the code
	push	rax
	push	rdi
	push	rdx
	;; prepare arguments for write syscall
	mov	rax, SYS_WRITE	; write syscall
	mov	rdi, STDOUT	; fd = 1 (stdout)
	mov	rdx, 1		; count bytes
search_eos:
	;; here we have to specify the string size (byte) 
	cmp	[rsi], byte 0	; end of string (0) reached?
	je	return	; yes, end of loop
eos_found:
	call write_spaces
	syscall			; system call
	inc rsi 		;go to next char
	inc rbx         ;inc rbx for next char
	call write_newline ;Write new line for next char
	jmp search_eos
return:
	;; restore registers (in opposite order)
	pop	rdx
	pop	rdi
	pop	rax
	pop rbx
	ret
	


;;;;Subroutine spaces
write_spaces:
	;; save registers that are used in the code
	push	rax
	push	rdi
	push	rsi
	push	rdx
	push    rbx
	;; prepare arguments for write syscall
	mov	rax, SYS_WRITE	; write syscall
	mov	rdi, STDOUT	; fd = 1 (stdout)
	mov	rsi, space	; string
	mov	rdx, 1		; length
print_spaces:
	cmp rbx, 0  ;end of subroutine, when enough spaces are printed
	jne continue_spaces
	je end_spaces
continue_spaces:
	syscall			; system call
	dec rbx         ;dec rbx until all spaces are printed
	jmp print_spaces
end_spaces:
	;; restore registers (in opposite order)
	pop rbx
	pop	rdx
	pop	rsi
	pop	rdi
	pop	rax
	ret
	



;;;--------------------------------------------------------------------------
;;; main entry
;;;--------------------------------------------------------------------------

_start:
	pop	rbx		; argc (>= 1 guaranteed)
	pop rsi     ;pop the first argument
	dec rbx    ;decrease the argument count once
	
read_args:
	;; print command line arguments
	pop	rsi		; argv[j]
	call	write_string	; string in rsi is written to stdout
	dec	rbx		; dec arg-index
	jnz	read_args	; continue until last argument was printed

	;; exit program via syscall exit (necessary!)
	mov	rax, SYS_EXIT	; exit syscall
	mov	rdi, 0		; exit code 0 (= "ok")
	syscall 		; kernel interrupt: system call
	