package org.jb.cce.uast

class VariableUsageNode(beforeText: String,
                        name: String,
                        afterText: String,
                        offset: Int) : RValueNode(beforeText, name, afterText, offset)