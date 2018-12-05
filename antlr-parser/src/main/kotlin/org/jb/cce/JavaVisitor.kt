package org.jb.cce

import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.TerminalNode
import org.jb.cce.uast.*

class JavaVisitor : Java8BaseVisitor<Unit>() {

    private var currentNode: UnifiedAstNode = FileNode("")

    fun buildUnifiedAst(file: String, parser: Java8Parser): FileNode {
        currentNode = FileNode(file)
        visit(parser.compilationUnit())
        return currentNode as FileNode
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
        visitVariableUsage(ctx.Identifier(), ctx)
    }

    override fun visitFieldAccess_lf_primary(ctx: Java8Parser.FieldAccess_lf_primaryContext) {
        visitVariableUsage(ctx.Identifier(), ctx)
    }

    override fun visitFieldAccess_lfno_primary(ctx: Java8Parser.FieldAccess_lfno_primaryContext) {
        visitVariableUsage(ctx.Identifier(), ctx)
    }

    override fun visitMethodInvocation(ctx: Java8Parser.MethodInvocationContext) {
        visitFunctionCall(ctx.Identifier() ?: ctx.methodName().Identifier(), ctx)
    }

    override fun visitMethodInvocation_lf_primary(ctx: Java8Parser.MethodInvocation_lf_primaryContext) {
        visitFunctionCall(ctx.Identifier(), ctx)
    }

    override fun visitMethodInvocation_lfno_primary(ctx: Java8Parser.MethodInvocation_lfno_primaryContext) {
        visitFunctionCall(ctx.Identifier() ?: ctx.methodName().Identifier(), ctx)
    }

    override fun visitClassInstanceCreationExpression(ctx: Java8Parser.ClassInstanceCreationExpressionContext) {
        visitFunctionCall(ctx.Identifier(0), ctx)
    }

    override fun visitClassInstanceCreationExpression_lf_primary(ctx: Java8Parser.ClassInstanceCreationExpression_lf_primaryContext) {
        visitFunctionCall(ctx.Identifier(), ctx)
    }

    override fun visitClassInstanceCreationExpression_lfno_primary(ctx: Java8Parser.ClassInstanceCreationExpression_lfno_primaryContext) {
        visitFunctionCall(ctx.Identifier(0), ctx)
    }

    private fun visitVariableUsage(identifier: TerminalNode, ctx: ParserRuleContext) {
        val start = identifier.symbol.startIndex
        val name = identifier.text
        val parentNode = currentNode
        val newCurrentNode = VariableUsageNode(
            name, name, start, parentNode is FunctionCallNode
        )
        ctx.children.forEach { visit(it) }
        when (parentNode) {
            is ClassNode -> parentNode.initVariableUsages.add(newCurrentNode)
            is FunctionNode -> parentNode.variableUsages.add(newCurrentNode)
            is FunctionCallNode -> parentNode.arguments.add(newCurrentNode)
            else -> throw UnifiedAstException()
        }
    }

    private fun visitFunctionCall(identifier: TerminalNode, ctx: ParserRuleContext) {
        val start = identifier.symbol.startIndex
        val name = identifier.text
        val parentNode = currentNode
        currentNode = FunctionCallNode(
            name, name, start, parentNode is FunctionCallNode
        )
        ctx.children.forEach { visit(it) }
        when (parentNode) {
            is ClassNode -> parentNode.initFunctionCalls.add(currentNode as FunctionCallNode)
            is FunctionNode -> parentNode.functionCalls.add(currentNode as FunctionCallNode)
            is FunctionCallNode -> parentNode.arguments.add(currentNode as FunctionCallNode)
            else -> throw UnifiedAstException()
        }
        currentNode = parentNode
    }
}
