package org.jb.cce.uast

import org.jb.cce.uast.statements.*
import org.jb.cce.uast.statements.declarations.*
import org.jb.cce.uast.statements.declarations.blocks.*
import org.jb.cce.uast.statements.expressions.*
import org.jb.cce.uast.statements.expressions.references.*

interface UnifiedAstVisitor {

    fun visitChildren(node: UnifiedAstNode) = node.getChildren().forEach { visit(it) }

    fun visit(node: UnifiedAstNode) {
        when (node) {
            is FileNode -> visitFileNode(node)
            is StatementNode -> visitStatementNode(node)
            is CompletableNode -> visitCompletableNode(node)
        }
    }

    fun visitStatementNode(node: StatementNode) {
        when (node) {
            is AssignmentNode -> visitAssignmentNode(node)
            is ExpressionNode -> visitExpressionNode(node)
            is DeclarationNode -> visitDeclarationNode(node)
        }
    }

    fun visitDeclarationNode(node: DeclarationNode) {
        when (node) {
            is ArrayDeclarationNode -> visitArrayDeclarationNode(node)
            is ClassDeclarationNode -> visitClassDeclarationNode(node)
            is MethodDeclarationNode -> visitMethodDeclarationNode(node)
            is MethodHeaderNode -> visitMethodHeaderNode(node)
            is VariableDeclarationNode -> visitVariableDeclarationNode(node)
            is BlockNode -> visitBlockNode(node)
        }
    }

    fun visitBlockNode(node: BlockNode) {
        when (node) {
            is ClassInitializerNode -> visitClassInitializerNode(node)
            is GlobalNode -> visitGlobalNode(node)
            is MethodBodyNode -> visitMethodBodyNode(node)
            is NamedBlockNode -> visitNamedBlockNode(node)
        }
    }

    fun visitExpressionNode(node: ExpressionNode) {
        when (node) {
            is ReferenceNode -> visitReferenceNode(node)
        }
    }

    fun visitReferenceNode(node: ReferenceNode) {
        when (node) {
            is ArrayAccessNode -> visitArrayAccessNode(node)
            is MethodCallNode -> visitMethodCallNode(node)
            is VariableAccessNode -> visitVariableAccessNode(node)
        }
    }

    fun visitClassInitializerNode(node: ClassInitializerNode) = visit(node)
    fun visitGlobalNode(node: GlobalNode) = visit(node)
    fun visitMethodBodyNode(node: MethodBodyNode) = visit(node)
    fun visitNamedBlockNode(node: NamedBlockNode) = visit(node)

    fun visitArrayDeclarationNode(node: ArrayDeclarationNode) = visitChildren(node)
    fun visitClassDeclarationNode(node: ClassDeclarationNode) = visitChildren(node)
    fun visitMethodDeclarationNode(node: MethodDeclarationNode) = visitChildren(node)
    fun visitMethodHeaderNode(node: MethodHeaderNode) = visitChildren(node)
    fun visitVariableDeclarationNode(node: VariableDeclarationNode) = visitChildren(node)

    fun visitArrayAccessNode(node: ArrayAccessNode) = visitChildren(node)
    fun visitMethodCallNode(node: MethodCallNode) = visitChildren(node)
    fun visitVariableAccessNode(node: VariableAccessNode) = visitChildren(node)

    fun visitAssignmentNode(node: AssignmentNode) = visitChildren(node)

    fun visitFileNode(node: FileNode) = visitChildren(node)
    fun visitCompletableNode(node: CompletableNode) = visitChildren(node)
}
