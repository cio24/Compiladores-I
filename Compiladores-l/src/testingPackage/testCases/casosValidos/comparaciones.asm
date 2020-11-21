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

@aux1 DB "comparacion entre constantes enteras", 0
@aux2 DB "0 = 0", 0
@aux3 DB "la comparacion no funciona bien", 0
@aux4 DB "0 <= 1", 0
@aux5 DB "la comparacion no funciona bien", 0
@aux6 DB "0 < 1", 0
@aux7 DB "la comparacion no funciona bien", 0
@aux8 DB "1 > 0", 0
@aux9 DB "la comparacion no funciona bien", 0
@aux10 DB "1 >= 0", 0
@aux11 DB "la comparacion no funciona bien", 0
@aux12 DB "comparacion entre constantes dobles", 0
@aux2bytes15 DW ?
@aux16 DB "0.0 = 0.0", 0
@aux17 DB "la comparacion no funciona bien", 0
@aux2bytes20 DW ?
@aux21 DB "0.0 <= 1.0", 0
@aux22 DB "la comparacion no funciona bien", 0
@aux2bytes25 DW ?
@aux26 DB "0.0 < 1.0", 0
@aux27 DB "la comparacion no funciona bien", 0
@aux2bytes30 DW ?
@aux31 DB "1.0 > 0.0", 0
@aux32 DB "la comparacion no funciona bien", 0
@aux2bytes35 DW ?
@aux36 DB "1.0 >= 0.0", 0
@aux37 DB "la comparacion no funciona bien", 0
@aux38 DB "comparacion entre variables enteras", 0
@aux39 DB "a = 0 y b = 1", 0
@aux40 DB "a = a", 0
@aux41 DB "la comparacion no funciona bien", 0
@aux42 DB "a <= b", 0
@aux43 DB "la comparacion no funciona bien", 0
@aux44 DB "a < b", 0
@aux45 DB "la comparacion no funciona bien", 0
@aux46 DB "b > a", 0
@aux47 DB "la comparacion no funciona bien", 0
@aux48 DB "b >= a", 0
@aux49 DB "la comparacion no funciona bien", 0
@aux50 DB "comparacion entre variables dobles", 0
@aux51 DB "c = 0.0 y d = 1.0", 0
@aux2bytes54 DW ?
@aux55 DB "c = c", 0
@aux56 DB "la comparacion no funciona bien", 0
@aux2bytes57 DW ?
@aux58 DB "c <= d", 0
@aux59 DB "la comparacion no funciona bien", 0
@aux2bytes60 DW ?
@aux61 DB "c < d", 0
@aux62 DB "la comparacion no funciona bien", 0
@aux2bytes63 DW ?
@aux64 DB "d > c", 0
@aux65 DB "la comparacion d > c no funciona bien", 0
@aux2bytes66 DW ?
@aux67 DB "d >= c", 0
@aux68 DB "la comparacion d >= c no funciona bien", 0
@aux69 DB "Resultado negativo luego de operaciÃ³n entre enteros sin signo", 0
@aux23 DQ 0.0
@aux24 DQ 1.0
_d@main DQ ?
@aux28 DQ 1.0
@aux29 DQ 0.0
@aux33 DQ 1.0
@aux34 DQ 0.0
_c@main DQ ?
_b@main DD ?
_a@main DD ?
@aux13 DQ 0.0
@aux14 DQ 0.0
@aux52 DQ 0.0
@aux53 DQ 1.0
@aux19 DQ 1.0
@aux18 DQ 0.0

.CODE
; declaracion de procedimientos

NEGATIVEERRORLABEL:
invoke printf, cfm$("%s"), OFFSET @aux69
invoke ExitProcess, 0

START:
invoke printf, cfm$("%s\n"), OFFSET @aux1
MOV EBX,0
CMP EBX, 0
JNE Label6
invoke printf, cfm$("%s\n"), OFFSET @aux2
JMP Label8
Label6:
invoke printf, cfm$("%s\n"), OFFSET @aux3
Label8:
MOV EBX,0
CMP EBX, 1
JNBE Label13
invoke printf, cfm$("%s\n"), OFFSET @aux4
JMP Label15
Label13:
invoke printf, cfm$("%s\n"), OFFSET @aux5
Label15:
MOV EBX,0
CMP EBX, 1
JNB Label20
invoke printf, cfm$("%s\n"), OFFSET @aux6
JMP Label22
Label20:
invoke printf, cfm$("%s\n"), OFFSET @aux7
Label22:
MOV EBX,1
CMP EBX, 0
JNA Label27
invoke printf, cfm$("%s\n"), OFFSET @aux8
JMP Label29
Label27:
invoke printf, cfm$("%s\n"), OFFSET @aux9
Label29:
MOV EBX,1
CMP EBX, 0
JNAE Label34
invoke printf, cfm$("%s\n"), OFFSET @aux10
JMP Label36
Label34:
invoke printf, cfm$("%s\n"), OFFSET @aux11
Label36:
invoke printf, cfm$("%s\n"), OFFSET @aux12
FLD @aux13
FCOMP @aux14
FSTSW @aux2bytes15
MOV AX,@aux2bytes15
SAHF
JNE Label42
invoke printf, cfm$("%s\n"), OFFSET @aux16
JMP Label44
Label42:
invoke printf, cfm$("%s\n"), OFFSET @aux17
Label44:
FLD @aux18
FCOMP @aux19
FSTSW @aux2bytes20
MOV AX,@aux2bytes20
SAHF
JNBE Label49
invoke printf, cfm$("%s\n"), OFFSET @aux21
JMP Label51
Label49:
invoke printf, cfm$("%s\n"), OFFSET @aux22
Label51:
FLD @aux23
FCOMP @aux24
FSTSW @aux2bytes25
MOV AX,@aux2bytes25
SAHF
JNB Label56
invoke printf, cfm$("%s\n"), OFFSET @aux26
JMP Label58
Label56:
invoke printf, cfm$("%s\n"), OFFSET @aux27
Label58:
FLD @aux28
FCOMP @aux29
FSTSW @aux2bytes30
MOV AX,@aux2bytes30
SAHF
JNA Label63
invoke printf, cfm$("%s\n"), OFFSET @aux31
JMP Label65
Label63:
invoke printf, cfm$("%s\n"), OFFSET @aux32
Label65:
FLD @aux33
FCOMP @aux34
FSTSW @aux2bytes35
MOV AX,@aux2bytes35
SAHF
JNAE Label70
invoke printf, cfm$("%s\n"), OFFSET @aux36
JMP Label72
Label70:
invoke printf, cfm$("%s\n"), OFFSET @aux37
Label72:
invoke printf, cfm$("%s\n"), OFFSET @aux38
invoke printf, cfm$("%s\n"), OFFSET @aux39
MOV _a@main,0
MOV _b@main,1
MOV EBX,_a@main
CMP EBX, _a@main
JNE Label81
invoke printf, cfm$("%s\n"), OFFSET @aux40
JMP Label83
Label81:
invoke printf, cfm$("%s\n"), OFFSET @aux41
Label83:
MOV EBX,_a@main
CMP EBX, _b@main
JNBE Label88
invoke printf, cfm$("%s\n"), OFFSET @aux42
JMP Label90
Label88:
invoke printf, cfm$("%s\n"), OFFSET @aux43
Label90:
MOV EBX,_a@main
CMP EBX, _b@main
JNB Label95
invoke printf, cfm$("%s\n"), OFFSET @aux44
JMP Label97
Label95:
invoke printf, cfm$("%s\n"), OFFSET @aux45
Label97:
MOV EBX,_b@main
CMP EBX, _a@main
JNA Label102
invoke printf, cfm$("%s\n"), OFFSET @aux46
JMP Label104
Label102:
invoke printf, cfm$("%s\n"), OFFSET @aux47
Label104:
MOV EBX,_b@main
CMP EBX, _a@main
JNAE Label109
invoke printf, cfm$("%s\n"), OFFSET @aux48
JMP Label111
Label109:
invoke printf, cfm$("%s\n"), OFFSET @aux49
Label111:
invoke printf, cfm$("%s\n"), OFFSET @aux50
invoke printf, cfm$("%s\n"), OFFSET @aux51
FLD @aux52
FSTP _c@main
FLD @aux53
FSTP _d@main
FLD _c@main
FCOMP _c@main
FSTSW @aux2bytes54
MOV AX,@aux2bytes54
SAHF
JNE Label120
invoke printf, cfm$("%s\n"), OFFSET @aux55
JMP Label122
Label120:
invoke printf, cfm$("%s\n"), OFFSET @aux56
Label122:
FLD _c@main
FCOMP _d@main
FSTSW @aux2bytes57
MOV AX,@aux2bytes57
SAHF
JNBE Label127
invoke printf, cfm$("%s\n"), OFFSET @aux58
JMP Label129
Label127:
invoke printf, cfm$("%s\n"), OFFSET @aux59
Label129:
FLD _c@main
FCOMP _d@main
FSTSW @aux2bytes60
MOV AX,@aux2bytes60
SAHF
JNB Label134
invoke printf, cfm$("%s\n"), OFFSET @aux61
JMP Label136
Label134:
invoke printf, cfm$("%s\n"), OFFSET @aux62
Label136:
FLD _d@main
FCOMP _c@main
FSTSW @aux2bytes63
MOV AX,@aux2bytes63
SAHF
JNA Label141
invoke printf, cfm$("%s\n"), OFFSET @aux64
JMP Label143
Label141:
invoke printf, cfm$("%s\n"), OFFSET @aux65
Label143:
FLD _d@main
FCOMP _c@main
FSTSW @aux2bytes66
MOV AX,@aux2bytes66
SAHF
JNAE Label148
invoke printf, cfm$("%s\n"), OFFSET @aux67
JMP Label150
Label148:
invoke printf, cfm$("%s\n"), OFFSET @aux68
Label150:

jmp $ ; ignoren esta croteada, es para que no se cierre la consola xD
END START