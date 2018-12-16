package org.jb.cce

import com.intellij.ide.highlighter.JavaFileType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.FileBasedIndex
import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams
import org.jb.cce.actions.Action
import org.jb.cce.actions.generateActions
import org.jb.cce.interpretator.CompletionInvokerImpl
import java.util.stream.Collectors

class CaclucateCompletionForAllProjectFilesAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent?) {
        val project = e?.project ?:return
        val containingFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, JavaFileType.INSTANCE,
                GlobalSearchScope.projectScope(project))
        val generatedActions = mutableListOf<List<Action>>()
        for (javaFile in containingFiles) {
            val lexer = Java8Lexer(CharStreams.fromFileName(javaFile.path))
            val parser = Java8Parser(BufferedTokenStream(lexer))
            val tree = JavaVisitor().buildUnifiedAst(javaFile.path, parser)
            generatedActions.add(generateActions(tree))
        }
        val completionInvoker = CompletionInvokerImpl(e.project!!)
        val interpretator = Interpretator(completionInvoker)
        val actions = generatedActions.stream()
                .flatMap { l -> l.stream() }
                .collect(Collectors.toList())
        val completions = interpretator.interpret(actions)
        println("Completion quality evaluation for all project")
        println("Precision Metric value = " + PrecisionMetricsEvaluator.evaluate(completions))
        println("Recall Metric value = " + RecallMetricsEvaluator.evaluate(completions))
        println("FMeasure Metric value = " + FMeasureMetricsEvaluator.evaluate(completions))
    }
}