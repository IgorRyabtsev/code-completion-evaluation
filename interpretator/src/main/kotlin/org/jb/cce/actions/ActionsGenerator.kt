package org.jb.cce.actions

import org.jb.cce.uast.*
import java.util.*

fun UnifiedAstNode.getAllRValues(list: MutableList<RValueNode> = mutableListOf()): List<RValueNode> {
    when (this) {
        is FileNode -> {
            list += globalFunctionCalls + globalVariableUsages
            (classes + globalFunctionCalls + functions).forEach {
                it.getAllRValues(list)
            }
        }
        is ClassNode -> {
            list += initFunctionCalls + initVariableUsages
            (subclasses + initFunctionCalls + methods).forEach {
                it.getAllRValues(list)
            }
        }
        is FunctionNode -> {
            list += variableUsages + functionCalls
            functionCalls.forEach {
                it.getAllRValues(list)
            }
        }
        is FunctionCallNode -> {
            list += arguments
            arguments.forEach {
                it.getAllRValues(list)
            }
        }
    }
    return list
}

fun generateActions(tree: FileNode): List<Action> {
    val list = mutableListOf<Action>(OpenFile(tree.path))
    val rvalues = tree.getAllRValues()
    Collections.sort(rvalues, Comparator.comparingInt { it.offset })
    rvalues.asReversed().forEach {
        list.add(DeleteRange(it.offset, it.offset + it.text.length))
    }
    rvalues.forEach { rvalue ->
        list.add(MoveCaret(rvalue.offset))
        list.add(CallCompletion())
        list.add(CancelSession())
        list.add(PrintText(rvalue.text))
    }
    list.add(CallCompletion())
    list.add(CancelSession())
    return list
}
