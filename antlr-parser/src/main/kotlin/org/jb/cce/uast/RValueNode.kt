package org.jb.cce.uast

abstract class RValueNode(val text: String,
                          val offset: Int,
                          val isArgument: Boolean) : UnifiedAstNode