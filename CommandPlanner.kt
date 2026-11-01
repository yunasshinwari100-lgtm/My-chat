package be.mychat.assistant

object CommandPlanner {
    fun plan(command: String): List<String> {
        val c = command.lowercase()
        return when {
            c.contains("open") || c.contains("start") || c.contains("ga naar") -> listOf(
                "Zoek de gevraagde app op je gsm.",
                "Open de app als die geïnstalleerd is.",
                "Wacht tot de app klaar is."
            )
            c.contains("scroll") || c.contains("naar beneden") -> listOf(
                "Controleer welke app op het scherm staat.",
                "Scroll omlaag in de huidige app."
            )
            c.contains("terug") -> listOf("Ga één scherm terug.")
            c.contains("tik") || c.contains("klik") -> listOf(
                "Zoek de knop of tekst die je noemt.",
                "Tik erop als die zichtbaar en herkenbaar is."
            )
            else -> listOf(
                "Begrijp de opdracht: \"$command\".",
                "Bepaal welke app en schermactie nodig zijn.",
                "Voer alleen acties uit die Android veilig toestaat."
            )
        }
    }
}
