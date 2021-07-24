package com.kaffka.httpserver.ui

import com.google.gson.FieldNamingPolicy
import com.kaffka.httpserver.data.ServerAddressRepository
import com.kaffka.httpserver.data.ServicesRepository
import com.kaffka.httpserver.domain.Routes
import com.kaffka.httpserver.domain.usecases.CallLogUseCase
import com.kaffka.httpserver.domain.usecases.GetCallStatusUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

@Module
@InstallIn(ServiceComponent::class)
object ServerModule {

    @Provides
    fun providesServer(
        callRepository: CallLogUseCase,
        serverAddressRepository: ServerAddressRepository,
        servicesRepository: ServicesRepository,
        getCallStatusUseCase: GetCallStatusUseCase,
    ): ApplicationEngine =
        embeddedServer(Netty, port = serverAddressRepository.serverPort) {
            install(ContentNegotiation) {
                gson { this.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES) }
            }
            routing {
                get("/${Routes.LOG.value}") {
                    call.respond(callRepository.getCallLog())
                }
                get("/${Routes.ROOT.value}") {
                    call.respond(servicesRepository.getServices())
                }
                get("/${Routes.STATUS.value}") {
                    call.respond(getCallStatusUseCase.getCallStatus())
                }
            }
        }
}
