package org.jb.cce.uast

import org.jb.cce.uast.statements.declarations.DeclarationNode

class FileNode(offset: Int,
               length: Int) : UnifiedAstNode(offset, length) {

    private val declarations = mutableListOf<DeclarationNode>()

    fun addDeclaration(declaration: DeclarationNode) {
        declarations += declaration
    }

    override fun getChildren() = declarations
}