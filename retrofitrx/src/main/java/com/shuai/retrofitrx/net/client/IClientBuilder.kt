package com.shuai.retrofitrx.net.client

interface IClientBuilder<T> {
    fun build(): T
}