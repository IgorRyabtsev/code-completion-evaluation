package org.jb.cce.actions

import org.jb.cce.uast.*
import org.jb.cce.uast.statements.declarations.blocks.BlockNode
import org.jb.cce.uast.statements.declarations.blocks.ClassInitializerNode
import org.jb.cce.uast.statements.declarations.blocks.GlobalNode
import org.jb.cce.uast.statements.declarations.blocks.MethodBodyNode

class DeleteMethodBodiesVisitor : UnifiedAstVisitor {

    private val actions = mutableListOf<DeleteRange>()

    fun getActions(): List<DeleteRange> = actions

    override fun visitGlobalNode(node: GlobalNode) {
        actions += DeleteRange(node.getOffset(), node.getOffset() + node.getLength())
    }

    override fun visitClassInitializerNode(node: ClassInitializerNode) {
        actions += DeleteRange(node.getOffset(), node.getOffset() + node.getLength())
    }

    override fun visitMethodBodyNode(node: MethodBodyNode) {
        actions += DeleteRange(node.getOffset(), node.getOffset() + node.getLength())
    }
}

class CallCompletionsVisitor(private val text: String) : UnifiedAstVisitor {

    private val actions = mutableListOf<Action>()
    private var isInsideDeletedText = false
    private var previousTextStart = 0

    fun getActions(): List<Action> = actions

    override fun visitClassInitializerNode(node: ClassInitializerNode) = visitDeletableBlock(node)

    override fun visitGlobalNode(node: GlobalNode) = visitDeletableBlock(node)

    override fun visitMethodBodyNode(node: MethodBodyNode) = visitDeletableBlock(node)

    private fun visitDeletableBlock(node: BlockNode) {
        if (isInsideDeletedText) return
        isInsideDeletedText = true
        previousTextStart = node.getOffset()
        visitChildren(node)
        isInsideDeletedText = false
        if (previousTextStart < node.getOffset() + node.getLength()) {
            actions += MoveCaret(previousTextStart)
            actions += PrintText(text.substring(IntRange(previousTextStart, node.getOffset() + node.getLength() - 1)))
        }
    }

    override fun visitCompletableNode(node: CompletableNode) {
        if (previousTextStart < node.getOffset()) {
            actions += MoveCaret(previousTextStart)
            actions += PrintText(text.substring(IntRange(previousTextStart, node.getOffset() - 1)))
            previousTextStart = node.getOffset() + node.getLength()
        }
        actions += CallCompletion(node.getText())
        actions += CancelSession()
        actions += PrintText(node.getText())
    }
}


fun generateActions(fileText: String, tree: FileNode): List<Action> {

    val deletionVisitor = DeleteMethodBodiesVisitor()
    deletionVisitor.visit(tree)
    val completionVisitor = CallCompletionsVisitor(fileText)
    completionVisitor.visit(tree)

    return deletionVisitor.getActions().reversed() + completionVisitor.getActions()
}
