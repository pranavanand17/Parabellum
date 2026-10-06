package com.parabellum.app.model

enum class TaskSection(
    val code: String,
    val title: String,
    val description: String
) {
    IMMEDIATE("01", "IMMEDIATE", "CRITICAL QUEUE // ACTION REQUIRED"),
    SOON("02", "SOON", "PENDING QUEUE // PRIORITY 2"),
    LATER("03", "LATER", "DEFERRED QUEUE // FUTURE PROCESSING"),
    EXTRAS("04", "EXTRAS", "SUPPLEMENTAL DATA // NON-CRITICAL"),
    MAYBE("05", "MAYBE", "SPECULATIVE QUEUE // CONDITIONAL");

    val displayLabel: String
        get() = "$code // $title"
}
