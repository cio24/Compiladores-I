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

@aux1 DB "operaciones entre constantes enteras", 0
@aux2 DB "se esta calculando la operacion a = 30*7 - 55 + 60/5", 0
@aux3 DB "a = 167", 0
@aux4 DB "las operaciones no funcionan bien", 0
@aux5 DB "operaciones entre variables enteras", 0
@aux6 DB "b = 30, c = 7, d = 55, e = 60, f = 5 y g = 167", 0
@aux7 DB "se esta calculando la operacion a = b*c - d + e/f y se compara a con g", 0
@aux8 DB "a = g", 0
@aux9 DB "las operaciones no funcionan bien", 0
@aux10 DB "operaciones entre constantes dobles", 0
@aux11 DB "se esta calculando la operacion h = 12.5*7.3 - 55.4 + 60.0/0.5", 0
@aux2bytes22 DW ?
@aux23 DB "h = 155.85", 0
@aux24 DB "las operaciones no funcionan bien", 0
@aux25 DB "operaciones entre variables dobles", 0
@aux32 DB "i = 12.5, j = 7.3, k = 55.4, l = 60.0, m = 0.5 y n = 155.85", 0
@aux33 DB "se esta calculando la operacion h = i*j - k + l/m y se compara h con n", 0
@aux2bytes38 DW ?
@aux39 DB "h = n", 0
@aux40 DB "las operaciones no funcionan bien", 0
@aux41 DB "Resultado negativo luego de operaciÃ³n entre enteros sin signo", 0
@aux20 DQ ?
_d@main DD ?
@aux21 DQ 155.85
@aux26 DQ 12.5
@aux27 DQ 7.3
@aux28 DQ 55.4
_j@main DQ ?
@aux29 DQ 60.0
_k@main DQ ?
_e@main DD ?
@aux34 DQ ?
@aux35 DQ ?
@aux36 DQ ?
_c@main DD ?
@aux30 DQ 0.5
@aux31 DQ 155.85
_l@main DQ ?
@aux37 DQ ?
_i@main DQ ?
_f@main DD ?
_m@main DQ ?
_g@main DD ?
_b@main DD ?
_h@main DQ ?
_a@main DD ?
_n@main DQ ?
@aux12 DQ 12.5
@aux13 DQ 7.3
@aux14 DQ ?
@aux19 DQ ?
@aux15 DQ 55.4
@aux16 DQ ?
@aux17 DQ 60.0
@aux18 DQ 0.5

.CODE
; declaracion de procedimientos

NEGATIVEERRORLABEL:
invoke printf, cfm$("%s"), OFFSET @aux41
invoke ExitProcess, 0

START:
invoke printf, cfm$("%s\n"), OFFSET @aux1
invoke printf, cfm$("%s\n"), OFFSET @aux2
MOV EAX,30
MOV EBX,7
MUL EBX
MOV ECX,EAX
SUB ECX, 55
JS NEGATIVEERRORLABEL
MOV EAX,60
MOV EDX,0
MOV EBX,5
DIV EBX
MOV EBX,EAX
ADD ECX,EBX
MOV _a@main,ECX
MOV EBX,_a@main
CMP EBX,167
JNE Label12
invoke printf, cfm$("%s\n"), OFFSET @aux3
JMP Label14
Label12:
invoke printf, cfm$("%s\n"), OFFSET @aux4
Label14:
invoke printf, cfm$("%s\n"), OFFSET @aux5
MOV _b@main,30
MOV _c@main,7
MOV _d@main,55
MOV _e@main,60
MOV _f@main,5
MOV _g@main,167
invoke printf, cfm$("%s\n"), OFFSET @aux6
invoke printf, cfm$("%s\n"), OFFSET @aux7
MOV EAX,_b@main
MUL _c@main
MOV EBX,EAX
SUB EBX, _d@main
JS NEGATIVEERRORLABEL
MOV EAX,_e@main
MOV EDX,0
DIV _f@main
MOV ECX,EAX
ADD EBX,ECX
MOV _a@main,EBX
MOV EBX,_a@main
CMP EBX,_g@main
JNE Label33
invoke printf, cfm$("%s\n"), OFFSET @aux8
JMP Label35
Label33:
invoke printf, cfm$("%s\n"), OFFSET @aux9
Label35:
invoke printf, cfm$("%s\n"), OFFSET @aux10
invoke printf, cfm$("%s\n"), OFFSET @aux11
FLD @aux12
FMUL @aux13
FSTP @aux14
FLD @aux14
FSUB @aux15
FSTP @aux16
FLD @aux17
FDIV @aux18
FSTP @aux19
FLD @aux16
FADD @aux19
FSTP @aux20
FLD @aux20
FSTP _h@main
FLD _h@main
FCOMP @aux21
FSTSW @aux2bytes22
MOV AX,@aux2bytes22
SAHF
JNE Label47
invoke printf, cfm$("%s\n"), OFFSET @aux23
JMP Label49
Label47:
invoke printf, cfm$("%s\n"), OFFSET @aux24
Label49:
invoke printf, cfm$("%s\n"), OFFSET @aux25
FLD @aux26
FSTP _i@main
FLD @aux27
FSTP _j@main
FLD @aux28
FSTP _k@main
FLD @aux29
FSTP _l@main
FLD @aux30
FSTP _m@main
FLD @aux31
FSTP _n@main
invoke printf, cfm$("%s\n"), OFFSET @aux32
invoke printf, cfm$("%s\n"), OFFSET @aux33
FLD _i@main
FMUL _j@main
FSTP @aux34
FLD @aux34
FSUB _k@main
FSTP @aux35
FLD _l@main
FDIV _m@main
FSTP @aux36
FLD @aux35
FADD @aux36
FSTP @aux37
FLD @aux37
FSTP _h@main
FLD _h@main
FCOMP _n@main
FSTSW @aux2bytes38
MOV AX,@aux2bytes38
SAHF
JNE Label68
invoke printf, cfm$("%s\n"), OFFSET @aux39
JMP Label70
Label68:
invoke printf, cfm$("%s\n"), OFFSET @aux40
Label70:

jmp $ ; ignoren esta croteada, es para que no se cierre la consola xD
END START