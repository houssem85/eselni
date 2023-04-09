package com.programasoft.application

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource

import java.io.IOException


@Configuration
class FirebaseConfig {
    @Bean
    @Throws(IOException::class)
    fun firebaseMessaging(): FirebaseMessaging? {
        val googleCredentials: GoogleCredentials = GoogleCredentials
            .fromStream(ClassPathResource("firebase-service-account.json").inputStream)
        val firebaseOptions: FirebaseOptions = FirebaseOptions
            .builder()
            .setCredentials(googleCredentials)
            .build()
        val app: FirebaseApp = FirebaseApp.initializeApp(firebaseOptions, "my-app")
        return FirebaseMessaging.getInstance(app)
    }
}