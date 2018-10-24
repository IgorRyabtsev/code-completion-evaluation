package org.jb.cce

import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams

fun main(args: Array<String>) {
    if (args.size != 1) {
        System.err.println("Expected arguments: file_to_execute")
        System.exit(1)
    }
    val lexer = Java8Lexer(CharStreams.fromFileName(args[0]))
    val parser = Java8Parser(BufferedTokenStream(lexer))
    println(parser.compilationUnit().toStringTree(parser))
}