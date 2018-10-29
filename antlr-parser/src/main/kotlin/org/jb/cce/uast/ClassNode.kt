package org.jb.cce.uast

class ClassNode(val name: String) : UnifiedAstNode {

    val subclasses = mutableListOf<ClassNode>()
    val initFunctionCalls = mutableListOf<FunctionCallNode>()
    val initVariableUsages = mutableListOf<VariableUsageNode>()
    val methods = mutableListOf<FunctionNode>()

    override fun print(indent: String) {
        println(indent + "class $name {")
        for (clazz in subclasses) {
            clazz.print("$indent  ")
        }
        for (functionCall in initFunctionCalls) {
            functionCall.print("$indent  ")
            println()
        }
        for (variableUsage in initVariableUsages) {
            variableUsage.print("$indent  ")
            println()
        }
        for (method in methods) {
            method.print("$indent  ")
        }
        println("$indent}")
    }

}