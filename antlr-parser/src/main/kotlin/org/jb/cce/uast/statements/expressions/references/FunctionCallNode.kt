package org.jb.cce.uast.statements.expressions.references

import org.jb.cce.uast.statements.expressions.ExpressionNode

class FunctionCallNode(name: String,
                       offset: Int,
                       length: Int) : ReferenceNode(name, offset, length) {

    private val arguments = mutableListOf<ExpressionNode>()

    fun addArgument(argument: ExpressionNode) {
        arguments += argument
    }

    override fun getChildren() = prefixReference?.let { listOf(it) + arguments } ?: arguments
}