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

pali:
    db " ist ein Palindrom"

no_pali:
    db " ist kein Palindrom"

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
	
;;; subroutine write pali

write_pali:
	;; save registers that are used in the code
	push	rax
	push	rdi
	push	rsi
	push	rdx
	;; prepare arguments for write syscall
	mov	rax, SYS_WRITE	; write syscall
	mov	rdi, STDOUT	; fd = 1 (stdout)
	mov	rsi, pali	; string
	mov	rdx, 18		; length
	syscall			; system call
	;; restore registers (in opposite order)
	pop	rdx
	pop	rsi
	pop	rdi
	pop	rax
	ret

;;; subroutine write no_pali

write_no_pali:
	;; save registers that are used in the code
	push	rax
	push	rdi
	push	rsi
	push	rdx
	;; prepare arguments for write syscall
	mov	rax, SYS_WRITE	; write syscall
	mov	rdi, STDOUT	; fd = 1 (stdout)
	mov	rsi, no_pali	; string
	mov	rdx, 19		; length
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
	;; save registers that are used in the code
	push	rax
	push	rdi
	push	rdx
	;; prepare arguments for write syscall
	mov	rax, SYS_WRITE	; write syscall
	mov	rdi, STDOUT	; fd = 1 (stdout)
	mov	rdx, 0		; count bytes
	push	rsi		; keep starting address of string
search_eos:
	;; here we have to specify the string size (byte) 
	cmp	[rsi], byte 0	; end of string (0) reached?
	je	eos_found	; yes, end of loop
	inc	rdx		; count
	inc	rsi		; next position in string
	jmp	search_eos	; loop
eos_found:
	pop	rsi		; restore starting address of string
	;; here rdx contains the string length
	syscall			; system call
	;; restore registers (in opposite order)
	;;  6. Oct 14 (rm): corrected bug: was pop rsi:
	pop	rdx
	pop	rdi
	pop	rax
	ret
	

;;;subroutine check pali

check_pali:
	push rsi
	push rdi
	push rdx
	push rbx
	mov rdi, rsi ;load adress into rdi
go_to_eos:
	cmp [rsi], byte  0 ; check if char is null
	je set_start
	inc rsi ;go to next char
	jmp go_to_eos
set_start:
	dec rsi ;decrease by 1 for null
compare:
	cmp rdi, rsi ;check if we reached the middle
	jae done
	mov ah, [rsi]
	mov bh, [rdi]
	cmp ah, bh ;compare chars
	jne failure
	inc rdi ;go to next char
	dec rsi ;go to prev char
	jmp compare
failure:
	pop rbx
	pop rdx
	pop rdi
	pop rsi
	call write_no_pali
	ret
done:
	pop rbx
	pop rdx
	pop rdi
	pop rsi
	call write_pali
	ret

;;;--------------------------------------------------------------------------
;;; main entry
;;;--------------------------------------------------------------------------

_start:
	pop	rbx		; argc (>= 1 guaranteed)
	pop rsi
	dec rbx
read_args:
	;; print command line arguments
	pop	rsi		; argv[j]
	call	write_string	; string in rsi is written to stdout
	call 	check_pali      ;check for plaindrome
	call	write_newline	; a newline character is written to stdout
	dec	rbx		; dec arg-index
	jnz	read_args	; continue until last argument was printed

	;; exit program via syscall exit (necessary!)
	mov	rax, SYS_EXIT	; exit syscall
	mov	rdi, 0		; exit code 0 (= "ok")
	syscall 		; kernel interrupt: system call
