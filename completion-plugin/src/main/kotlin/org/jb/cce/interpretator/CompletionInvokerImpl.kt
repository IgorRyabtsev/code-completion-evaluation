package org.jb.cce.interpretator

import com.intellij.codeInsight.completion.CodeCompletionHandlerBase
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupManager
import com.intellij.codeInsight.lookup.impl.LookupImpl
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import org.jb.cce.CompletionInvoker
import java.io.File


class CompletionInvokerImpl(private val project: Project) : CompletionInvoker {

    private var editor: Editor? = null
    override fun moveCaret(offset: Int) {
        editor!!.caretModel.moveToOffset(offset)
    }

    override fun callCompletion(): List<String> {
        CodeCompletionHandlerBase(CompletionType.BASIC).invokeCompletion(project, editor)
        if (LookupManager.getActiveLookup(editor) == null) {
            return ArrayList()
        }
        val lookup = LookupManager.getActiveLookup(editor) as LookupImpl
        return lookup.items.toTypedArray().map(LookupElement::toString).toList()
    }

    override fun printText(text: String) {
        val document = editor!!.document
        val project = editor!!.project
        val initialOffset = editor!!.caretModel.offset
        val runnable = Runnable { document.insertString(initialOffset, text) }
        WriteCommandAction.runWriteCommandAction(project, runnable)
        editor!!.caretModel.moveToOffset(initialOffset + text.length)
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

    override fun closeFile(file: String) {
        val virtualFile = LocalFileSystem.getInstance().findFileByIoFile(File(file))
        FileEditorManager.getInstance(project).closeFile(virtualFile!!)
        editor = null
    }
}