package org.jb.cce.uast

class ClassNode(val name: String) : UnifiedAstNode {

    val subclasses = mutableListOf<ClassNode>()
    val initFunctionCalls = mutableListOf<FunctionCallNode>()
    val initVariableUsages = mutableListOf<VariableUsageNode>()
    val methods = mutableListOf<FunctionNode>()
}