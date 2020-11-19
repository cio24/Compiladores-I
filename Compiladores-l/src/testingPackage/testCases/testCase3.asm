	.386
	.model flat, stdcall
	option casemap :none
	include \masm32\include\windows.inc
	include \masm32\include\kernel32.inc
	include \masm32\include\user32.inc
	includelib \masm32\lib\kernel32.lib
	includelib \masm32\lib\user32.lib
	include \masm32\include\masm32rt.inc
	dll_dllcrt0 PROTO C
	printf PROTO C :VARARG

.DATA
	; declaracion de variables

	@aux1 DB "Hello World!", 0
	@aux2 DB "Invocacion recursiva a procedimiento invalida", 0

.CODE
	; declaracion de procedimientos

	RECURSIVEERRORSUBROUTINE:
		invoke printf, cfm$("%s"), OFFSET @aux2
		invoke ExitProcess, 0

START:
	invoke printf, cfm$("%s"), OFFSET @aux1

	jmp $	; ignoren esta croteada, es para que no se cierre la consola xD
END START