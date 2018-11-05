package org.jb.cce.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import org.jb.cce.interpretator.CompletionInvokerImpl
import org.jb.cce.interpretator.Interpretator
import org.jb.cce.interpretatoractions.*


class PrintCompletionAction : AnAction() {

    private val filePath = "/Users/igorryabtsev/work/gittaxos/taxos/src/main/java/com/tool/taxonomy/controller/TaxonomyController.java"
    private val offset = 814
    private val codeForInsertion = "{ \"asdasdasdasdasdsadasdsaddasdasd\"."

    override fun actionPerformed(e: AnActionEvent?) {
        // test case
        val exampleActions = mutableListOf<Action>()
        exampleActions.add(OpenFile(filePath))
        exampleActions.add(MoveCaret(offset))
        exampleActions.add(PrintText(codeForInsertion))
        exampleActions.add(CallCompletion())
        exampleActions.add(DeleteRange(offset, offset + codeForInsertion.length))
        exampleActions.add(CancelSession())
        val completionInvoker = CompletionInvokerImpl(e?.project!!)

        val interpretator = Interpretator(completionInvoker)
        val listOfCompletions = interpretator.interpret(exampleActions)

        listOfCompletions.stream().map { item -> item.lookups }.forEach { completion -> println(completion) }

    }
}