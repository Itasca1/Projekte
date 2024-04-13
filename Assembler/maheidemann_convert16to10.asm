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
hex_number:
	db "hexadecimal number = 0x"
decimal_number:
	db "decimal number = "
error_msg:
	db "ERROR: invalid hexadecimal number"

section .bss
buffer:
	resb 16  ;0xffffffffff


;;; start of code section
section	.text
	;; this symbol has to be defined as entry point of the program
	global _start
;;;subroutine write_hex_num

write_hex_num:
	;; save registers that are used in the code
	push	rax
	push	rdi
	push	rsi
	push	rdx
	;; prepare arguments for write syscall
	mov	rax, SYS_WRITE	; write syscall
	mov	rdi, STDOUT	; fd = 1 (stdout)
	mov	rsi, hex_number	; string
	mov	rdx, 23	; length
	syscall			; system call
	;; restore registers (in opposite order)
	pop	rdx
	pop	rsi
	pop	rdi
	pop	rax
	ret

;;;  subroutine write_dec_num
write_dec_num:
	;; save registers that are used in the code
	push	rax
	push	rdi
	push	rsi
	push	rdx
	;; prepare arguments for write syscall
	mov	rax, SYS_WRITE	; write syscall
	mov	rdi, STDOUT	; fd = 1 (stdout)
	mov	rsi, decimal_number	; string
	mov	rdx, 17	; length
	syscall			; system call
	;; restore registers (in opposite order)
	pop	rdx
	pop	rsi
	pop	rdi
	pop	rax
	ret

;;; subroutine write_error
write_error:
	;; save registers that are used in the code
	push	rax
	push	rdi
	push	rsi
	push	rdx
	;; prepare arguments for write syscall
	mov	rax, SYS_WRITE	; write syscall
	mov	rdi, STDOUT	; fd = 1 (stdout)
	mov	rsi, error_msg; string
	mov	rdx, 33	; length
	syscall			; system call
	;; restore registers (in opposite order)
	pop	rdx
	pop	rsi
	pop	rdi
	pop	rax
	ret



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

;;; subroutine change_to_dec:

change_to_dec:
	mov r10, 0 ;set r10 to 0 and use as counter for string length
	mov r9, 0; use r9 to count the sum
check_for_valid_input:
	cmp [rsi], byte 0 ;
	je hex_read_done
	cmp[rsi], byte 0x30 ; '0', end if lower then '0'
	jb end_error   ;if below throw error
	cmp [rsi], byte 0x39 ;'9', shift if between '0' and '9'
	jbe shift_digit
	cmp [rsi], byte 0x61; 'a', end if lower then 'a'
	jb end_error ;if below throw error 
	cmp [rsi], byte 0x66; 'f', shift if between 'a' and 'f'
	jbe shift_char
	jmp end_error ;if above throw error 
shift_digit:
	mov r8b, [rsi]
	sub r8b, byte 0x30 ;shift by 30 to make ascii digit a numeric digit between 0-9
	;Horner schema add number to sum and mult sum with 16
	add r9, r8 ;add value to the sum
	inc rsi ;go to next char
	inc r10 ;inc counter 
	cmp [rsi], byte 0 ;check if this was the last digit of the hex number
	je hex_read_done
	shl r9, 4 ;mul with 16
	jmp check_for_valid_input
shift_char:
	mov r8b, [rsi]
	sub r8b, byte 0x57 ;shift by 57 to make ascii digit a numeric value btween 10-15
	;Horner schema add number to sum and mult sum with 16
	add r9, r8 ;add value to the sum
	inc rsi ;go to next char
	inc r10 ;inc counter
	cmp [rsi], byte 0 ;check if this was the last digit of the hex number
	je hex_read_done
	shl r9, 4 ;mul with 16
	jmp check_for_valid_input
hex_read_done:
	cmp r10, byte 0x8 ;max input of 8 bytes
	ja end_error ;if over 8 bytes throw error 
	;save registers used for division
	push rax
	push rcx
	push rdx 
	mov r12, 1;use r12 as counter for position
hex_to_dec:
	mov rdx, 0 ;rdx saves the remainder, store 0 to start 
	mov rax, r9 ;move the sum in rax
	mov rcx, 10 ;move 10 in rbx for divison
	div rcx ;divide sum by 10 
	mov r9, rax ;move the divided sum in r9 
	add rdx, byte 0x30 ;add 30 to match ascii signs from 0-9
	mov r13, 16 ;16 bytes as in the buffer 
	sub r13, r12 ;sub counter from r13, to put in numbers from back to front
	mov [buffer + r13], dl ;use dl to store 1 byte from rdx
	inc r12 ;increase position counter
	cmp rax, byte 0 ;check if divident operant is 0 
	jnz hex_to_dec
print_num:
	;write the buffer
	mov rsi, buffer
	mov rdx, 16
	mov rax, SYS_WRITE
	mov rdi, STDOUT
	syscall
done:
	pop rdx
	pop rcx
	pop rax 
	ret


;;;--------------------------------------------------------------------------
;;; main entry
;;;--------------------------------------------------------------------------

_start:
	pop	rbx		; argc (>= 1 guaranteed)
    pop rsi
    dec rbx
	jz end
read_args:
	;; print command line arguments
	pop	rsi		; argv[j]
	call write_hex_num
	call write_string	; string in rsi is written to stdout
	call write_newline
	call write_dec_num
	call change_to_dec
	call write_newline
	dec	rbx		; dec arg-index
	jnz	read_args	; continue until last argument was printed
end:
	;; exit program via syscall exit (necessary!)
	mov	rax, SYS_EXIT	; exit syscall
	mov	rdi, 0		; exit code 0 (= "ok")
	syscall 		; kernel interrupt: system call
end_error:
	call write_error
	call write_newline
	;; exit program via syscall exit (necessary!)
	mov	rax, SYS_EXIT	; exit syscall
	mov	rdi, 0		; exit code 0 (= "ok")
	syscall 		; kernel interrupt: system call