package us.docbee.docbeeapp.di

import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import us.docbee.docbeeapp.data.EmailAuth
import us.docbee.docbeeapp.data.datasources.ContactRemoteDataSource
import us.docbee.docbeeapp.data.datasources.JsonResourceLoader
import us.docbee.docbeeapp.data.datasources.UserRemoteDataSource
import us.docbee.docbeeapp.data.datasources.getContactDataSource
import us.docbee.docbeeapp.data.datasources.getUserDataSource
import us.docbee.docbeeapp.data.datasources.interfaces.ResourcesLoader
import us.docbee.docbeeapp.data.getEmailAuth
import us.docbee.docbeeapp.data.repositories.ContactDataRepository
import us.docbee.docbeeapp.data.repositories.EmailAuthDataRepository
import us.docbee.docbeeapp.data.repositories.JsonCountryRepository
import us.docbee.docbeeapp.data.repositories.UserDataRepository
import us.docbee.docbeeapp.domain.repositories.ContactsRepository
import us.docbee.docbeeapp.domain.repositories.CountryRepository
import us.docbee.docbeeapp.domain.repositories.EmailAuthRepository
import us.docbee.docbeeapp.domain.repositories.UserRepository
import us.docbee.docbeeapp.domain.usecases.EmailAuthUseCase
import us.docbee.docbeeapp.domain.usecases.EmailSignupUseCase
import us.docbee.docbeeapp.domain.usecases.GetCountriesUseCase
import us.docbee.docbeeapp.domain.usecases.GetUserContactsUseCase
import us.docbee.docbeeapp.domain.usecases.SaveUserContactUseCase
import us.docbee.docbeeapp.presentation.dashboard.DashboardViewModel
import us.docbee.docbeeapp.presentation.directory.AddContactViewModel
import us.docbee.docbeeapp.presentation.directory.DirectoryViewModel
import us.docbee.docbeeapp.presentation.login.LoginViewModel
import us.docbee.docbeeapp.presentation.login.SignupViewModel

val dataSourcesModule = module {
    factory<EmailAuth> { getEmailAuth() }
    factory<ResourcesLoader> { JsonResourceLoader() }
    factory<UserRemoteDataSource> { getUserDataSource() }
    factory<ContactRemoteDataSource> { getContactDataSource() }
}

val repositoryModule = module {
    factory<EmailAuthRepository> { EmailAuthDataRepository(get()) }
    factory<CountryRepository> { JsonCountryRepository(get()) }
    factory<UserRepository> { UserDataRepository(get()) }
    factory<ContactsRepository> { ContactDataRepository(get()) }
}

val usesCasesModule = module {
    factory { EmailAuthUseCase(get()) }
    factory { EmailSignupUseCase(get(), get()) }
    factory { GetCountriesUseCase(get()) }
    factory { GetUserContactsUseCase(get(), get()) }
    factory { SaveUserContactUseCase(get(), get()) }
}

val viewModelsModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignupViewModel)
    viewModelOf(::DashboardViewModel)
    viewModelOf(::DirectoryViewModel)
    viewModelOf(::AddContactViewModel)
}

fun initKoin() {
    startKoin {
        modules(dataSourcesModule, repositoryModule, usesCasesModule, viewModelsModule)
    }
}