package com.github.klpx.envmixer.ui

import com.github.klpx.envmixer.StateService
import javax.swing.JTree
import javax.swing.plaf.basic.BasicTreeUI
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.TreePath

/**
 * Created by hsslbch on 1/6/17.
 */
class EnvConfigTree : JTree() {


    override fun convertValueToText(
            value: Any?,
            selected: Boolean,
            expanded: Boolean,
            leaf: Boolean,
            row: Int,
            hasFocus: Boolean
        ): String {

        return when(value) {
            is DefaultMutableTreeNode -> convertValueToText(value.userObject, selected, expanded, leaf, row, hasFocus)
            is StateService.EnvMixinGroup -> value.name
            is StateService.StaticVarsEnvMixin -> value.name
            else -> super.convertValueToText(value, selected, expanded, leaf, row, hasFocus)
        }
    }
}
