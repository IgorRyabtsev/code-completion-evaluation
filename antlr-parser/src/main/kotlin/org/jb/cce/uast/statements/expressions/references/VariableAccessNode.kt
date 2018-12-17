package org.jb.cce.uast.statements.expressions.references

class VariableAccessNode(name: String,
                         offset: Int,
                         length: Int) : ReferenceNode(name, offset, length) {

    override fun getChildren() = prefixReference?.let { listOf(it) } ?: listOf()
}