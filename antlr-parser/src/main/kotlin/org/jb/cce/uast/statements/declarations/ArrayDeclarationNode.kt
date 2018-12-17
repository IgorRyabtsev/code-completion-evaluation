package org.jb.cce.uast.statements.declarations

import org.jb.cce.uast.statements.expressions.ExpressionNode

class ArrayDeclarationNode(name: String,
                           offset: Int,
                           length: Int) : DeclarationNode(name, offset, length) {

    private val initExpresstions = mutableListOf<ExpressionNode>()

    fun setInitExpression(expression: ExpressionNode) {
        initExpresstions += expression
    }

    override fun getChildren() = initExpresstions
}