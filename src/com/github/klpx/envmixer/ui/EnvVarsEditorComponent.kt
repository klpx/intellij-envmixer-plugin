package com.github.klpx.envmixer.ui

import com.intellij.execution.util.EnvVariablesTable
import java.awt.BorderLayout
import javax.swing.JPanel

/**
 * Created by hsslbch on 1/6/17.
 */
class EnvVarsEditorComponent : JPanel(BorderLayout()) {
    val envTable = EnvVariablesTable()

    init {
        add(envTable.component)
    }
}
