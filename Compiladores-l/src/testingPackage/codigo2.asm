.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib

.DATA

; declaracion de variables
@aux1 DB "Invocacion recursiva a procedimiento invalida", 0
outTitle DB "OUT message", 0
 @PROCISACTIVEprocedure@main DD 0
 @PROCISACTIVEprocedure1@main@procedure DD 0
_@aux1 DD ?
_var@main DD ?

.CODE

RECURSIVEERRORSUBROUTINE:
invoke MessageBox, NULL, addr @aux1, addr outTitle, MB_OK
invoke ExitProcess, 0
PROCLabel1:
FLD _var@main
FST _var@main
MOV @PROCISACTIVEprocedure1@main@procedure,0
RET
PROCLabel0:
FLD _var@main
FST _var@main
CMP @PROCISACTIVEprocedure1@main@procedure,0
JNE RECURSIVEERRORSUBROUTINE
MOV @PROCISACTIVEprocedure1@main@procedure,1
CALL PROCLabel1
MOV @PROCISACTIVEprocedure@main,0
RET

START:

FLD _var@main
FST _var@main
CMP @PROCISACTIVEprocedure@main,0
JNE RECURSIVEERRORSUBROUTINE
MOV @PROCISACTIVEprocedure@main,1
CALL PROCLabel0

END START