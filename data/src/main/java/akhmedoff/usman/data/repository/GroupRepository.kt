package akhmedoff.usman.data.repository

import akhmedoff.usman.data.api.VkApi
import akhmedoff.usman.data.local.UserSettings


class GroupRepository(
    private val api: VkApi,
    userSettings: UserSettings
) {
    fun getGroup(id: String) = api.getGroups(groupId = id)

    fun getGroups(ids: List<String>) = api.getGroups(ids)
}