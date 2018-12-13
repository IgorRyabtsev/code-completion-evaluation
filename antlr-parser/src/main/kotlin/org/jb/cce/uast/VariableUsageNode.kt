package org.jb.cce.uast

import org.jb.cce.CallWithBracketsNode

class VariableUsageNode(brackets: BracketsNode?,
                        dotOrNew: DotOrNewNode?,
                        text: String,
                        offset: Int) : CallWithBracketsNode(brackets, dotOrNew, text, offset)