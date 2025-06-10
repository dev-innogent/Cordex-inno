package com.systemmind;

/** Simple voice helper with placeholders for speech recognition and TTS. */
public class VoiceModule {
    public String listen() {
        // TODO integrate a speech-to-text library such as Sphinx4
        return null;
    }

    public void speak(String text) {
        // TODO integrate a text-to-speech library such as FreeTTS
        System.out.println("VOICE: " + text);
    }

    public boolean askYesNo(String question) {
        speak(question + " (say 'yes' or 'no')");
        String reply = listen();
        return reply != null && reply.trim().equalsIgnoreCase("yes");
    }
}
