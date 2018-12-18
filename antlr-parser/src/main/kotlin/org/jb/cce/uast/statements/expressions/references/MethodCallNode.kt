package org.jb.cce.uast.statements.expressions.references

import org.jb.cce.uast.CompletableNode
import org.jb.cce.uast.statements.expressions.ExpressionNode

class MethodCallNode(private val name: CompletableNode,
                     offset: Int,
                     length: Int) : ReferenceNode(name.getText(), offset, length) {

    private val arguments = mutableListOf<ExpressionNode>()

    fun addArgument(argument: ExpressionNode) {
        arguments += argument
    }

    override fun getChildren() = prefixReference?.let { listOf(it) + name + arguments} ?: listOf(name) + arguments
}