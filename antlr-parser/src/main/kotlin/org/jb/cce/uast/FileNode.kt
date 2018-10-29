package org.jb.cce.uast

class FileNode : UnifiedAstNode {

    val classes = mutableListOf<ClassNode>()
    val globalFunctionCalls = mutableListOf<FunctionCallNode>()
    val globalVariableUsages = mutableListOf<VariableUsageNode>()
    val functions = mutableListOf<FunctionNode>()

    override fun print(indent: String) {
        for (clazz in classes) {
            clazz.print(indent)
        }
        for (functionCall in globalFunctionCalls) {
            functionCall.print(indent)
            println()
        }
        for (variableUsage in globalVariableUsages) {
            variableUsage.print(indent)
            println()
        }
        for (function in functions) {
            function.print(indent)
        }
    }
}