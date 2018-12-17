package org.jb.cce.uast.statements.declarations

import org.jb.cce.uast.statements.StatementNode

abstract class BlockNode(offset: Int,
                length: Int) : DeclarationNode("", offset, length) {

    protected val bodyStatements = mutableListOf<StatementNode>()

    fun addStatement(statement: StatementNode) {
        bodyStatements += statement
    }

    override fun getChildren() = bodyStatements.toList()
}