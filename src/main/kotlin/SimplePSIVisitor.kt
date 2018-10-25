import com.intellij.psi.JavaRecursiveElementVisitor
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.util.PsiTreeUtil

class SimplePSIVisitor : JavaRecursiveElementVisitor() {

    var methodAndExpressions: HashMap<PsiMethod, ArrayList<PsiMethodCallExpression>> = HashMap()

    override fun visitMethod(method: PsiMethod) {
        methodAndExpressions[method] = ArrayList()
        super.visitMethod(method)
    }

    override fun visitMethodCallExpression(expression: PsiMethodCallExpression) {
        val mainMethod = PsiTreeUtil.getParentOfType(expression.context, PsiMethod::class.java)
        if (mainMethod != null) {
            val expressions = methodAndExpressions[mainMethod]
            expressions!!.add(expression)
        }
        super.visitMethodCallExpression(expression)
    }
}