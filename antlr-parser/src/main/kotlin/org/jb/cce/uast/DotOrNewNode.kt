package org.jb.cce.uast

class DotOrNewNode(val text: String, val offset: Int) : UnifiedAstNode {
    val length = text.length
}