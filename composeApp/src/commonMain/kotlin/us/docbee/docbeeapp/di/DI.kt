package us.docbee.docbeeapp.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import us.docbee.docbeeapp.data.EmailAuth
import us.docbee.docbeeapp.data.datasources.ContactRemoteDataSource
import us.docbee.docbeeapp.data.datasources.JsonResourceLoader
import us.docbee.docbeeapp.data.datasources.UserRemoteDataSource
import us.docbee.docbeeapp.data.datasources.UserSessionLocalDataSource
import us.docbee.docbeeapp.data.datasources.getContactDataSource
import us.docbee.docbeeapp.data.datasources.getUserDataSource
import us.docbee.docbeeapp.data.datasources.interfaces.ResourcesLoader
import us.docbee.docbeeapp.data.datasources.interfaces.UserSessionDataSource
import us.docbee.docbeeapp.data.getEmailAuth
import us.docbee.docbeeapp.data.repositories.ContactDataRepository
import us.docbee.docbeeapp.data.repositories.EmailAuthDataRepository
import us.docbee.docbeeapp.data.repositories.JsonCountryRepository
import us.docbee.docbeeapp.data.repositories.SessionDataRepository
import us.docbee.docbeeapp.data.repositories.UserDataRepository
import us.docbee.docbeeapp.domain.repositories.ContactsRepository
import us.docbee.docbeeapp.domain.repositories.CountryRepository
import us.docbee.docbeeapp.domain.repositories.EmailAuthRepository
import us.docbee.docbeeapp.domain.repositories.SessionRepository
import us.docbee.docbeeapp.domain.repositories.UserRepository
import us.docbee.docbeeapp.domain.usecases.EmailAuthUseCase
import us.docbee.docbeeapp.domain.usecases.EmailSignupUseCase
import us.docbee.docbeeapp.domain.usecases.GetCountriesUseCase
import us.docbee.docbeeapp.domain.usecases.GetUserContactsUseCase
import us.docbee.docbeeapp.domain.usecases.GetUserSessionStatus
import us.docbee.docbeeapp.domain.usecases.LogoutUseCase
import us.docbee.docbeeapp.domain.usecases.SaveUserContactUseCase
import us.docbee.docbeeapp.domain.usecases.directory.DeleteUserContactUseCase
import us.docbee.docbeeapp.presentation.dashboard.DashboardViewModel
import us.docbee.docbeeapp.presentation.directory.AddContactViewModel
import us.docbee.docbeeapp.presentation.directory.DirectoryViewModel
import us.docbee.docbeeapp.presentation.login.LoginViewModel
import us.docbee.docbeeapp.presentation.login.SignupViewModel
import us.docbee.docbeeapp.presentation.settings.SettingsViewModel
import us.docbee.docbeeapp.presentation.splash.SplashViewModel

expect val nativeModules: Module

val dataSourcesModule = module {
    factory<EmailAuth> { getEmailAuth() }
    factory<ResourcesLoader> { JsonResourceLoader() }
    factory<UserRemoteDataSource> { getUserDataSource() }
    factory<ContactRemoteDataSource> { getContactDataSource() }
    factory<UserSessionDataSource> { UserSessionLocalDataSource(get()) }
}

val repositoryModule = module {
    factory<EmailAuthRepository> { EmailAuthDataRepository(get()) }
    factory<CountryRepository> { JsonCountryRepository(get()) }
    factory<UserRepository> { UserDataRepository(get()) }
    factory<ContactsRepository> { ContactDataRepository(get()) }
    factory<SessionRepository> { SessionDataRepository(get(), get()) }
}

val usesCasesModule = module {
    factory { EmailAuthUseCase(get(), get()) }
    factory { EmailSignupUseCase(get(), get()) }
    factory { GetCountriesUseCase(get()) }
    factory { GetUserContactsUseCase(get(), get()) }
    factory { SaveUserContactUseCase(get(), get()) }
    factory { DeleteUserContactUseCase(get(), get()) }
    factory { GetUserSessionStatus(get(), get()) }
    factory { LogoutUseCase(get()) }
}

val viewModelsModule = module {
    viewModelOf(::SplashViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignupViewModel)
    viewModelOf(::DashboardViewModel)
    viewModelOf(::DirectoryViewModel)
    viewModelOf(::AddContactViewModel)
    viewModelOf(::SettingsViewModel)
}

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            nativeModules,
            dataSourcesModule,
            repositoryModule,
            usesCasesModule,
            viewModelsModule
        )
    }
}