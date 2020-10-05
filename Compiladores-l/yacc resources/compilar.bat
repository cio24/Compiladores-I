yacc.exe -J specification.y
copy ParserVal.java temp.txt
echo.package lexicalAnalyzerPackage;>ParserVal.java
type temp.txt >>ParserVal.java
del temp.txt
move .\Parser.java ..\src\lexicalAnalyzerPackage\
move .\ParserVal.java ..\src\lexicalAnalyzerPackage\