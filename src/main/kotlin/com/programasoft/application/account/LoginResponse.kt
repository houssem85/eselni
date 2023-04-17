package com.programasoft.application.account

import com.programasoft.application.administrator.Administrator
import com.programasoft.application.psychologist.Psychologist
import com.programasoft.application.client.Client

data class LoginResponse(
    val client: Client?,
    val psychologist: Psychologist?,
    val administrator: Administrator?
)