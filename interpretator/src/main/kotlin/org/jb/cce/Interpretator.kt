package org.jb.cce

import org.jb.cce.actions.*
import org.jb.cce.exception.UnexpectedActionException


class Interpretator(private val invoker: CompletionInvoker) {

    fun interpret(actions: List<Action>): List<Session> {
        val result = mutableListOf<Session>()
        var currentSession: Session? = null
        var currentOpenedFile = ""
        for (action in actions) {
            when (action) {
                is MoveCaret -> invoker.moveCaret(action.offset)
                is CallCompletion -> {
                    if (currentSession == null) {
                        currentSession = Session()
                    }
                    currentSession.completions.add(action.text)
                    currentSession.lookups.add(invoker.callCompletion())
                }
                is CancelSession -> {
                    if (currentSession == null) {
                        throw UnexpectedActionException("Session canceled before created")
                    }
                    result.add(currentSession)
                    currentSession = null
                }
                is PrintText -> invoker.printText(action.text)
                is DeleteRange -> invoker.deleteRange(action.begin, action.end)
                is OpenFile -> {
                    if (!currentOpenedFile.isEmpty()) {
                        invoker.closeFile(currentOpenedFile)
                    }
                    invoker.openFile(action.file)
                    currentOpenedFile = action.file
                }
            }
        }
        return result
    }
}
