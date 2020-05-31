package com.tegMob.connectivity
import retrofit2.converter.gson.GsonConverterFactory

object ClientBuilder {

    object MatchesClientBuilder {
        private const val matchesUrl = "https://e9c4x73vla.execute-api.us-east-1.amazonaws.com/dev/"

        fun<T> buildService(service: Class<T>): T{
            return ClientBuilder.buildService(service, matchesUrl)
        }
    }

    object UsersClientBuilder {
        private const val usersUrl = "https://tzdb0jz1yh.execute-api.us-east-1.amazonaws.com/dev/"

        fun<T> buildService(service: Class<T>): T{
            return ClientBuilder.buildService(service, usersUrl)
        }
    }

    object PlayersClientBuilder {
        private const val playersUrl = "https://fjskeqpwi3.execute-api.us-east-1.amazonaws.com/dev/"

        fun<T> buildService(service: Class<T>): T{
            return ClientBuilder.buildService(service, playersUrl)
        }
    }

    fun<T> buildService(service: Class<T>, baseUrl: String): T{
        return retrofit2.Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
            .create(service)
    }
}