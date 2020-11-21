.386
.model flat, stdcall
.stack 200h
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

@aux1 DB "if anidados sin else", 0
@aux2bytes4 DW ?
@aux2bytes7 DW ?
@aux8 DB "funciona bien", 0
@aux9 DB "TIENE QUE HABER UN FUNCIONA BIEN ANTES DE ESTE OUT", 0
@aux10 DB "if anidados con else", 0
@aux2bytes13 DW ?
@aux2bytes16 DW ?
@aux17 DB "funciona mal", 0
@aux18 DB "funciona bien", 0
@aux19 DB "funciona mal", 0
@aux20 DB "if anidados sin else con bloques", 0
@aux2bytes23 DW ?
@aux24 DB "funciona bien", 0
@aux2bytes27 DW ?
@aux28 DB "funciona mal", 0
@aux29 DB "funciona muy mal", 0
@aux30 DB "if anidados con else y con bloques", 0
@aux2bytes33 DW ?
@aux34 DB "funciona mal", 0
@aux2bytes37 DW ?
@aux38 DB "funciona mal", 0
@aux39 DB "funciona muy mal", 0
@aux40 DB "funciona bien pero el if externo funciona mal", 0
@aux41 DB "funciona bien pero el if externo funciona mal", 0
@aux42 DB "funciona bien", 0
@aux43 DB "funciona MUY bien", 0
@aux44 DB "Resultado negativo luego de operaciÃ³n entre enteros sin signo", 0
@aux22 DQ 0.0
@aux6 DQ 0.0
@aux5 DQ 0.0
@aux25 DQ 0.0
@aux21 DQ 0.0
@aux26 DQ 0.0
@aux3 DQ 0.0
@aux2 DQ 0.0
@aux11 DQ 0.0
@aux12 DQ 0.0
@aux35 DQ 0.0
@aux14 DQ 0.0
@aux36 DQ 0.0
@aux31 DQ 0.0
@aux32 DQ 0.0
@aux15 DQ 0.0

.CODE
; declaracion de procedimientos

NEGATIVEERRORLABEL:
invoke printf, cfm$("%s"), OFFSET @aux44
invoke ExitProcess, 0

START:
invoke printf, cfm$("%s\n"), OFFSET @aux1
FLD @aux2
FCOMP @aux3
FSTSW @aux2bytes4
MOV AX,@aux2bytes4
SAHF
JNE Label8
FLD @aux5
FCOMP @aux6
FSTSW @aux2bytes7
MOV AX,@aux2bytes7
SAHF
JNE Label7
invoke printf, cfm$("%s\n"), OFFSET @aux8
Label7:
Label8:
invoke printf, cfm$("%s\n"), OFFSET @aux9
invoke printf, cfm$("%s\n"), OFFSET @aux10
FLD @aux11
FCOMP @aux12
FSTSW @aux2bytes13
MOV AX,@aux2bytes13
SAHF
JNE Label21
FLD @aux14
FCOMP @aux15
FSTSW @aux2bytes16
MOV AX,@aux2bytes16
SAHF
JNB Label17
invoke printf, cfm$("%s\n"), OFFSET @aux17
JMP Label19
Label17:
invoke printf, cfm$("%s\n"), OFFSET @aux18
Label19:
JMP Label23
Label21:
invoke printf, cfm$("%s\n"), OFFSET @aux19
Label23:
invoke printf, cfm$("%s\n"), OFFSET @aux20
FLD @aux21
FCOMP @aux22
FSTSW @aux2bytes23
MOV AX,@aux2bytes23
SAHF
JNE Label33
invoke printf, cfm$("%s\n"), OFFSET @aux24
FLD @aux25
FCOMP @aux26
FSTSW @aux2bytes27
MOV AX,@aux2bytes27
SAHF
JNB Label32
invoke printf, cfm$("%s\n"), OFFSET @aux28
invoke printf, cfm$("%s\n"), OFFSET @aux29
Label32:
Label33:
invoke printf, cfm$("%s\n"), OFFSET @aux30
FLD @aux31
FCOMP @aux32
FSTSW @aux2bytes33
MOV AX,@aux2bytes33
SAHF
JNB Label48
invoke printf, cfm$("%s\n"), OFFSET @aux34
FLD @aux35
FCOMP @aux36
FSTSW @aux2bytes37
MOV AX,@aux2bytes37
SAHF
JNB Label43
invoke printf, cfm$("%s\n"), OFFSET @aux38
invoke printf, cfm$("%s\n"), OFFSET @aux39
JMP Label46
Label43:
invoke printf, cfm$("%s\n"), OFFSET @aux40
invoke printf, cfm$("%s\n"), OFFSET @aux41
Label46:
JMP Label51
Label48:
invoke printf, cfm$("%s\n"), OFFSET @aux42
invoke printf, cfm$("%s\n"), OFFSET @aux43
Label51:

jmp $ ; ignoren esta croteada, es para que no se cierre la consola xD
END START