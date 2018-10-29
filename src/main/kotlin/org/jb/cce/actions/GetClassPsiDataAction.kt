package org.jb.cce.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.psi.impl.source.PsiJavaFileImpl
import org.jb.cce.visitors.SimplePSIVisitor

class GetClassPsiDataAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent?) {

        val data = e?.getData(LangDataKeys.PSI_FILE) ?: return
        val classesInCurrentFile = (e.getData(CommonDataKeys.PSI_FILE) as PsiJavaFileImpl).classes
        if (classesInCurrentFile.isEmpty()) {
            return
        }
        // visit first class in file
        val simplePsiVisitor = SimplePSIVisitor(classesInCurrentFile[0])
        data.accept(simplePsiVisitor)

        // print information about methods and expressions inside them
        simplePsiVisitor.methodToExpressions.forEach { (key, value) ->
            run {
                println("Method \"${key.name}\" expression calls:")
                value.forEach { expr -> println(expr.text) }
            }
        }
    }

}