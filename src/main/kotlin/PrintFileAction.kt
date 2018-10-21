import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.psi.*

class PrintFileAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent?) {
        val editor = e?.getData(CommonDataKeys.EDITOR);
        val document = editor?.document;
        val data = e?.getData(LangDataKeys.PSI_FILE)
        data?.accept(SimplePSIVisitor())
        println(document?.text)
    }

    class SimplePSIVisitor : JavaRecursiveElementVisitor() {
        override fun visitKeyword(keyword: PsiKeyword?) {
            println("keyword " + keyword.toString())
            super.visitKeyword(keyword)
        }

        override fun visitParameterList(list: PsiParameterList?) {
            println("list " + list.toString())
            super.visitParameterList(list)
        }

        override fun visitVariable(variable: PsiVariable?) {
            println("variable " + variable.toString())
            println("variable " + variable?.textRange?.startOffset)
            println("variable " + variable?.textRange?.endOffset)
            super.visitVariable(variable)
        }

        override fun visitArrayInitializerExpression(expression: PsiArrayInitializerExpression?) {
            println("expression " + expression.toString())
            super.visitArrayInitializerExpression(expression)
        }

        override fun visitBinaryExpression(expression: PsiBinaryExpression?) {
            println("expression " + expression.toString())
            super.visitBinaryExpression(expression)
        }

        override fun visitField(field: PsiField?) {
            println("field " + field.toString())
            super.visitField(field)
        }
    }
}