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

@aux1 DB "Invocacion recursiva a procedimiento invalida", 0
@aux2 DB "Resultado negativo luego de operación entre enteros sin signo", 0
_var2@main DD ?
_var3@main DD ?
_var1@main DD ?

.CODE
; declaracion de procedimientos

RECURSIVEERRORSUBROUTINE:
invoke printf, cfm$("%s"), OFFSET @aux1
invoke ExitProcess, 0
NEGATIVEERRORLABEL:
invoke printf, cfm$("%s"), OFFSET @aux2
invoke ExitProcess, 0

START:
MOV _var1@main,0
MOV _var2@main,2
MOV EBX,_var1@main
SUB EBX, _var2@main
JS NEGATIVEERRORLABEL
MOV _var3@main,EBX

jmp $ ; ignoren esta croteada, es para que no se cierre la consola xD
END START