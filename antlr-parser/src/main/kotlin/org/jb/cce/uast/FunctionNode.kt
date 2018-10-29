package org.jb.cce.uast

class FunctionNode(val name: String = "CONSTRUCTOR") : UnifiedAstNode {

    val functionCalls = mutableListOf<FunctionCallNode>()
    val variableUsages = mutableListOf<VariableUsageNode>()

    override fun print(indent: String) {
        println(indent + "fun $name() {")
        for (functionCall in functionCalls) {
            functionCall.print("$indent  ")
            println()
        }
        for (variableUsage in variableUsages) {
            variableUsage.print("$indent  ")
            println()
        }
        println("$indent}")
    }
}