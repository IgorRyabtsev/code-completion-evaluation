package org.jb.cce

interface CompletionInvoker {
    fun moveCaret(offset: Int)
    fun callCompletion(): List<String>
    fun printText(text: String)
    fun deleteRange(begin: Int, end: Int)
    fun openFile(file: String)
}