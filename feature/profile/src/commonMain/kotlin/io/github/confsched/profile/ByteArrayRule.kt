package io.github.confsched.profile

import soil.form.core.ValidationResult
import soil.form.core.ValidationRule
import soil.form.core.ValidationRuleBuilder

typealias ByteArrayRule = ValidationRule<ByteArray>
typealias ByteArrayRuleBuilder = ValidationRuleBuilder<ByteArray>

fun ByteArrayRule(
    predicate: ByteArray.() -> Boolean,
    message: () -> String,
): ByteArrayRule = { value ->
    if (value.predicate()) ValidationResult.Valid else ValidationResult.Invalid(message())
}

fun ByteArrayRuleBuilder.notEmpty(message: () -> String): ByteArrayRule = ByteArrayRule(
    predicate = { isNotEmpty() },
    message = message,
)
