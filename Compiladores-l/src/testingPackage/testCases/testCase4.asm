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

@aux4 DB "Resultado negativo luego de operación entre enteros sin signo", 0
@aux3 DQ 4.0
@aux2 DQ 3.0
@aux1 DQ 3.6
_var@main DQ ?

.CODE
; declaracion de procedimientos

NEGATIVEERRORLABEL:
invoke printf, cfm$("%s"), OFFSET @aux4
invoke ExitProcess, 0


PROCLabel0:
FLD @aux2
FSTP _var@main
FLD _var@main
FSTP _var2@main@procedure
RET
PROCLabel1:
FLD @aux3
FSTP _var@main
FLD _var@main
FSTP _var2@main@procedure
CALL PROCLabel0
RET

START:
FLD @aux1
FSTP _var@main
FLD _var@main
FSTP _var2@main@procedure
CALL PROCLabel0

jmp $ ; ignoren esta croteada, es para que no se cierre la consola xD
END START