package org.jb.cce.uast.statements.expressions.references

import org.jb.cce.uast.CompletableNode

class VariableAccessNode(private val name: CompletableNode,
                         offset: Int,
                         length: Int) : ReferenceNode(name.getText(), offset, length) {

    override fun getChildren() = prefixReference?.let { listOf(it, name) } ?: listOf(name)
}