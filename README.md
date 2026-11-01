# My Chat Android — eerste native basis

Dit is een Android Studio-project (Kotlin) voor een native My Chat-app met een AccessibilityService.

## Wat deze eerste basis al bevat
- Native Android-appscherm.
- Opdracht typen of inspreken (Nederlandse spraakherkenning via Android).
- Een eenvoudig stappenplan vóór uitvoering.
- Instellingenknop om Android-toegankelijkheid te openen.
- Eerste bediening: openen van CapCut, TikTok, YouTube, WhatsApp en Chrome; terug; home; scrollen; tikken op zichtbare tekst.

## Wat nog NIET af is
- Dit is nog geen volwaardige AI-agent en geen kant-en-klare APK.
- De planner is voorlopig eenvoudig en lokaal; er is nog geen AI-backend gekoppeld.
- App-bediening verschilt per app en scherm. Sommige knoppen zijn niet toegankelijk voor Android Accessibility.
- Tekst invoeren, media bewerken, bestanden beheren en complexe workflows moeten nog per actie worden gebouwd en getest.
- Android kan om extra bevestiging/toestemming vragen.

## APK bouwen
1. Installeer Android Studio op een computer.
2. Open deze projectmap in Android Studio.
3. Laat Gradle synchroniseren en installeer de gevraagde Android SDK (API 35).
4. Kies Build > Build Bundle(s) / APK(s) > Build APK(s).
5. Android Studio maakt de APK in `app/build/outputs/apk/debug/`.

## Toegankelijkheid inschakelen op Samsung
Na installatie: Instellingen > Toegankelijkheid > Geïnstalleerde apps (of Geavanceerde instellingen) > My Chat app-bediening > Aan. De precieze menunamen kunnen verschillen per One UI-versie.

Geef deze toestemming alleen aan software die je vertrouwt. De service kan scherminhoud bekijken en acties uitvoeren.
