package us.docbee.docbeeapp.di

import org.koin.core.context.startKoin
import org.koin.dsl.module
import us.docbee.docbeeapp.data.EmailAuth
import us.docbee.docbeeapp.data.getEmailAuth
import us.docbee.docbeeapp.data.repositories.EmailAuthDataRepository
import us.docbee.docbeeapp.domain.repositories.EmailAuthRepository
import us.docbee.docbeeapp.domain.usecases.EmailAuthUseCase

val dataSourcesModule = module {
    factory<EmailAuth> { getEmailAuth() }
}

val repositoryModule = module {
    factory<EmailAuthRepository> { EmailAuthDataRepository(get()) }
}

val usesCasesModule = module {
    factory { EmailAuthUseCase(get()) }
}

fun initKoin() {
    startKoin {
        modules(dataSourcesModule, repositoryModule, usesCasesModule)
    }
}