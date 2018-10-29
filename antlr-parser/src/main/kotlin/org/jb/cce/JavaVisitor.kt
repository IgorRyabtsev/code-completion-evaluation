package org.jb.cce

import org.jb.cce.uast.*

class JavaVisitor : Java8BaseVisitor<Unit>() {

    private var currentNode: UnifiedAstNode = FileNode()

    fun buildUnifiedAst(parser: Java8Parser): FileNode {
        visit(parser.compilationUnit())
        return currentNode as FileNode
    }

    override fun visitCompilationUnit(ctx: Java8Parser.CompilationUnitContext) {
        currentNode = FileNode()
        ctx.children.forEach { visit(it) }
    }

    override fun visitNormalClassDeclaration(ctx: Java8Parser.NormalClassDeclarationContext) {
        val parentNode = currentNode
        currentNode = ClassNode(ctx.Identifier().text)
        ctx.children.forEach { visit(it) }
        when (parentNode) {
            is FileNode -> parentNode.classes.add(currentNode as ClassNode)
            is ClassNode -> parentNode.subclasses.add(currentNode as ClassNode)
            else -> throw UnifiedAstException()
        }
        currentNode = parentNode
    }

    override fun visitConstructorDeclaration(ctx: Java8Parser.ConstructorDeclarationContext) {
        val parentNode = currentNode
        currentNode = FunctionNode()
        ctx.children.forEach { visit(it) }
        when (parentNode) {
            is ClassNode -> parentNode.methods.add(currentNode as FunctionNode)
            else -> throw UnifiedAstException()
        }
        currentNode = parentNode
    }

    override fun visitMethodDeclaration(ctx: Java8Parser.MethodDeclarationContext) {
        val parentNode = currentNode
        currentNode = FunctionNode(ctx.methodHeader().methodDeclarator().Identifier().text)
        ctx.children.forEach { visit(it) }
        when (parentNode) {
            is ClassNode -> parentNode.methods.add(currentNode as FunctionNode)
            else -> throw UnifiedAstException()
        }
        currentNode = parentNode
    }

    override fun visitExpressionName(ctx: Java8Parser.ExpressionNameContext) {
        val parentNode = currentNode
        currentNode = VariableUsageNode(ctx.Identifier().text)
        ctx.children.forEach { visit(it) }
        when (parentNode) {
            is ClassNode -> parentNode.initVariableUsages.add(currentNode as VariableUsageNode)
            is FunctionNode -> parentNode.variableUsages.add(currentNode as VariableUsageNode)
            is FunctionCallNode -> parentNode.arguments.add(currentNode as VariableUsageNode)
            else -> throw UnifiedAstException()
        }
        currentNode = parentNode
    }

    override fun visitMethodInvocation(ctx: Java8Parser.MethodInvocationContext) {
        val parentNode = currentNode
        currentNode = FunctionCallNode(ctx.Identifier()?.text ?: ctx.methodName().Identifier().text)
        ctx.children.forEach { visit(it) }
        addMethodInvocationToParentNode(parentNode)
        currentNode = parentNode
    }

    override fun visitMethodInvocation_lf_primary(ctx: Java8Parser.MethodInvocation_lf_primaryContext) {
        val parentNode = currentNode
        ctx.children.forEach { visit(it) }
        addMethodInvocationToParentNode(parentNode)
        currentNode = parentNode
    }

    override fun visitMethodInvocation_lfno_primary(ctx: Java8Parser.MethodInvocation_lfno_primaryContext) {
        val parentNode = currentNode
        currentNode = FunctionCallNode(ctx.Identifier()?.text ?: ctx.methodName().Identifier().text)
        ctx.children.forEach { visit(it) }
        addMethodInvocationToParentNode(parentNode)
        currentNode = parentNode
    }

    private fun addMethodInvocationToParentNode(parentNode: UnifiedAstNode) {
        when (parentNode) {
            is ClassNode -> parentNode.initFunctionCalls.add(currentNode as FunctionCallNode)
            is FunctionNode -> parentNode.functionCalls.add(currentNode as FunctionCallNode)
            is FunctionCallNode -> parentNode.arguments.add(currentNode as FunctionCallNode)
            else -> throw UnifiedAstException()
        }
    }
}
