package org.jb.cce

import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams
import java.io.File
import java.io.FileWriter

class SampleCompletionInvoker : CompletionInvoker {

    var text: String = ""
    var position: Int = 0

    override fun moveCaret(offset: Int) {
        position = offset
    }

    override fun callCompletion(): List<String> {
        return listOf(text)
    }

    override fun printText(text: String) {
        this.text = this.text.substring(0, position) + text + this.text.substring(position)
        moveCaret(position + text.length)
    }

    override fun deleteRange(begin: Int, end: Int) {
        text = text.substring(0, begin) + text.substring(end)
    }

    override fun openFile(file: String) {
        text = File(file).bufferedReader().use { it.readText() }
    }
}

fun main(args: Array<String>) {
    if (args.size != 2) {
        System.err.println("Expected arguments: file_to_process, file_for_actions")
        System.exit(1)
    }
    val lexer = Java8Lexer(CharStreams.fromFileName(args[0]))
    val parser = Java8Parser(BufferedTokenStream(lexer))
    val tree = JavaVisitor().buildUnifiedAst(args[0], parser)
    val out = FileWriter(args[1])
    out.append(ActionSerializer().serialize(generateActions(tree)))
    out.close()
    val json = File(args[1]).bufferedReader().use { it.readText() }
    val actions = ActionSerializer().deserialize(json)
    Interpreter(SampleCompletionInvoker()).interpret(actions).forEach {
        it.lookups.forEach { snippet -> println("${snippet[0]}\n=== end of snippet ===") }
        println("=== end of session ===")
    }
}
