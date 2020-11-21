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

@aux1 DB "loop externo", 0
@aux2 DB "    loop  interno", 0
@aux3 DB "Resultado negativo luego de operaciÃ³n entre enteros sin signo", 0
_counter2@main DD ?
_counter1@main DD ?

.CODE
; declaracion de procedimientos

NEGATIVEERRORLABEL:
invoke printf, cfm$("%s"), OFFSET @aux3
invoke ExitProcess, 0

START:
MOV _counter1@main,0
MOV _counter2@main,0
Label3:
invoke printf, cfm$("%s\n"), OFFSET @aux1
MOV EBX, _counter1@main
ADD EBX, 1
MOV _counter1@main,EBX
Label7:
invoke printf, cfm$("%s\n"), OFFSET @aux2
MOV EBX, _counter2@main
ADD EBX, 1
MOV _counter2@main,EBX
MOV EBX,_counter2@main
CMP EBX, 3
JNE Label7
MOV _counter2@main,0
MOV ECX,_counter1@main
CMP ECX, 4
JNE Label3

jmp $ ; ignoren esta croteada, es para que no se cierre la consola xD
END START