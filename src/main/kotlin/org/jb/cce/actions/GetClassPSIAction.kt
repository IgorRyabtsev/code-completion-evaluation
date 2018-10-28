package org.jb.cce.actions

import org.jb.cce.visitors.SimplePSIVisitor
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.LangDataKeys

class GetClassPSIAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent?) {
        val editor = e?.getData(CommonDataKeys.EDITOR) ?: return
        val document = editor.document
        val data = e.getData(LangDataKeys.PSI_FILE)
        val simplePSIVisitor = SimplePSIVisitor()
        data?.accept(simplePSIVisitor)
        println(document.text)
    }

}