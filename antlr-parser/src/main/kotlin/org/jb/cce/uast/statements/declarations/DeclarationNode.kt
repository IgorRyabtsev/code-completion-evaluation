package org.jb.cce.uast.statements.declarations

import org.jb.cce.uast.statements.StatementNode

abstract class DeclarationNode(private val name: String,
                               offset: Int,
                               length: Int) : StatementNode(offset, length) {

    open fun getName() = name
}