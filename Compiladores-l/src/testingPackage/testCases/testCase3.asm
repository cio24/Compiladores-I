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

	@aux4 DB "Invocacion recursiva a procedimiento invalida", 0
	_carl@main DQ ?
	@aux3 DQ ?
	@aux2 DQ 5.0
	@aux1 DQ 5.0

.CODE
	; declaracion de procedimientos

	RECURSIVEERRORSUBROUTINE:
		invoke printf, cfm$("%s"), OFFSET @aux4
		invoke ExitProcess, 0

START:
	FLD @aux1
	FMUL @aux2
	FST @aux3
	FLD @aux3
	FST _carl@main
    invoke printf, cfm$("%f"), @aux3
	jmp $	; ignoren esta croteada, es para que no se cierre la consola xD
END START