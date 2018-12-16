package org.jb.cce

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.fileEditor.FileDocumentManager
import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams
import org.jb.cce.actions.generateActions
import org.jb.cce.interpretator.CompletionInvokerImpl

class CalculateCompletionForSelectedFileAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent?) {

        val document = e?.getData(LangDataKeys.EDITOR)?.document ?:return
        val currentFile = FileDocumentManager.getInstance().getFile(document)
        val filePath = currentFile!!.path

        // set up
        val completionInvoker = CompletionInvokerImpl(e.project!!)
        val interpretator = Interpretator(completionInvoker)

        // generate actions
        val lexer = Java8Lexer(CharStreams.fromFileName(filePath))
        val parser = Java8Parser(BufferedTokenStream(lexer))
        val tree = JavaVisitor().buildUnifiedAst(filePath, parser)

        val generatedActions = generateActions(tree)
        val completions = interpretator.interpret(generatedActions)
        println("Completion quality evaluation for selected file")
        println("Precision Metric value = " + PrecisionMetricsEvaluator.evaluate(completions))
        println("Recall Metric value = " + RecallMetricsEvaluator.evaluate(completions))
        println("FMeasure Metric value = " + FMeasureMetricsEvaluator.evaluate(completions))
    }
}