package org.jb.cce.interpretator

import org.jb.cce.interpretatoractions.*
import org.jb.cce.interpretatoractions.exception.UnexpectedActionException

class Interpretator(private val invoker: CompletionInvoker) {

    fun interpret(actions: List<Action>): List<Session> {
        val result = mutableListOf<Session>()
        var currentSession: Session? = null
        for (action in actions) {
            when (action) {
                is MoveCaret -> invoker.moveCaret(action.offset)
                is CallCompletion -> {
                    if (currentSession == null) {
                        currentSession = org.jb.cce.interpretator.Session()
                    }
                    currentSession.lookups.add(invoker.callCompletion())
                }
                is CancelSession -> {
                    if (currentSession == null) {
                        throw UnexpectedActionException("Session canceled before created")
                    }
                    result.add(currentSession)
                }
                is PrintText -> invoker.printText(action.text)
                is DeleteRange -> invoker.deleteRange(action.begin, action.end)
                is OpenFile -> invoker.openFile(action.file)
            }
        }
        return result
    }
}
