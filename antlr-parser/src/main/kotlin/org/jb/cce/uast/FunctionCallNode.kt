package org.jb.cce.uast

class FunctionCallNode(beforeText: String,
                       name: String,
                       afterText: String,
                       offset: Int) : RValueNode(beforeText, name, afterText, offset) {

    val arguments = mutableListOf<RValueNode>()
}