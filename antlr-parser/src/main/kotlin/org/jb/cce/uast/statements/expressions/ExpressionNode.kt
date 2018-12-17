package org.jb.cce.uast.statements.expressions

import org.jb.cce.uast.statements.StatementNode

abstract class ExpressionNode(offset: Int,
                              length: Int) : StatementNode(offset, length)