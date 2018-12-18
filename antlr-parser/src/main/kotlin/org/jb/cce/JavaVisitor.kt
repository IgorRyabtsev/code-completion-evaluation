package org.jb.cce

import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.TerminalNode
import org.jb.cce.uast.*
import org.jb.cce.uast.statements.AssignmentNode
import org.jb.cce.uast.statements.declarations.*
import org.jb.cce.uast.statements.declarations.blocks.BlockNode
import org.jb.cce.uast.statements.declarations.blocks.ClassInitializerNode
import org.jb.cce.uast.statements.declarations.blocks.MethodBodyNode
import org.jb.cce.uast.statements.expressions.references.MethodCallNode
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

    override fun visitEnumDeclaration(ctx: Java8Parser.EnumDeclarationContext) {
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
        parentNode.addMember(currentNode as MethodDeclarationNode)
        currentNode = parentNode
    }

    override fun visitInterfaceMethodDeclaration(ctx: Java8Parser.InterfaceMethodDeclarationContext) {
        val parentNode = currentNode as ClassDeclarationNode
        currentNode = MethodDeclarationNode(getOffset(ctx), getLength(ctx))
        visitChildren(ctx)
        parentNode.addMember(currentNode as MethodDeclarationNode)
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
        val parentNode = currentNode as MethodHeaderNode
        val childNode = VariableDeclarationNode(ctx.variableDeclaratorId().Identifier().text, getOffset(ctx), getLength(ctx))
        parentNode.addArgument(childNode)
    }

    override fun visitLastFormalParameter(ctx: Java8Parser.LastFormalParameterContext) {
        if (ctx.formalParameter() != null) {
            visit(ctx.formalParameter())
            return
        }
        val parentNode = currentNode as MethodHeaderNode
        val childNode = VariableDeclarationNode(ctx.variableDeclaratorId().Identifier().text, getOffset(ctx), getLength(ctx))
        parentNode.addArgument(childNode)
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
        val name = CompletableNode(ctx.Identifier().text, getOffset(ctx.Identifier()), getLength(ctx.Identifier()))
        val parentNode = currentNode
        val childNode = VariableAccessNode(name, getOffset(ctx), getLength(ctx))
        when (parentNode) {
            is BlockNode -> parentNode.addStatement(childNode)
            is AssignmentNode -> parentNode.setAssigned(childNode)
            is MethodCallNode -> parentNode.addArgument(childNode)
        }
    }

    override fun visitFieldAccess(ctx: Java8Parser.FieldAccessContext) {
        val name = CompletableNode(ctx.Identifier().text, getOffset(ctx.Identifier()), getLength(ctx.Identifier()))
        val parentNode = currentNode
        val childNode = VariableAccessNode(name, getOffset(ctx), getLength(ctx))
        when (parentNode) {
            is BlockNode -> parentNode.addStatement(childNode)
            is AssignmentNode -> parentNode.setAssigned(childNode)
            is MethodCallNode -> parentNode.addArgument(childNode)
        }
    }

    override fun visitMethodInvocation(ctx: Java8Parser.MethodInvocationContext) {
        ctx.expressionName()?.let { visit(it) }
        ctx.primary()?.let { visit(it) }
        val parentNode = currentNode
        val name = CompletableNode(ctx.Identifier().text, getOffset(ctx.Identifier()), getLength(ctx.Identifier()))
        currentNode = MethodCallNode(name, getOffset(ctx), getLength(ctx))
        ctx.argumentList()?.let { visit(it) }
        when (parentNode) {
            is BlockNode -> parentNode.addStatement(currentNode as MethodCallNode)
            is MethodCallNode -> parentNode.addArgument(currentNode as MethodCallNode)
        }
        currentNode = parentNode
    }

    override fun visitMethodInvocation_lf_primary(ctx: Java8Parser.MethodInvocation_lf_primaryContext) {
        val parentNode = currentNode
        val name = CompletableNode(ctx.Identifier().text, getOffset(ctx.Identifier()), getLength(ctx.Identifier()))
        currentNode = MethodCallNode(name, getOffset(ctx), getLength(ctx))
        ctx.argumentList()?.let { visit(it) }
        when (parentNode) {
            is BlockNode -> parentNode.addStatement(currentNode as MethodCallNode)
            is MethodCallNode -> parentNode.addArgument(currentNode as MethodCallNode)
        }
        currentNode = parentNode
    }

    override fun visitMethodInvocation_lfno_primary(ctx: Java8Parser.MethodInvocation_lfno_primaryContext) {
        ctx.expressionName()?.let { visit(it) }
        val parentNode = currentNode
        val name = CompletableNode(ctx.Identifier().text, getOffset(ctx.Identifier()), getLength(ctx.Identifier()))
        currentNode = MethodCallNode(name, getOffset(ctx), getLength(ctx))
        ctx.argumentList()?.let { visit(it) }
        when (parentNode) {
            is BlockNode -> parentNode.addStatement(currentNode as MethodCallNode)
            is MethodCallNode -> parentNode.addArgument(currentNode as MethodCallNode)
        }
        currentNode = parentNode
    }

    override fun visitClassInstanceCreationExpression(ctx: Java8Parser.ClassInstanceCreationExpressionContext) {
        ctx.expressionName()?.let { visit(it) }
        ctx.primary()?.let { visit(it) }
        val parentNode = currentNode
        val name = CompletableNode(ctx.Identifier().last().text, getOffset(ctx.Identifier().last()), getLength(ctx.Identifier().last()))
        currentNode = MethodCallNode(name, getOffset(ctx), getLength(ctx))
        ctx.argumentList()?.let { visit(it) }
        when (parentNode) {
            is BlockNode -> parentNode.addStatement(currentNode as MethodCallNode)
            is MethodCallNode -> parentNode.addArgument(currentNode as MethodCallNode)
        }
        currentNode = parentNode
    }

    override fun visitClassInstanceCreationExpression_lf_primary(ctx: Java8Parser.ClassInstanceCreationExpression_lf_primaryContext) {
        val parentNode = currentNode
        val name = CompletableNode(ctx.Identifier().text, getOffset(ctx.Identifier()), getLength(ctx.Identifier()))
        currentNode = MethodCallNode(name, getOffset(ctx), getLength(ctx))
        ctx.argumentList()?.let { visit(it) }
        when (parentNode) {
            is BlockNode -> parentNode.addStatement(currentNode as MethodCallNode)
            is MethodCallNode -> parentNode.addArgument(currentNode as MethodCallNode)
        }
        currentNode = parentNode
    }

    override fun visitClassInstanceCreationExpression_lfno_primary(ctx: Java8Parser.ClassInstanceCreationExpression_lfno_primaryContext) {
        ctx.expressionName()?.let { visit(it) }
        val parentNode = currentNode
        val name = CompletableNode(ctx.Identifier().last().text, getOffset(ctx.Identifier().last()), getLength(ctx.Identifier().last()))
        currentNode = MethodCallNode(name, getOffset(ctx), getLength(ctx))
        ctx.argumentList()?.let { visit(it) }
        when (parentNode) {
            is BlockNode -> parentNode.addStatement(currentNode as MethodCallNode)
            is MethodCallNode -> parentNode.addArgument(currentNode as MethodCallNode)
        }
        currentNode = parentNode
    }

    private fun getOffset(ctx: ParserRuleContext) = ctx.start.startIndex
    private fun getOffset(ctx: TerminalNode) = ctx.symbol.startIndex

    private fun getLength(ctx: ParserRuleContext) = ctx.stop.stopIndex - ctx.start.startIndex + 1
    private fun getLength(ctx: TerminalNode) = ctx.symbol.stopIndex - ctx.symbol.startIndex + 1
}
