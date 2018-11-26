package org.jb.cce.uast

class FunctionCallNode(val name: String,
                       text: String,
                       offset: Int,
                       isArgument: Boolean) : RValueNode(text, offset, isArgument) {

    val arguments = mutableListOf<RValueNode>()
}