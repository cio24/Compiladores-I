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

@aux1 DB " ", 0
@aux2 DB "invocacion de procedimiento con un solo parametros", 0
@aux3 DB "a = 1, entonces funciona correctamente", 0
@aux4 DB "anda mal", 0
@aux5 DB " ", 0
@aux6 DB "invocacion de procedimiento con dos parametros", 0
@aux7 DB "a = 2, entonces funciona correctamente", 0
@aux8 DB "anda mal", 0
@aux9 DB " ", 0
@aux10 DB "invocacion de procedimiento con tres parametros", 0
@aux11 DB "a = 3, entonces funciona correctamente", 0
@aux12 DB "anda mal", 0
@aux13 DB " ", 0
@aux14 DB "declaracion e invocacion de procedimientos anidados restringiendo el nivel de anidamientos y variando el shadowing para ver como responde", 0
@aux15 DB "Hello World!,funciona correctamente!", 0
@aux16 DB "Resultado negativo luego de operaciÃ³n entre enteros sin signo", 0
_a@main@print_suma_tres_valo DD ?
_b@main DD ?
_b@main@print_suma_dos_valor DD ?
_a@main DD ?
_a@main@print_valor DD ?
_c@main@print_suma_tres_valo DD ?
_c@main DD ?
_a@main@print_suma_dos_valor DD ?
_b@main@print_suma_tres_valo DD ?

.CODE
; declaracion de procedimientos

NEGATIVEERRORLABEL:
invoke printf, cfm$("%s"), OFFSET @aux16
invoke ExitProcess, 0

PROCLabel6:
invoke printf, cfm$("%s\n"), OFFSET @aux15
RET

PROCLabel5:
CALL PROCLabel6
RET

PROCLabel4:
CALL PROCLabel5
RET

PROCLabel3:
CALL PROCLabel4
RET

PROCLabel2:
MOV EBX,_a@main@print_suma_tres_valo
ADD EBX,_b@main@print_suma_tres_valo
ADD EBX,_c@main@print_suma_tres_valo
MOV _a@main@print_suma_tres_valo,EBX
MOV EBX,_a@main@print_suma_tres_valo
CMP EBX,3
JNE Label43
invoke printf, cfm$("%s\n"), OFFSET @aux11
JMP Label45
Label43:
invoke printf, cfm$("%s\n"), OFFSET @aux12
Label45:
RET

PROCLabel1:
MOV EBX,_a@main@print_suma_dos_valor
ADD EBX,_b@main@print_suma_dos_valor
MOV _a@main@print_suma_dos_valor,EBX
MOV EBX,_a@main@print_suma_dos_valor
CMP EBX,2
JNE Label26
invoke printf, cfm$("%s\n"), OFFSET @aux7
JMP Label28
Label26:
invoke printf, cfm$("%s\n"), OFFSET @aux8
Label28:
RET

PROCLabel0:
MOV EBX,_a@main@print_valor
CMP EBX,1
JNE Label11
invoke printf, cfm$("%s\n"), OFFSET @aux3
JMP Label13
Label11:
invoke printf, cfm$("%s\n"), OFFSET @aux4
Label13:
RET

START:
MOV _a@main,1
MOV _b@main,1
MOV _c@main,1
invoke printf, cfm$("%s\n"), OFFSET @aux1
invoke printf, cfm$("%s\n"), OFFSET @aux2
MOV EBX, _a@main
MOV _a@main@print_valor,EBX
CALL PROCLabel0
invoke printf, cfm$("%s\n"), OFFSET @aux5
invoke printf, cfm$("%s\n"), OFFSET @aux6
MOV EBX, _a@main
MOV _a@main@print_suma_dos_valor,EBX
MOV EBX, _b@main
MOV _b@main@print_suma_dos_valor,EBX
CALL PROCLabel1
invoke printf, cfm$("%s\n"), OFFSET @aux9
invoke printf, cfm$("%s\n"), OFFSET @aux10
MOV EBX, _a@main
MOV _a@main@print_suma_tres_valo,EBX
MOV EBX, _b@main
MOV _b@main@print_suma_tres_valo,EBX
MOV EBX, _c@main
MOV _c@main@print_suma_tres_valo,EBX
CALL PROCLabel2
invoke printf, cfm$("%s\n"), OFFSET @aux13
invoke printf, cfm$("%s\n"), OFFSET @aux14
CALL PROCLabel3

jmp $ ; ignoren esta croteada, es para que no se cierre la consola xD
END START