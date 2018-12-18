package org.jb.cce.uast.statements.expressions.references

import org.jb.cce.uast.CompletableNode
import org.jb.cce.uast.statements.expressions.ExpressionNode

class ArrayAccessNode(private val name: CompletableNode,
                      offset: Int,
                      length: Int) : ReferenceNode(name.getText(), offset, length) {

    private val indices = mutableListOf<ExpressionNode>()

    fun addIndex(index: ExpressionNode) {
        indices += index
    }

    override fun getChildren() = prefixReference?.let { listOf(it, name) + indices } ?:listOf(name) + indices
}