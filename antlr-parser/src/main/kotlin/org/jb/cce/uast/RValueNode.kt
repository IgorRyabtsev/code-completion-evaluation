package org.jb.cce.uast

abstract class RValueNode(val brackets: BracketsNode?,
                          val dotOrNew: DotOrNewNode?,
                          val name: String,
                          val offset: Int) : UnifiedAstNode {
    abstract val length: Int
}