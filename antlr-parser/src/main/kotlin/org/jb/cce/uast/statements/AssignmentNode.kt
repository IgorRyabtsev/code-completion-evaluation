package org.jb.cce.uast.statements

import org.jb.cce.uast.statements.expressions.ExpressionNode
import org.jb.cce.uast.statements.expressions.references.ReferenceNode

class AssignmentNode(offset: Int,
                     length: Int) : StatementNode(offset, length) {

    private lateinit var reference: ReferenceNode
    private lateinit var assigned: ExpressionNode

    fun setReference(reference: ReferenceNode) {
        this.reference = reference
    }

    fun setAssigned(expression: ExpressionNode) {
        assigned = expression
    }

    override fun getChildren() = listOf(reference, assigned)
}