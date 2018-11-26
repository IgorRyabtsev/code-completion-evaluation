package org.jb.cce.uast

class FunctionNode(val name: String = "CONSTRUCTOR") : UnifiedAstNode {

    val functionCalls = mutableListOf<FunctionCallNode>()
    val variableUsages = mutableListOf<VariableUsageNode>()
}