package org.jb.cce.visitors

import com.intellij.psi.JavaRecursiveElementVisitor
import com.intellij.psi.PsiAnonymousClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.util.PsiTreeUtil

class SimplePSIVisitor : JavaRecursiveElementVisitor() {

    private val methodAndExpressions: MutableMap<PsiMethod, MutableList<PsiMethodCallExpression>> = mutableMapOf()

    override fun visitMethod(method: PsiMethod) {
        if (method.context !is PsiAnonymousClass) {
            methodAndExpressions[method] = ArrayList()
        }
        super.visitMethod(method)
    }

    override fun visitMethodCallExpression(expression: PsiMethodCallExpression) {
        var parentNodeOfExpression = expression.parent
        var mainMethod = PsiTreeUtil.getParentOfType(parentNodeOfExpression, PsiMethod::class.java)
        if (mainMethod != null) {
            var expressions = methodAndExpressions[mainMethod]
            while (expressions == null) {
                parentNodeOfExpression = parentNodeOfExpression.parent
                mainMethod = PsiTreeUtil.getParentOfType(parentNodeOfExpression, PsiMethod::class.java)
                expressions = methodAndExpressions[mainMethod]
            }
            expressions.add(expression)
        }
        super.visitMethodCallExpression(expression)
    }
}