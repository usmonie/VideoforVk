package akhmedoff.usman.data.repository

import akhmedoff.usman.data.api.VkApi


class GroupRepository(private val api: VkApi) {
    fun getGroup(id: String) = api.getGroups(groupId = id)

    fun getGroups(ids: List<String>) = api.getGroups(ids)
}