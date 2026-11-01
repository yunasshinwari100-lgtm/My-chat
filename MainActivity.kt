package be.mychat.assistant

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.speech.RecognizerIntent
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import java.util.Locale

class MainActivity : Activity() {
    private lateinit var commandInput: EditText
    private lateinit var planText: TextView
    private var plannedCommand: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val outer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(28, 32, 28, 24)
            setBackgroundColor(0xFFF6F7FB.toInt())
        }
        val title = TextView(this).apply {
            text = "my chat"
            textSize = 30f
            setTextColor(0xFF17233C.toInt())
        }
        val subtitle = TextView(this).apply {
            text = "Jouw AI-assistent voor je gsm"
            textSize = 16f
            setTextColor(0xFF596579.toInt())
        }
        commandInput = EditText(this).apply {
            hint = "Bijv. Open CapCut"
            textSize = 17f
            minLines = 2
            setPadding(18, 16, 18, 16)
            setBackgroundColor(0xFFFFFFFF.toInt())
        }
        val voice = Button(this).apply {
            text = "🎙 Spreek opdracht in"
            setOnClickListener { startVoiceInput() }
        }
        val makePlan = Button(this).apply {
            text = "Maak stappenplan"
            setOnClickListener { showPlan() }
        }
        planText = TextView(this).apply {
            text = "Je stappenplan verschijnt hier."
            textSize = 16f
            setTextColor(0xFF263247.toInt())
            setPadding(0, 16, 0, 16)
        }
        val execute = Button(this).apply {
            text = "Start uitvoering"
            setOnClickListener {
                if (plannedCommand.isBlank()) {
                    Toast.makeText(this@MainActivity, "Maak eerst een stappenplan.", Toast.LENGTH_SHORT).show()
                } else {
                    MyChatAccessibilityService.executeCommand(plannedCommand)
                    Toast.makeText(this@MainActivity, "Opdracht doorgegeven. Schakel eerst de bedieningstoestemming in.", Toast.LENGTH_LONG).show()
                }
            }
        }
        val permission = Button(this).apply {
            text = "Verbind met mijn gsm (toestemming)"
            setOnClickListener { startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }
        }
        val note = TextView(this).apply {
            text = "Let op: Android vraagt jou om My Chat handmatig toe te staan bij Toegankelijkheid. De bediening werkt alleen binnen de mogelijkheden en regels van Android."
            textSize = 13f
            setTextColor(0xFF697386.toInt())
            setPadding(0, 12, 0, 0)
        }
        outer.addView(title)
        outer.addView(subtitle)
        outer.addView(commandInput, LinearLayout.LayoutParams(-1, ViewGroup.LayoutParams.WRAP_CONTENT).apply { topMargin = 24 })
        outer.addView(voice)
        outer.addView(makePlan)
        outer.addView(planText)
        outer.addView(execute)
        outer.addView(permission)
        outer.addView(note)
        val scroll = ScrollView(this).apply { addView(outer) }
        setContentView(scroll)
    }

    private fun showPlan() {
        plannedCommand = commandInput.text.toString().trim()
        if (plannedCommand.isBlank()) {
            planText.text = "Typ eerst wat My Chat moet doen."
            return
        }
        val steps = CommandPlanner.plan(plannedCommand)
        planText.text = "Plan:\n" + steps.mapIndexed { i, s -> "${i + 1}. $s" }.joinToString("\n") +
            "\n\nControleer het plan en druk daarna op Start uitvoering."
    }

    private fun startVoiceInput() {
        try {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "nl-BE")
                putExtra(RecognizerIntent.EXTRA_PROMPT, "Wat moet My Chat doen?")
            }
            startActivityForResult(intent, 42)
        } catch (_: Exception) {
            Toast.makeText(this, "Spraakherkenning is niet beschikbaar op dit toestel.", Toast.LENGTH_LONG).show()
        }
    }

    @Deprecated("Deprecated by Android API; kept for broad device compatibility")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 42 && resultCode == RESULT_OK) {
            val spoken = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
            if (!spoken.isNullOrBlank()) {
                commandInput.setText(spoken)
                showPlan()
            }
        }
    }
}
