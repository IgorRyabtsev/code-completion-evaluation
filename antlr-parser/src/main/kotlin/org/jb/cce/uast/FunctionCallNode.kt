package org.jb.cce.uast

class FunctionCallNode(val name: String) : RValueNode {

    val arguments = mutableListOf<RValueNode>()

    override fun print(indent: String) {
        kotlin.io.print("$indent$name(")
        for (argument in arguments) {
            argument.print(" ")
        }
        kotlin.io.print(" )")
    }
}