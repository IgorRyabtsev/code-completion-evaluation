package org.jb.cce

import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.TerminalNode
import org.jb.cce.uast.*

class JavaVisitor : Java8BaseVisitor<Unit>() {

    private var currentNode: UnifiedAstNode = FileNode("")

    private var arrayBrackets: BracketsNode? = null
    private var arrayArgument: Java8Parser.ExpressionContext? = null

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
        visitVariableUsage(ctx.Identifier(), ctx, ctx.DOT()?.symbol?.startIndex)
    }

    override fun visitFieldAccess_lf_primary(ctx: Java8Parser.FieldAccess_lf_primaryContext) {
        visitVariableUsage(ctx.Identifier(), ctx, ctx.DOT()?.symbol?.startIndex)
        visitChildren(ctx)
    }

    override fun visitFieldAccess_lfno_primary(ctx: Java8Parser.FieldAccess_lfno_primaryContext) {
        visitVariableUsage(ctx.Identifier(), ctx, ctx.DOT()?.last()?.symbol?.startIndex)
        visitChildren(ctx)
    }

    override fun visitArrayAccess(ctx: Java8Parser.ArrayAccessContext) {
        arrayBrackets = BracketsNode(ctx.LBRACK().last().symbol.startIndex, ctx.RBRACK().last().symbol.startIndex,
                "[", "]")
        arrayArgument = ctx.children[ctx.childCount - 2] as Java8Parser.ExpressionContext
        ctx.children.remove(arrayArgument)
        visitChildren(ctx)
        arrayBrackets = null
    }

    override fun visitArrayAccess_lf_primary(ctx: Java8Parser.ArrayAccess_lf_primaryContext) {
        arrayBrackets = BracketsNode(ctx.LBRACK().last().symbol.startIndex, ctx.RBRACK().last().symbol.startIndex,
                "[", "]")
        arrayArgument = ctx.children[ctx.childCount - 2] as Java8Parser.ExpressionContext
        ctx.children.remove(arrayArgument)
        visitChildren(ctx)
        arrayBrackets = null
    }

    override fun visitArrayAccess_lfno_primary(ctx: Java8Parser.ArrayAccess_lfno_primaryContext) {
        arrayBrackets = BracketsNode(ctx.LBRACK().last().symbol.startIndex, ctx.RBRACK().last().symbol.startIndex,
                "[", "]")
        arrayArgument = ctx.children[ctx.childCount - 2] as Java8Parser.ExpressionContext
        ctx.children.remove(arrayArgument)
        visitChildren(ctx)
        arrayBrackets = null
    }

    override fun visitMethodInvocation(ctx: Java8Parser.MethodInvocationContext) {
        val dotPos = if (ctx.DOT().isNotEmpty()) ctx.DOT().last().symbol.startIndex else null
        visitFunctionCall(ctx.Identifier() ?: ctx.methodName().Identifier(), ctx,
                dotPos, ".",
                BracketsNode(ctx.LPAREN().symbol.startIndex, ctx.RPAREN().symbol.startIndex, "(", ")")
        )
    }

    override fun visitMethodInvocation_lf_primary(ctx: Java8Parser.MethodInvocation_lf_primaryContext) {
        visitFunctionCall(ctx.Identifier(), ctx,
                ctx.DOT()?.symbol?.startIndex, ".",
                BracketsNode(ctx.LPAREN().symbol.startIndex, ctx.RPAREN().symbol.startIndex, "(", ")")
        )
    }

    override fun visitMethodInvocation_lfno_primary(ctx: Java8Parser.MethodInvocation_lfno_primaryContext) {
        val dotPos = if (ctx.DOT().isNotEmpty()) ctx.DOT().last().symbol.startIndex else null
        visitFunctionCall(ctx.Identifier() ?: ctx.methodName().Identifier(), ctx,
                dotPos, ".",
                BracketsNode(ctx.LPAREN().symbol.startIndex, ctx.RPAREN().symbol.startIndex, "(", ")")
        )
    }

    override fun visitClassInstanceCreationExpression(ctx: Java8Parser.ClassInstanceCreationExpressionContext) {
        visitFunctionCall(ctx.Identifier(0), ctx,
                ctx.NEW().symbol.startIndex, "new",
                BracketsNode(ctx.LPAREN().symbol.startIndex, ctx.RPAREN().symbol.startIndex, "(", ")")
        )
    }

    override fun visitClassInstanceCreationExpression_lf_primary(ctx: Java8Parser.ClassInstanceCreationExpression_lf_primaryContext) {
        visitFunctionCall(ctx.Identifier(), ctx,
                ctx.NEW().symbol.startIndex, "new",
                BracketsNode(ctx.LPAREN().symbol.startIndex, ctx.RPAREN().symbol.startIndex, "(", ")")
        )
    }

    override fun visitClassInstanceCreationExpression_lfno_primary(ctx: Java8Parser.ClassInstanceCreationExpression_lfno_primaryContext) {
        visitFunctionCall(ctx.Identifier(0), ctx,
                ctx.NEW().symbol.startIndex, "new",
                BracketsNode(ctx.LPAREN().symbol.startIndex, ctx.RPAREN().symbol.startIndex, "(", ")")
        )
    }

    override fun visitArgumentList(ctx: Java8Parser.ArgumentListContext) {
        visitChildren(ctx)
        ctx.COMMA()?.let { (currentNode as FunctionCallNode).addArgument(CommaNode(it.symbol.startIndex)) }
    }

    private fun visitVariableUsage(identifier: TerminalNode, ctx: ParserRuleContext, dotPos: Int?) {
        val start = identifier.symbol.startIndex
        val name = identifier.text
        val parentNode = currentNode
        currentNode = VariableUsageNode(
                arrayBrackets, dotPos?.let { DotOrNewNode(".", it) }, name, start
        )
        arrayBrackets = null
        val argument = arrayArgument
        arrayArgument = null
        argument?.let { visit(it) }
        when (parentNode) {
            is ClassNode -> parentNode.initVariableUsages.add(currentNode as VariableUsageNode)
            is FunctionNode -> parentNode.variableUsages.add(currentNode as VariableUsageNode)
            is CallWithBracketsNode -> parentNode.addArgument(currentNode as VariableUsageNode)
            else -> throw UnifiedAstException()
        }
        currentNode = parentNode
    }

    private fun visitFunctionCall(identifier: TerminalNode, ctx: ParserRuleContext, dotOrNewPos: Int?, dotOrNewText: String?, bracketsNode: BracketsNode) {
        val start = identifier.symbol.startIndex
        val name = identifier.text
        val parentNode = currentNode
        currentNode = FunctionCallNode(
                bracketsNode, dotOrNewPos?.let { DotOrNewNode(dotOrNewText!!, it) }, name, start
        )
        ctx.children.forEach { visit(it) }
        when (parentNode) {
            is ClassNode -> parentNode.initFunctionCalls.add(currentNode as FunctionCallNode)
            is FunctionNode -> parentNode.functionCalls.add(currentNode as FunctionCallNode)
            is CallWithBracketsNode -> parentNode.addArgument(currentNode as FunctionCallNode)
            else -> throw UnifiedAstException()
        }
        currentNode = parentNode
    }
}
