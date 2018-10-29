package org.jb.cce.visitors

import com.intellij.psi.JavaRecursiveElementVisitor
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.util.PsiTreeUtil

class SimplePSIVisitor(private val targetClass: PsiClass) : JavaRecursiveElementVisitor() {

    val methodToExpressions: MutableMap<PsiMethod, MutableList<PsiMethodCallExpression>> = mutableMapOf()

    override fun visitMethod(method: PsiMethod) {
        if (method.parent == targetClass) {
            methodToExpressions[method] = ArrayList()
            super.visitMethod(method)
        }
    }

    override fun visitMethodCallExpression(expression: PsiMethodCallExpression) {
        val mainMethod = PsiTreeUtil.getParentOfType(expression.parent, PsiMethod::class.java)
        if (mainMethod != null) {
            val expressions = methodToExpressions[mainMethod]
            expressions!!.add(expression)
        }
        super.visitMethodCallExpression(expression)
    }
}