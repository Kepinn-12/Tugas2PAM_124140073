package com.example.tugas_2_pam_124140073

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform