package org.jb.cce.uast

class VariableUsageNode(val name: String,
                        text: String,
                        offset: Int,
                        isArgument: Boolean) : RValueNode(text, offset, isArgument)