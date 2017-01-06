package com.github.klpx.envmixer

import com.github.klpx.envmixer.ui.EnvConfigTree
import com.intellij.execution.util.EnvVariablesTable
import com.intellij.execution.util.EnvironmentVariable
import com.intellij.openapi.diagnostic.Logger
import java.awt.Component
import java.awt.event.MouseEvent
import java.util.*
import javax.swing.*
import javax.swing.event.TreeModelEvent
import javax.swing.event.TreeModelListener
import javax.swing.tree.*

/**
 * Created by hsslbch on 1/6/17.
 */
class KConfigurationForm(private val innerForm: ConfigurationForm) {

    private var state = StateService.Settings()

    private var logger = Logger.getInstance(KConfigurationForm::class.java)

    companion object {
        val rootNodeObject = "All mixin groups"
    }

    private object EnvCellEditor : DefaultCellEditor(JTextField()) {
    }

    private object EnvTreeCellRenderer : DefaultTreeCellRenderer() {
        override fun getTreeCellRendererComponent(
                tree: JTree?,
                value: Any?,
                sel: Boolean,
                expanded: Boolean,
                leaf: Boolean,
                row: Int,
                hasFocus: Boolean
        ): Component {
            val resultComp = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus)
            val innerValue = (value as DefaultMutableTreeNode).userObject
            text = when(innerValue) {
                is StateService.EnvMixinGroup -> innerValue.name
                is StateService.StaticVarsEnvMixin -> innerValue.name
                rootNodeObject -> rootNodeObject
                else -> "???"
            }
            return resultComp
        }
    }

    private val EnvTreeCellEditor = object : DefaultTreeCellEditor(innerForm.tree1, EnvTreeCellRenderer, EnvCellEditor) {

        override fun getCellEditorValue(): Any {
            val newName = super.getCellEditorValue() as String
            val innerValue = (lastPath.lastPathComponent as DefaultMutableTreeNode).userObject
            return when(innerValue) {
                is StateService.EnvMixinGroup -> innerValue.copy(name = newName)
                is StateService.StaticVarsEnvMixin -> innerValue.copy(name = newName)
                else -> newName
            }
        }

        override fun isCellEditable(event: EventObject?): Boolean {
            val source = event?.source
            if (event is MouseEvent && source is EnvConfigTree) {
                val path: TreePath? = source.getPathForLocation(event.x, event.y)
                val value = path?.lastPathComponent
                if (value is DefaultMutableTreeNode && value.userObject == rootNodeObject) {
                    return false
                } else if (value == rootNodeObject) {
                    return false
                }
            }
            return super.isCellEditable(event)
        }

        override fun canEditImmediately(event: EventObject?): Boolean {
            if (event is MouseEvent && SwingUtilities.isLeftMouseButton(event)) {
                return event.clickCount > 1 && inHitRegion(event.x, event.y)
            }
            return event == null
        }
    }

    private enum class DetailPane {
        GLOBAL, GROUP, EDITOR
    }

    private fun toggleVisiblePane(pane: DetailPane) {
        innerForm.envEditor.isVisible = pane == DetailPane.EDITOR
        innerForm.groupHelpPane.isVisible = pane == DetailPane.GROUP
        innerForm.globalTextPane.isVisible = pane == DetailPane.GLOBAL
    }


    init {
        updateView()
        //        newMixinGroupButton.addActionListener(event -> {
        //            sets = sets.copy(sets.getMixinGroups())
        //        });

        innerForm.tree1.isEditable = true
        innerForm.tree1.cellEditor = EnvTreeCellEditor

        innerForm.tree1.cellRenderer = EnvTreeCellRenderer
        innerForm.tree1.selectionModel.selectionMode = TreeSelectionModel.SINGLE_TREE_SELECTION

        toggleVisiblePane(DetailPane.GLOBAL)

        innerForm.tree1.addTreeSelectionListener { event ->
            val selectedNode = event.path.lastPathComponent as DefaultMutableTreeNode
            val selectedItem = selectedNode.userObject
            when (selectedItem) {
                is StateService.StaticVarsEnvMixin -> {
                    logger.warn("Show editor")
                    innerForm.envEditor.envTable.setValues(
                        selectedItem.vars.toList().map { x ->
                            EnvironmentVariable(x.first, x.second, false)
                        }
                    )
                    innerForm.envEditor.envTable.addObserver(object : Observer {
                        override fun update(o: Observable?, arg: Any?) {
                            val envTable = o as EnvVariablesTable
                            selectedItem.vars = envTable.environmentVariables.map { ev -> ev.name to ev.value }.toMap()
                        }
                    })

                    toggleVisiblePane(DetailPane.EDITOR)
                }
                is StateService.EnvMixinGroup ->
                    toggleVisiblePane(DetailPane.GROUP)
                else ->
                    toggleVisiblePane(DetailPane.GLOBAL)
            }
        }
    }

    fun getRootPanel(): JComponent? {
        return innerForm.panel1
    }

    fun setSettings(settings: StateService.Settings) {
        state = settings.copy()
        updateView()
    }

    fun getSettings(): StateService.Settings {
        return state
    }

    private fun updateView() {
        val rootnode = DefaultMutableTreeNode()
        rootnode.allowsChildren = true
        rootnode.userObject = rootNodeObject

        val treeModel = DefaultTreeModel(rootnode)
        innerForm.tree1.model = treeModel

        state.mixinGroups.forEach { mixinGroup ->
            val mixinGroupNode = DefaultMutableTreeNode()
            mixinGroupNode.allowsChildren = true
            mixinGroupNode.userObject = mixinGroup

            mixinGroup.mixins.forEach { mixin ->
                val mixinNode = DefaultMutableTreeNode()
                mixinNode.allowsChildren = false
                mixinNode.userObject = mixin

                mixinGroupNode.add(mixinNode)
            }

            rootnode.add(mixinGroupNode)
        }
    }
}
