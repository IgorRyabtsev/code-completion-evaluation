package org.jb.cce.actions

sealed class Action(val type: ActionType) {
    enum class ActionType {
        MOVE_CARET, CALL_COMPLETION, CANCEL_SESSION, PRINT_TEXT, DELETE_RANGE, OPEN_FILE
    }
}

data class MoveCaret(val offset: Int) : Action(ActionType.MOVE_CARET)
class CallCompletion(val text: String) : Action(ActionType.CALL_COMPLETION)
class CancelSession : Action(ActionType.CANCEL_SESSION)
data class PrintText(val text: String) : Action(ActionType.PRINT_TEXT)
data class DeleteRange(val begin: Int, val end: Int) : Action(ActionType.DELETE_RANGE)
data class OpenFile(val file: String) : Action(ActionType.OPEN_FILE)
