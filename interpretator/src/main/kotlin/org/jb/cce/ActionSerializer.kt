package org.jb.cce

import com.google.gson.Gson
import org.jb.cce.Action.ActionType.*

class ActionSerializer {

    private val gson = Gson()

    private fun serialize(action: Action): String {
        return gson.toJson(action)
    }

    fun serialize(actions: List<Action>): String {
        return actions.asSequence().map { serialize(it) }.joinToString(", ", "[", "]")
    }

    private fun deserialize(action: Map<String, Any>): Action {
        return when (action["type"]) {
            MOVE_CARET.name -> MoveCaret((action["offset"] as Double).toInt())
            CALL_COMPLETION.name -> CallCompletion()
            CANCEL_SESSION.name -> CancelSession()
            PRINT_TEXT.name -> PrintText(action["text"] as String)
            DELETE_RANGE.name ->
                DeleteRange((action["begin"] as Double).toInt(), (action["end"] as Double).toInt())
            OPEN_FILE.name -> OpenFile(action["file"] as String)
            else -> throw UnexpectedActionException("Incorrect action type")
        }
    }

    fun deserialize(json: String): List<Action> {
        val list = gson.fromJson(json, mutableListOf<Map<String, Any>>().javaClass)
        return list.asSequence().map { deserialize(it) }.toList()
    }
}
