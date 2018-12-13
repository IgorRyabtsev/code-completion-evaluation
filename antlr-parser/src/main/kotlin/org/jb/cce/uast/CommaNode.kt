package org.jb.cce.uast

class CommaNode(offset: Int) : RValueNode(null, null, ",", offset) {
    override val length: Int
        get() = 1
}