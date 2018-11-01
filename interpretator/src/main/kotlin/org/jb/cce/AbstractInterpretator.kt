package org.jb.cce

interface CompletionInvoker {
    fun moveCaret(offset: Int)
    fun callCompletion(): List<String>
    fun printText(text: String)
    fun deleteRange(begin: Int, end: Int)
    fun openFile(file: String)
}

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
                }
                is PrintText -> invoker.printText(action.text)
                is DeleteRange -> invoker.deleteRange(action.begin, action.end)
                is OpenFile -> invoker.openFile(action.file)
            }
        }
        return result
    }
}

sealed class Action

data class MoveCaret(val offset: Int) : Action()
class CallCompletion : Action()
class CancelSession : Action()
data class PrintText(val text: String) : Action()
data class DeleteRange(val begin: Int, val end: Int) : Action()
data class OpenFile(val file: String) : Action()

class Session {
    val lookups: MutableList<List<String>> = mutableListOf()
}

class UnexpectedActionException(message: String) : Exception(message)