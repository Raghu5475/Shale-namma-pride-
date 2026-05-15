package com.shalenammapride.app.data.service

import com.shalenammapride.app.data.model.AnnouncementDraft

class GenAiService {
    suspend fun generateAnnouncement(input: String): AnnouncementDraft {
        val trimmed = input.trim()
        if (trimmed.isBlank()) return AnnouncementDraft()

        return AnnouncementDraft(
            input = trimmed,
            english = "Today at school: $trimmed. Thank you to our students, teachers, and community for making the day meaningful.",
            kannada = "ಇಂದು ಶಾಲೆಯಲ್ಲಿ: $trimmed. ಈ ದಿನವನ್ನು ಅರ್ಥಪೂರ್ಣಗೊಳಿಸಿದ ವಿದ್ಯಾರ್ಥಿಗಳು, ಶಿಕ್ಷಕರು ಮತ್ತು ಸಮುದಾಯಕ್ಕೆ ಧನ್ಯವಾದಗಳು."
        )
    }
}
