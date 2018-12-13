package org.jb.cce.uast

class BracketsNode(val leftPos: Int,
                   val rightPos: Int,
                   val leftText: String,
                   val rightText: String) : UnifiedAstNode {
    val length = leftText.length + rightText.length
}