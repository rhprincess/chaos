package io.rhprincess.chaos.main

object ChaosException {
    class ApplyingFailure(msg: String) : Exception(msg)
    class LinearLayoutWeightFailure(msg: String) : Exception(msg)
    class UnsupportedAttribute(attr: String, type: Class<*>) :
        Exception("Trying to using a unsupported attribute \"$attr\" in ${type.simpleName}")

    class IdNotFound(id: String) : Exception("Cannot find any bound id call: \"$id\"")
    class DuplicatedIdValueFailure(id: String) : Exception("Cannot use id value: $id twice.")
    class DuplicatedIdNameFailure(id: String) : Exception("Cannot use id name: $id twice.")
    class IncludeParamsFailure(msg: String) : Exception(msg)
    class UnsupportedViewManager(msg: String) : Exception(msg)
    class ContextNotFound : Exception("Cannot found any context in your application.")
    class MultiConstructorFailure(target: Int, current: Int) :
        Exception("Expected $target constructor${if (target > 1) "s" else ""}, found $current constructor${if (current > 1) "s" else ""}")

    class ConstructorNotFound(clazzName: String) :
        Exception("Cannot found constructor for $clazzName")

    class ComponentNotFound(msg: String) : Exception(msg)
}