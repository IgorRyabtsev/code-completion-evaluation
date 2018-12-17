package org.jb.cce.uast.statements

import org.jb.cce.uast.UnifiedAstNode

abstract class StatementNode(offset: Int,
                             length: Int) : UnifiedAstNode(offset, length)