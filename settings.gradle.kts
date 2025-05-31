pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MoviesApp"
include(":app")
include(":core")
include(":data")
include(":domain")
include(":features")
include(":features:movieList")
include(":domain:movies")
include(":data:moviesRepository")
include(":data:repositories")
include(":data:services")
include(":data:services:remoteDataSourceServices")
include(":data:services:localDataSourceServices")
include(":data:services:localDataSourceServices:localMovies")
include(":data:services:remoteDataSourceServices:remoteMovies")
include(":core:coreNetwork")
