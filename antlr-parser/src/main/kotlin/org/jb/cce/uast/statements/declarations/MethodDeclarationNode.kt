package org.jb.cce.uast.statements.declarations

import org.jb.cce.uast.statements.declarations.blocks.MethodBodyNode

class MethodDeclarationNode(offset: Int,
                            length: Int) : DeclarationNode("", offset, length) {

    private lateinit var header: MethodHeaderNode
    private var body: MethodBodyNode? = null

    fun setHeader(header: MethodHeaderNode) {
        this.header = header
    }

    fun setBody(body: MethodBodyNode) {
        this.body = body
    }

    override fun getName() = header.getName()

    override fun getChildren() = header.getChildren() + (body?.getChildren() ?: listOf())
}