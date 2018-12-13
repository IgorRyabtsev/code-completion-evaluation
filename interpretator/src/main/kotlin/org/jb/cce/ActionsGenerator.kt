package org.jb.cce

import org.jb.cce.uast.*
import java.util.*

fun UnifiedAstNode.getAllRValues(list: MutableList<RValueNode> = mutableListOf()): List<RValueNode> {
    when (this) {
        is FileNode -> {
            list += globalFunctionCalls + globalVariableUsages
            (classes + globalFunctionCalls + globalVariableUsages + functions).forEach {
                it.getAllRValues(list)
            }
        }
        is ClassNode -> {
            list += initFunctionCalls + initVariableUsages
            (subclasses + initFunctionCalls + initVariableUsages + methods).forEach {
                it.getAllRValues(list)
            }
        }
        is FunctionNode -> {
            list += variableUsages + functionCalls
            (variableUsages + functionCalls).forEach {
                it.getAllRValues(list)
            }
        }
        is CallWithBracketsNode -> {
            list += getArguments()
            getArguments().forEach {
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
        if (it is CallWithBracketsNode) {
            it.brackets?.let { br ->
                list.add(DeleteRange(br.rightPos - it.argumentsLength,
                        br.rightPos - it.argumentsLength + br.rightText.length))
                list.add(DeleteRange(br.leftPos, br.leftPos + br.leftText.length))
            }
        }
        list.add(DeleteRange(it.offset, it.offset + it.name.length))
        it.dotOrNew?.let { don -> list.add(DeleteRange(don.offset, don.offset + don.text.length)) }
    }
    rvalues.forEach {
        it.dotOrNew?.let { don ->
            list.add(MoveCaret(don.offset))
            list.add(PrintText(don.text))
        }
        list.add(MoveCaret(it.offset))
        if (it !is CommaNode) {
            list.add(CallCompletion())
            list.add(CancelSession())
        }
        list.add(PrintText(it.name))
        if (it is CallWithBracketsNode) {
            it.brackets?.let { br ->
                list.add(MoveCaret(br.leftPos))
                list.add(PrintText(br.leftText))
                list.add(MoveCaret(br.rightPos - it.argumentsLength))
                list.add(PrintText(br.rightText))
            }
        }
    }
    list.add(CallCompletion())
    list.add(CancelSession())
    return list
}
