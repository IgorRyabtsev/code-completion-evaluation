package org.jb.cce

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
    val rvalues = tree.getAllRValues().filter { !it.isArgument }
    Collections.sort(rvalues, Comparator.comparingInt { it.offset })
    rvalues.asReversed().forEach {
        list.add(DeleteRange(it.offset, it.offset + it.text.length))
    }
    rvalues.forEach { rvalue ->
        list.add(MoveCaret(rvalue.offset))
        list.add(CallCompletion())
        var prevStart = 0
        if (rvalue is FunctionCallNode) {
            val arguments = rvalue.getAllRValues()
            Collections.sort(arguments, kotlin.Comparator.comparingInt { it.offset })
            arguments.forEach {
                val nextStart = it.offset - rvalue.offset
                list.add(PrintText(rvalue.text.substring(prevStart, nextStart)))
                list.add(CallCompletion())
                prevStart = nextStart
            }
        }
        list.add(CancelSession())
        list.add(PrintText(rvalue.text.substring(prevStart)))
    }
    return list
}
