package org.jb.cce.uast.statements.declarations.blocks

import org.jb.cce.uast.statements.StatementNode
import org.jb.cce.uast.statements.declarations.DeclarationNode

abstract class BlockNode(offset: Int,
                length: Int) : DeclarationNode("", offset, length) {

    protected val bodyStatements = mutableListOf<StatementNode>()

    fun addStatement(statement: StatementNode) {
        bodyStatements += statement
    }

    override fun getChildren(): List<StatementNode> = bodyStatements
}