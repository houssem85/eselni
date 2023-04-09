package com.programasoft.application.account

import com.programasoft.application.administrator.Administrator
import com.programasoft.application.advocate.Advocate
import com.programasoft.application.client.Client

data class LoginResponse(
    val client: Client?,
    val advocate: Advocate?,
    val administrator: Administrator?
)