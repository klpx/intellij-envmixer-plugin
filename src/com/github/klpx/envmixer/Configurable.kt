package com.github.klpx.envmixer

import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ui.configuration.ModulesConfigurator
import com.intellij.openapi.roots.ui.configuration.ProjectConfigurable
import com.intellij.openapi.roots.ui.configuration.projectRoot.ProjectSdksModel
import com.intellij.openapi.roots.ui.configuration.projectRoot.StructureConfigurableContext
import javax.swing.JComponent

/**
 * Created by hsslbch on 1/5/17.
 */
class Configurable(val project: Project) : SearchableConfigurable {

    /// representation

    override fun getHelpTopic(): String? {
        return "preferences.EnvMixerConfigurable"
    }

    override fun getDisplayName(): String {
        return "EnvMixer Plugin"
    }

    override fun getId(): String {
        return "preferences.EnvMixerConfigurable"
    }

    /// behaviour

    private var configurationForm: KConfigurationForm? = null

    override fun createComponent(): JComponent? {
        configurationForm = KConfigurationForm(ConfigurationForm())
        reset()
        return configurationForm?.getRootPanel()
    }

    override fun disposeUIResources() {
        configurationForm = null
        super.disposeUIResources()
    }


    override fun isModified(): Boolean {
        return StateService.getInstance(project).state == configurationForm?.getSettings()
    }

    override fun apply() {
        val cf = configurationForm
        if (cf != null) {
            StateService.getInstance(project).loadState(cf.getSettings())
        }
    }

    override fun reset() {
        val state = StateService.getInstance(project).state
        configurationForm?.setSettings(state)
    }


}
