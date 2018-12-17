package org.jb.cce.uast.statements.declarations

import org.jb.cce.uast.statements.expressions.ExpressionNode

class VariableDeclarationNode(name: String,
                              offset: Int,
                              length: Int) : DeclarationNode(name, offset, length) {

    private var initExpresstion: ExpressionNode? = null

    fun setInitExpression(expression: ExpressionNode) {
        initExpresstion = expression
    }

    override fun getChildren() = initExpresstion?.let { listOf(it) } ?: listOf()
}