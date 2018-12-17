package org.jb.cce.uast.statements.declarations.blocks

import org.jb.cce.uast.statements.StatementNode
import org.jb.cce.uast.statements.declarations.BlockNode

class NamedBlockNode(private val name: String,
                     offset: Int,
                     length: Int) : BlockNode(offset, length) {

    override fun getName() = name

    private val arguments = mutableListOf<StatementNode>()

    fun addArgument(argument: StatementNode) {
        arguments += argument
    }

    override fun getChildren() = arguments + bodyStatements
}