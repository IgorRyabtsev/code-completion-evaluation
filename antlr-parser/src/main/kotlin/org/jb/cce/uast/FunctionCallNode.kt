package org.jb.cce.uast

class FunctionCallNode(dotPos: Int?,
                       text: String,
                       offset: Int,
                       isArgument: Boolean) : RValueNode(dotPos, text, offset, isArgument) {

    val arguments = mutableListOf<RValueNode>()
}