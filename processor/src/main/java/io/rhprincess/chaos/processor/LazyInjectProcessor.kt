package io.rhprincess.chaos.processor

import com.squareup.kotlinpoet.*
import io.rhprincess.chaos.annotations.LazyInject
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement

class LazyInjectProcessor : AbstractProcessor() {

    override fun getSupportedAnnotationTypes() = mutableSetOf(LazyInject::class.java.canonicalName)

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        renv: RoundEnvironment
    ): Boolean {
        generateLazyViews(renv)
        return true
    }

    @OptIn(DelicateKotlinPoetApi::class)
    private fun generateLazyViews(renv: RoundEnvironment) {
        val ktf = FileSpec.builder("io.rhprincess.chaos.lazy", "LazyViews")
        renv.getElementsAnnotatedWith(LazyInject::class.java).forEach { element ->
            val annotation = element.getAnnotation(LazyInject::class.java)
            val viewName = element.simpleName.first()
                .uppercase() + element.simpleName.removeRange(0, 1)
            ktf.addImport(annotation.packageName, viewName)
            if (element.kind != ElementKind.METHOD) throw RuntimeException("@LazyInject must modify function! Current kind is " + element.kind)

            // transform this element into func element
            val func = element as ExecutableElement
            val lazyFunc = FunSpec.builder("${element.simpleName}")
                .receiver(ClassName("io.rhprincess.chaos.lazy", "LazyViewFactory"))

            func.parameters.forEachIndexed { index, ele ->
                // index 0 aka the first parameter is the function's extender
                if (index != 0) {
                    // if the variable name is init, we add lambda on it
                    when (val name = ele.simpleName.toString()) {
                        "init" -> {
                            val t = TypeVariableName(viewName)
                            val lambda = LambdaTypeName.get(
                                receiver = t,
                                returnType = Unit::class.asClassName(),
                            )
                            val initParameter =
                                ParameterSpec.builder(name, lambda)
                                    .defaultValue("{}")
                            lazyFunc.addParameter(initParameter.build())
                        }
                        "theme", "styledLayout" -> {
                            val themeParameter =
                                ParameterSpec.builder(name, Int::class.java)
                                    .defaultValue("0")
                            lazyFunc.addParameter(themeParameter.build())
                        }
                        else -> {
                            lazyFunc.addParameter(name, ele.asType().asTypeName())
                        }
                    }
                }
            }

            lazyFunc.returns(TypeVariableName(viewName))
                .addCode("""
                    return add(theme, styledLayout, init) { $viewName(it) }
                """.trimIndent())
            ktf.addFunction(lazyFunc.build())
        }
        try {
            val builder = ktf.build()
            builder.writeTo(processingEnv.filer)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}