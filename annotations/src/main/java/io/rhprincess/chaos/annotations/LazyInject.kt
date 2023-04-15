package io.rhprincess.chaos.annotations

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class LazyInject(val packageName: String)