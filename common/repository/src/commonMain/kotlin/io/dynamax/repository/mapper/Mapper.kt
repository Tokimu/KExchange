package io.dynamax.repository.mapper

interface Mapper<T : Any, U : Any> {
    fun map(t: T): U
}
