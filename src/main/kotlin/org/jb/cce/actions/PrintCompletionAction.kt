package org.jb.cce.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams
import org.jb.cce.Java8Lexer
import org.jb.cce.Java8Parser
import org.jb.cce.antlr.JavaVisitor
import org.jb.cce.completion.CompletionEvaluator
import org.jb.cce.interpretator.CompletionInvokerImpl
import org.jb.cce.interpretator.Interpretator
import org.jb.cce.interpretatoractions.*


class PrintCompletionAction : AnAction() {

    private val filePath = "/Users/igorryabtsev/work/gittaxos/taxos/src/main/java/com/tool/taxonomy/controller/TaxonomyController.java"

    override fun actionPerformed(e: AnActionEvent?) {
        // set up
        val completionInvoker = CompletionInvokerImpl(e?.project!!)
        val interpretator = Interpretator(completionInvoker)

        // generate actions
        val lexer = Java8Lexer(CharStreams.fromFileName(filePath))
        val parser = Java8Parser(BufferedTokenStream(lexer))
        val tree = JavaVisitor().buildUnifiedAst(filePath, parser)

        val generatedActions = generateActions(tree)
        val completions = interpretator.interpret(generatedActions)

        val evaluator = CompletionEvaluator(generatedActions, completions)
        println(evaluator.getPrecisionMetric())
        println(evaluator.getRecallMetric())
        println(evaluator.getFMeasureMetric())
    }
}