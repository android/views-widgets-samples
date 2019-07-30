/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.multiwindowplayground.logger

/**
 * Helper class for a list (or tree) of LoggerNodes.
 *
 *
 * When this is set as the head of the list,
 * an instance of it can function as a drop-in replacement for [android.util.Log].
 * Most of the methods in this class serve only to map a method call in Log to its equivalent
 * in LogNode.
 */
object Log {

    internal val NONE = -1

    // Use the native value from Android's native logging facilities for easy migration and interop.
    private val DEBUG = android.util.Log.DEBUG

    // Stores the beginning of the LogNode topology.
    internal var logNode: LogNode? = null

    /**
     * Instructs the LogNode to print the log data provided. Other LogNodes can
     * be chained to the end of the LogNode as desired.
     *
     * @param priority Log level of the data being logged. Verbose, Error, etc.
     * @param tag      Tag for for the log data. Can be used to organize log statements.
     * @param msg      The actual message to be logged. The actual message to be logged.
     */
    private fun println(priority: Int, tag: String, msg: String) {
        logNode?.println(priority, tag, msg, null)
    }

    /**
     * Prints a message at DEBUG priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     */
    fun d(tag: String, msg: String) {
        println(DEBUG, tag, msg)
    }
}
