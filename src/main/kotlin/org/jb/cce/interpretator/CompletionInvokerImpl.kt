package org.jb.cce.interpretator

import com.intellij.codeInsight.completion.CodeCompletionHandlerBase
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupManager
import com.intellij.codeInsight.lookup.impl.LookupImpl
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import java.io.File


class CompletionInvokerImpl(private val project: Project) : CompletionInvoker {

    private var editor: Editor? = null
    override fun moveCaret(offset: Int) {
        editor!!.caretModel.moveToOffset(offset)
    }

    override fun callCompletion(): List<String> {
        CodeCompletionHandlerBase(CompletionType.BASIC).invokeCompletion(project, editor) // smart?
        val lookup = LookupManager.getActiveLookup(editor) as LookupImpl?
        val resultCompletion = mutableListOf<String>()
        lookup?.items?.toTypedArray()?.forEach { item -> resultCompletion.add(item.lookupString) }
        return resultCompletion
    }

    override fun printText(text: String) {
        val document = editor!!.document
        val project = editor!!.project
        val runnable = Runnable { document.insertString(editor!!.caretModel.offset, text) }
        WriteCommandAction.runWriteCommandAction(project, runnable)
        editor!!.caretModel.moveToOffset(editor!!.caretModel.offset + text.length)
    }

    override fun deleteRange(begin: Int, end: Int) {
        val document = editor!!.document
        val project = editor!!.project
        val runnable = Runnable { document.deleteString(begin, end) }
        WriteCommandAction.runWriteCommandAction(project, runnable)
    }

    override fun openFile(file: String) {
        val virtualFile = LocalFileSystem.getInstance().findFileByIoFile(File(file))
        val fileEditor = FileEditorManager.getInstance(project).openFile(virtualFile!!, false)[0]
        editor = (fileEditor as TextEditor).editor
    }
}