package org.jb.cce.uast

import org.jb.cce.CallWithBracketsNode

class FunctionCallNode(brackets: BracketsNode?,
                        dotOrNew: DotOrNewNode?,
                        text: String,
                        offset: Int) : CallWithBracketsNode(brackets, dotOrNew, text, offset)