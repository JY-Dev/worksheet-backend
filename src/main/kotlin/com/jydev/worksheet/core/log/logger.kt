package com.mindshare.api.core.log

import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun <T : Any> T.logger(): Logger = LoggerFactory.getLogger(this::class.java)