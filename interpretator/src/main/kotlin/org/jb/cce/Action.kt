package org.jb.cce

import org.jb.cce.Action.ActionType.*

sealed class Action(val type: ActionType) {
    enum class ActionType {
        MOVE_CARET, CALL_COMPLETION, CANCEL_SESSION, PRINT_TEXT, DELETE_RANGE, OPEN_FILE
    }
}

data class MoveCaret(val offset: Int) : Action(MOVE_CARET)
class CallCompletion : Action(CALL_COMPLETION)
class CancelSession : Action(CANCEL_SESSION)
data class PrintText(val text: String) : Action(PRINT_TEXT)
data class DeleteRange(val begin: Int, val end: Int) : Action(DELETE_RANGE)
data class OpenFile(val file: String) : Action(OPEN_FILE)

class Session {
    val lookups: MutableList<List<String>> = mutableListOf()
}

class UnexpectedActionException(message: String) : Exception(message)