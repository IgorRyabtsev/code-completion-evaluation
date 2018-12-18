package org.jb.cce

import org.jb.cce.actions.Action
import org.jb.cce.actions.CallCompletion
import org.jb.cce.actions.CancelSession
import org.jb.cce.actions.MoveCaret
import org.jb.cce.actions.PrintText
import org.jb.cce.actions.DeleteRange
import org.jb.cce.actions.OpenFile
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
                is OpenFile -> invoker.openFile(action.file)
            }
        }
        return result
    }
}
