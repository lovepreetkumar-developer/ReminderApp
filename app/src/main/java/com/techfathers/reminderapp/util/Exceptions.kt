package com.techfathers.reminderapp.util

import java.io.IOException

class MyApiException(message: String) : IOException(message)
class NoInternetException(message: String) : IOException(message)