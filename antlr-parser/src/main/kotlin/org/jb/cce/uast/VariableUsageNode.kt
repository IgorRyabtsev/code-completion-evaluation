package org.jb.cce.uast

class VariableUsageNode(dotPos: Int?,
                        text: String,
                        offset: Int,
                        isArgument: Boolean) : RValueNode(dotPos, text, offset, isArgument)