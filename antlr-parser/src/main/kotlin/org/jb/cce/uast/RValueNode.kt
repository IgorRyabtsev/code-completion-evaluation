package org.jb.cce.uast

abstract class RValueNode(val dotPos: Int?,
                          val name: String,
                          val offset: Int,
                          val isArgument: Boolean) : UnifiedAstNode