package org.jb.cce.uast

class CompletableNode(private val text: String,
                      offset: Int,
                      length: Int) : UnifiedAstNode(offset, length) {

    fun getText() = text

    override fun getChildren() = listOf<UnifiedAstNode>()
}