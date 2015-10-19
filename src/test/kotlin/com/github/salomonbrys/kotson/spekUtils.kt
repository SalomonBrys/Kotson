package com.github.salomonbrys.kotson

import org.jetbrains.spek.api.It
import org.jetbrains.spek.api.shouldThrow

public abstract class Spek(init: Spek.() -> Unit) : org.jetbrains.spek.api.Spek() {
    init {
        init()
    }
}

public inline fun It.shouldThrow<reified T: Throwable>(noinline block: () -> Any): T {
    return shouldThrow(T::class.java, block)
}
