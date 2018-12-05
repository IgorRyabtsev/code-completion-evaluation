package org.jb.cce.interpretator

import org.jb.cce.CompletionInvoker
import org.jb.cce.Session
import org.jb.cce.actions.*
import org.jb.cce.exception.UnexpectedActionException


class Interpretator(private val invoker: CompletionInvoker) {

    fun interpret(actions: List<Action>): List<Session> {
        val result = mutableListOf<Session>()
        var currentSession: Session? = null
        for (action in actions) {
            when (action) {
                is MoveCaret -> invoker.moveCaret(action.offset)
                is CallCompletion -> {
                    if (currentSession == null) {
                        currentSession = Session()
                    }
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
                is OpenFile -> invoker.openFile(action.file)
            }
        }
        return result
    }
}
