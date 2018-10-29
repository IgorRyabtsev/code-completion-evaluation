package org.jb.cce.uast

class VariableUsageNode(val name: String) : RValueNode {

    override fun print(indent: String) {
        kotlin.io.print(indent + name)
    }
}