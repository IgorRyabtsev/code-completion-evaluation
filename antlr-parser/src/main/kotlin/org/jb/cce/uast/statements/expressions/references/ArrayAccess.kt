package org.jb.cce.uast.statements.expressions.references

import org.jb.cce.uast.statements.expressions.ExpressionNode

class ArrayAccess(name: String,
                  offset: Int,
                  length: Int) : ReferenceNode(name, offset, length) {

    private val indices = mutableListOf<ExpressionNode>()

    fun addIndex(index: ExpressionNode) {
        indices += index
    }

    override fun getChildren() = prefixReference?.let { listOf(it) + indices } ?: indices
}