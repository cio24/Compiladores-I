#!/bin/bash

yacc -J specification.y
sed -i '1s/^/package lexicalAnalyzerPackage;\n/' ParserVal.java
mv ./Parser.java ../src/lexicalAnalyzerPackage
mv ./ParserVal.java ../src/lexicalAnalyzerPackage
