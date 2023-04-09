package com.programasoft.application

import com.programasoft.application.account.Account
import com.programasoft.application.account.AccountService
import com.programasoft.application.administrator.Administrator
import com.programasoft.application.administrator.AdministratorService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class ApplicationStartup(
    private val administratorService: AdministratorService,
    private val accountService: AccountService,
) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {

        if (administratorService.count() == 0L) {
            val account = Account(
                id = 0,
                email = "administrator@eselni.com",
                password = "admin",
                fullName = "Administrator"
            )
            val createdAccount = accountService.create(account)
            val administrator = Administrator(
                id = 0,
                account = createdAccount
            )
            administratorService.create(administrator)
        }
    }
}