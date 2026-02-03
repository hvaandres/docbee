package us.docbee.docbeeapp.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import us.docbee.docbeeapp.data.EmailAuth
import us.docbee.docbeeapp.data.datasources.AlertsRemoteDataSource
import us.docbee.docbeeapp.data.datasources.ContactRemoteDataSource
import us.docbee.docbeeapp.data.datasources.JsonResourceLoader
import us.docbee.docbeeapp.data.datasources.LocationDataSource
import us.docbee.docbeeapp.data.datasources.UserRemoteDataSource
import us.docbee.docbeeapp.data.datasources.UserSessionLocalDataSource
import us.docbee.docbeeapp.data.datasources.getAlertsDataSource
import us.docbee.docbeeapp.data.datasources.getContactDataSource
import us.docbee.docbeeapp.data.datasources.getUserDataSource
import us.docbee.docbeeapp.data.datasources.interfaces.ResourcesLoader
import us.docbee.docbeeapp.data.datasources.interfaces.UserSessionDataSource
import us.docbee.docbeeapp.data.datasources.provideLocationDataSource
import us.docbee.docbeeapp.data.getEmailAuth
import us.docbee.docbeeapp.data.repositories.AlertsDataRepository
import us.docbee.docbeeapp.data.repositories.ContactDataRepository
import us.docbee.docbeeapp.data.repositories.EmailAuthDataRepository
import us.docbee.docbeeapp.data.repositories.EmergencyDataRepository
import us.docbee.docbeeapp.data.repositories.JsonCountryRepository
import us.docbee.docbeeapp.data.repositories.LocationDataRepository
import us.docbee.docbeeapp.data.repositories.SessionDataRepository
import us.docbee.docbeeapp.data.repositories.UserDataRepository
import us.docbee.docbeeapp.data.services.EmergencyNotificationApiService
import us.docbee.docbeeapp.domain.managers.AlertsStringsManager
import us.docbee.docbeeapp.domain.repositories.AlertsRepository
import us.docbee.docbeeapp.domain.repositories.ContactsRepository
import us.docbee.docbeeapp.domain.repositories.CountryRepository
import us.docbee.docbeeapp.domain.repositories.EmailAuthRepository
import us.docbee.docbeeapp.domain.repositories.EmergenciesRepository
import us.docbee.docbeeapp.domain.repositories.LocationRepository
import us.docbee.docbeeapp.domain.repositories.SessionRepository
import us.docbee.docbeeapp.domain.repositories.UserRepository
import us.docbee.docbeeapp.domain.usecases.EmailAuthUseCase
import us.docbee.docbeeapp.domain.usecases.EmailSignupUseCase
import us.docbee.docbeeapp.domain.usecases.FetchLocationUseCase
import us.docbee.docbeeapp.domain.usecases.alerts.GetAlertsUseCase
import us.docbee.docbeeapp.domain.usecases.GetCountriesUseCase
import us.docbee.docbeeapp.domain.usecases.GetUserContactsUseCase
import us.docbee.docbeeapp.domain.usecases.GetUserSessionStatus
import us.docbee.docbeeapp.domain.usecases.LogoutUseCase
import us.docbee.docbeeapp.domain.usecases.NotifyEmergencyUseCase
import us.docbee.docbeeapp.domain.usecases.SaveUserContactUseCase
import us.docbee.docbeeapp.domain.usecases.SendEmergencyUseCase
import us.docbee.docbeeapp.domain.usecases.alerts.DeleteAlertsUseCase
import us.docbee.docbeeapp.domain.usecases.alerts.ModifyAlertsUseCase
import us.docbee.docbeeapp.domain.usecases.alerts.SaveAlertsUseCase
import us.docbee.docbeeapp.domain.usecases.directory.DeleteUserContactUseCase
import us.docbee.docbeeapp.presentation.alerts.AlertsViewModel
import us.docbee.docbeeapp.presentation.dashboard.DashboardViewModel
import us.docbee.docbeeapp.presentation.directory.AddContactViewModel
import us.docbee.docbeeapp.presentation.directory.DirectoryViewModel
import us.docbee.docbeeapp.presentation.emergency.EmergencyViewModel
import us.docbee.docbeeapp.presentation.home.HomeViewModel
import us.docbee.docbeeapp.presentation.login.LoginViewModel
import us.docbee.docbeeapp.presentation.login.SignupViewModel
import us.docbee.docbeeapp.presentation.settings.SettingsViewModel
import us.docbee.docbeeapp.presentation.splash.SplashViewModel
import us.docbee.docbeeapp.utils.ui.managers.AlertsStringDataManager
import us.docbee.docbeeapp.utils.ui.network.NetworkUtils
import us.docbee.docbeeapp.utils.ui.network.getNetworkUtils
import us.docbee.docbeeapp.utils.ui.permissions.provideLocationPermissionManager

expect val nativeModules: Module

val dataSourcesModule = module {
    factory<EmailAuth> { getEmailAuth() }
    factory<ResourcesLoader> { JsonResourceLoader() }
    factory<UserRemoteDataSource> { getUserDataSource() }
    factory<ContactRemoteDataSource> { getContactDataSource() }
    factory<AlertsRemoteDataSource> { getAlertsDataSource() }
    factory<UserSessionDataSource> { UserSessionLocalDataSource(get()) }
    factory<LocationDataSource> { provideLocationDataSource() }
    factory { EmergencyNotificationApiService() }
}

val repositoryModule = module {
    factory<EmailAuthRepository> { EmailAuthDataRepository(get()) }
    factory<CountryRepository> { JsonCountryRepository(get()) }
    factory<UserRepository> { UserDataRepository(get()) }
    factory<ContactsRepository> { ContactDataRepository(get()) }
    factory<SessionRepository> { SessionDataRepository(get(), get()) }
    factory<LocationRepository> { LocationDataRepository(get()) }
    factory<EmergenciesRepository> { EmergencyDataRepository(get()) }
    factory<AlertsRepository> { AlertsDataRepository(get(), get()) }
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
    factory { FetchLocationUseCase(get(), get()) }
    factory { SendEmergencyUseCase(get()) }
    factory { NotifyEmergencyUseCase(get(), get(), get()) }
    factory { GetAlertsUseCase(get(), get()) }
    factory { SaveAlertsUseCase(get(), get()) }
    factory { DeleteAlertsUseCase(get(), get()) }
    factory { ModifyAlertsUseCase(get(), get()) }
}


val managersModule = module {
    single { provideLocationPermissionManager() }
    single<AlertsStringsManager> { AlertsStringDataManager() }
    single { getNetworkUtils().also { it.start() } }
}

val viewModelsModule = module {
    viewModelOf(::SplashViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignupViewModel)
    viewModelOf(::DashboardViewModel)
    viewModelOf(::DirectoryViewModel)
    viewModelOf(::AddContactViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::EmergencyViewModel)
    viewModelOf(::AlertsViewModel)
}

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            managersModule,
            nativeModules,
            dataSourcesModule,
            repositoryModule,
            usesCasesModule,
            viewModelsModule
        )
    }
}