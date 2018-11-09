package org.jb.cce.uast

class FileNode(val path: String) : UnifiedAstNode {

    val classes = mutableListOf<ClassNode>()
    val globalFunctionCalls = mutableListOf<FunctionCallNode>()
    val globalVariableUsages = mutableListOf<VariableUsageNode>()
    val functions = mutableListOf<FunctionNode>()
}