package org.jb.cce

import org.antlr.v4.runtime.ParserRuleContext
import org.jb.cce.uast.*
import org.jb.cce.uast.statements.declarations.*
import org.jb.cce.uast.statements.declarations.blocks.ClassInitializerNode
import org.jb.cce.uast.statements.declarations.blocks.MethodBodyNode
import org.jb.cce.uast.statements.expressions.references.FunctionCallNode
import org.jb.cce.uast.statements.expressions.references.VariableAccessNode

class JavaVisitor : Java8BaseVisitor<Unit>() {

    private lateinit var currentNode: UnifiedAstNode

    fun buildUnifiedAst(parser: Java8Parser): FileNode {
        visit(parser.compilationUnit())
        return currentNode as FileNode
    }

    override fun visitCompilationUnit(ctx: Java8Parser.CompilationUnitContext) {
        currentNode = FileNode(getOffset(ctx), getLength(ctx))
        visitChildren(ctx)
    }

    override fun visitNormalClassDeclaration(ctx: Java8Parser.NormalClassDeclarationContext) {
        val parentNode = currentNode
        currentNode = ClassDeclarationNode(ctx.Identifier().text, getOffset(ctx), getLength(ctx))
        visitChildren(ctx)
        when (parentNode) {
            is FileNode -> parentNode.addDeclaration(currentNode as ClassDeclarationNode)
            is ClassDeclarationNode -> parentNode.addMember(currentNode as ClassDeclarationNode)
            is BlockNode -> parentNode.addStatement(currentNode as ClassDeclarationNode)
        }
        currentNode = parentNode
    }

    override fun visitNormalInterfaceDeclaration(ctx: Java8Parser.NormalInterfaceDeclarationContext) {
        val parentNode = currentNode
        currentNode = ClassDeclarationNode(ctx.Identifier().text, getOffset(ctx), getLength(ctx))
        visitChildren(ctx)
        when (parentNode) {
            is FileNode -> parentNode.addDeclaration(currentNode as ClassDeclarationNode)
            is ClassDeclarationNode -> parentNode.addMember(currentNode as ClassDeclarationNode)
            is BlockNode -> parentNode.addStatement(currentNode as ClassDeclarationNode)
        }
        currentNode = parentNode
    }

    override fun visitInstanceInitializer(ctx: Java8Parser.InstanceInitializerContext) {
        val parentNode = currentNode as ClassDeclarationNode
        currentNode = ClassInitializerNode(getOffset(ctx), getLength(ctx))
        visitChildren(ctx)
        parentNode.addMember(currentNode as ClassInitializerNode)
        currentNode = parentNode
    }

    override fun visitStaticInitializer(ctx: Java8Parser.StaticInitializerContext) {
        val parentNode = currentNode as ClassDeclarationNode
        currentNode = ClassInitializerNode(getOffset(ctx), getLength(ctx))
        visitChildren(ctx)
        parentNode.addMember(currentNode as ClassInitializerNode)
        currentNode = parentNode
    }

    override fun visitVariableDeclarator(ctx: Java8Parser.VariableDeclaratorContext) {
        val parentNode = currentNode
        currentNode = if (ctx.variableDeclaratorId().dims() != null) {
            ArrayDeclarationNode(ctx.variableDeclaratorId().Identifier().text, getOffset(ctx), getLength(ctx))
        } else {
            VariableDeclarationNode(ctx.variableDeclaratorId().Identifier().text, getOffset(ctx), getLength(ctx))
        }
        visitChildren(ctx)
        when (parentNode) {
            is ClassDeclarationNode -> parentNode.addMember(currentNode as VariableDeclarationNode)
            is BlockNode -> parentNode.addStatement(currentNode as VariableDeclarationNode)
        }
        currentNode = parentNode
    }

    override fun visitMethodDeclaration(ctx: Java8Parser.MethodDeclarationContext) {
        val parentNode = currentNode as ClassDeclarationNode
        currentNode = MethodDeclarationNode(getOffset(ctx), getLength(ctx))
        visitChildren(ctx)
        parentNode.addMember(currentNode as MethodDeclarationNode)
        currentNode = parentNode
    }

    override fun visitConstructorDeclaration(ctx: Java8Parser.ConstructorDeclarationContext) {
        val parentNode = currentNode as ClassDeclarationNode
        currentNode = MethodDeclarationNode(getOffset(ctx), getLength(ctx))
        visitChildren(ctx)
        parentNode.addMember(currentNode as ClassInitializerNode)
        currentNode = parentNode
    }

    override fun visitInterfaceMethodDeclaration(ctx: Java8Parser.InterfaceMethodDeclarationContext) {
        val parentNode = currentNode as ClassDeclarationNode
        currentNode = MethodDeclarationNode(getOffset(ctx), getLength(ctx))
        visitChildren(ctx)
        parentNode.addMember(currentNode as ClassInitializerNode)
        currentNode = parentNode
    }

    override fun visitMethodDeclarator(ctx: Java8Parser.MethodDeclaratorContext) {
        val parentNode = currentNode as MethodDeclarationNode
        currentNode = MethodHeaderNode(ctx.Identifier().text, getOffset(ctx), getLength(ctx))
        visitChildren(ctx)
        parentNode.setHeader(currentNode as MethodHeaderNode)
        currentNode = parentNode
    }

    override fun visitConstructorDeclarator(ctx: Java8Parser.ConstructorDeclaratorContext) {
        val parentNode = currentNode as MethodDeclarationNode
        currentNode = MethodHeaderNode(ctx.simpleTypeName().Identifier().text, getOffset(ctx), getLength(ctx))
        visitChildren(ctx)
        parentNode.setHeader(currentNode as MethodHeaderNode)
        currentNode = parentNode
    }

    override fun visitFormalParameter(ctx: Java8Parser.FormalParameterContext) {
        val childNode = VariableDeclarationNode(ctx.variableDeclaratorId().Identifier().text, getOffset(ctx), getLength(ctx))
        (currentNode as MethodHeaderNode).addArgument(childNode)
    }

    override fun visitLastFormalParameter(ctx: Java8Parser.LastFormalParameterContext) {
        if (ctx.formalParameter() != null) {
            visit(ctx.formalParameter())
            return
        }
        val childNode = VariableDeclarationNode(ctx.variableDeclaratorId().Identifier().text, getOffset(ctx), getLength(ctx))
        (currentNode as MethodHeaderNode).addArgument(childNode)
    }

    override fun visitMethodBody(ctx: Java8Parser.MethodBodyContext) {
        val parentNode = currentNode as MethodDeclarationNode
        currentNode = MethodBodyNode(getOffset(ctx), getLength(ctx))
        visitChildren(ctx)
        parentNode.setBody(currentNode as MethodBodyNode)
        currentNode = parentNode
    }

    override fun visitConstructorBody(ctx: Java8Parser.ConstructorBodyContext) {
        val parentNode = currentNode as MethodDeclarationNode
        currentNode = MethodBodyNode(getOffset(ctx), getLength(ctx))
        visitChildren(ctx)
        parentNode.setBody(currentNode as MethodBodyNode)
        currentNode = parentNode
    }

    override fun visitExpressionName(ctx: Java8Parser.ExpressionNameContext) {
        val childNode = VariableAccessNode(ctx.Identifier().text, getOffset(ctx), getLength(ctx))
        (currentNode as BlockNode).addStatement(childNode)
    }

    override fun visitFieldAccess(ctx: Java8Parser.FieldAccessContext) {
        val childNode = VariableAccessNode(ctx.Identifier().text, getOffset(ctx), getLength(ctx))
        (currentNode as BlockNode).addStatement(childNode)
    }

    override fun visitMethodInvocation(ctx: Java8Parser.MethodInvocationContext) {
        ctx.expressionName()?.let { visit(it) }
        ctx.primary()?.let { visit(it) }
        val parentNode = currentNode
        currentNode = FunctionCallNode(ctx.Identifier().text, getOffset(ctx), getLength(ctx))
        ctx.argumentList()?.let { visit(it) }
        when (parentNode) {
            is BlockNode -> parentNode.addStatement(currentNode as FunctionCallNode)
            is FunctionCallNode -> parentNode.addArgument(currentNode as FunctionCallNode)
        }
        currentNode = parentNode
    }

    override fun visitMethodInvocation_lf_primary(ctx: Java8Parser.MethodInvocation_lf_primaryContext) {
        val parentNode = currentNode
        currentNode = FunctionCallNode(ctx.Identifier().text, getOffset(ctx), getLength(ctx))
        ctx.argumentList()?.let { visit(it) }
        when (parentNode) {
            is BlockNode -> parentNode.addStatement(currentNode as FunctionCallNode)
            is FunctionCallNode -> parentNode.addArgument(currentNode as FunctionCallNode)
        }
        currentNode = parentNode
    }

    override fun visitMethodInvocation_lfno_primary(ctx: Java8Parser.MethodInvocation_lfno_primaryContext) {
        ctx.expressionName()?.let { visit(it) }
        val parentNode = currentNode
        currentNode = FunctionCallNode(ctx.Identifier().text, getOffset(ctx), getLength(ctx))
        ctx.argumentList()?.let { visit(it) }
        when (parentNode) {
            is BlockNode -> parentNode.addStatement(currentNode as FunctionCallNode)
            is FunctionCallNode -> parentNode.addArgument(currentNode as FunctionCallNode)
        }
        currentNode = parentNode
    }

    private fun getOffset(ctx: ParserRuleContext) = ctx.start.startIndex

    private fun getLength(ctx: ParserRuleContext) = ctx.stop.stopIndex - ctx.start.startIndex + 1
}
