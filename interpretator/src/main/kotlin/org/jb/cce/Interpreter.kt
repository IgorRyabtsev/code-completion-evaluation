@file:Suppress("CanSealedSubClassBeObject")

package org.jb.cce

interface CompletionInvoker {
    fun moveCaret(offset: Int)
    fun callCompletion(): List<String>
    fun printText(text: String)
    fun deleteRange(begin: Int, end: Int)
    fun openFile(file: String)
}

class Interpreter(private val invoker: CompletionInvoker) {

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
