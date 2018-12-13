package org.jb.cce.uast

private object UnifiedAstNodePrinter {

    fun printClassNode(indent: String, node: ClassNode) {
        println(indent + "class ${node.name} {")
        for (clazz in node.subclasses) {
            clazz.accept("$indent  ")
        }
        for (functionCall in node.initFunctionCalls) {
            functionCall.accept("$indent  ")
            println()
        }
        for (variableUsage in node.initVariableUsages) {
            variableUsage.accept("$indent  ")
            println()
        }
        for (method in node.methods) {
            method.accept("$indent  ")
        }
        println("$indent}")
    }

    fun printFileNode(indent: String, node: FileNode) {
        for (clazz in node.classes) {
            clazz.accept(indent)
        }
        for (functionCall in node.globalFunctionCalls) {
            functionCall.accept(indent)
            println()
        }
        for (variableUsage in node.globalVariableUsages) {
            variableUsage.accept(indent)
            println()
        }
        for (function in node.functions) {
            function.accept(indent)
        }
    }

    fun printFunctionCallNode(indent: String, node: FunctionCallNode) {
        kotlin.io.print("$indent${node.name}(")
        for (argument in node.getArguments()) {
            argument.accept(" ")
        }
        kotlin.io.print(" )")
    }

    fun printFunctionNode(indent: String, node: FunctionNode) {
        println(indent + "fun ${node.name}() {")
        for (functionCall in node.functionCalls) {
            functionCall.accept("$indent  ")
            println()
        }
        for (variableUsage in node.variableUsages) {
            variableUsage.accept("$indent  ")
            println()
        }
        println("$indent}")
    }

    fun printVariableUsageNode(indent: String, node: VariableUsageNode) {
        kotlin.io.print(indent + node.name)
    }
}

interface UnifiedAstNode {
    fun accept(indent: String = "") {
        when(this) {
            is ClassNode -> UnifiedAstNodePrinter.printClassNode(indent, this)
            is FileNode -> UnifiedAstNodePrinter.printFileNode(indent, this)
            is FunctionCallNode -> UnifiedAstNodePrinter.printFunctionCallNode(indent, this)
            is FunctionNode -> UnifiedAstNodePrinter.printFunctionNode(indent, this)
            is VariableUsageNode -> UnifiedAstNodePrinter.printVariableUsageNode(indent, this)
        }
    }
}