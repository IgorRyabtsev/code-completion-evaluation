package org.jb.cce.interpretatoractions

sealed class Action

data class MoveCaret(val offset: Int) : Action()
class CallCompletion : Action()
class CancelSession : Action()
data class PrintText(val text: String) : Action()
data class DeleteRange(val begin: Int, val end: Int) : Action()
data class OpenFile(val file: String) : Action()
