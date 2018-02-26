package akhmedoff.usman.data.utils.interceptors

import akhmedoff.usman.data.Error

class LogException(override val message: String, val error: Error) : Exception(message)