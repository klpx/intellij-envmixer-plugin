package com.github.klpx.envmixer

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.project.Project

/**
 * Created by hsslbch on 1/5/17.
 */
@State(name = "envmixer")
class StateService : PersistentStateComponent<StateService.Settings> {

    data class Settings(
            var mixinGroups: List<EnvMixinGroup> = listOf()
    )

    data class EnvMixinGroup(
            var name: String = "",
            var mixins: List<StaticVarsEnvMixin> = listOf()
    )

    data class StaticVarsEnvMixin(
            var name: String = "",
            var vars: Map<String, String> = mapOf()
    )

    private var state: Settings = Settings(listOf(
            EnvMixinGroup("test1", listOf(
                    StaticVarsEnvMixin("digits", mapOf("N1" to "111", "N2" to "222")),
                    StaticVarsEnvMixin("words", mapOf("N1" to "one", "N2" to "two"))
            ))
    ))


    override fun getState(): Settings {
        return state
    }

    override fun loadState(newState: Settings) {
        state = newState
    }

    companion object {
        fun getInstance(project: Project): StateService {
            return ServiceManager.getService(project, StateService::class.java)
        }
    }
}
