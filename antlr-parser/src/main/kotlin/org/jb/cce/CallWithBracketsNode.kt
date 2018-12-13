package org.jb.cce

import org.jb.cce.uast.BracketsNode
import org.jb.cce.uast.DotOrNewNode
import org.jb.cce.uast.RValueNode

abstract class CallWithBracketsNode(brackets: BracketsNode?,
                           dotOrNew: DotOrNewNode?,
                           text: String,
                           offset: Int) : RValueNode(brackets, dotOrNew, text, offset) {
    var argumentsLength = 0

    override val length: Int
        get() = (brackets?.length ?: 0) + (dotOrNew?.length ?: 0) + name.length + argumentsLength

    private val arguments = mutableListOf<RValueNode>()

    fun getArguments() = arguments.toList()

    fun addArgument(arg: RValueNode) {
        arguments.add(arg)
        argumentsLength += arg.length
    }
}
