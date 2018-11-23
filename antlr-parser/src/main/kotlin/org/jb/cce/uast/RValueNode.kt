package org.jb.cce.uast

abstract class RValueNode(val beforeText: String,
                          val name: String,
                          val afterText: String,
                          val offset: Int) : UnifiedAstNode