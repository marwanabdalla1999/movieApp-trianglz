package com.trianglz.movies.pagingMediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.trianglz.localDatabase.dataBase.AppDatabase
import com.trianglz.localDatabase.movies.entities.MovieEntity
import com.trianglz.localDatabase.movies.entities.MovieRemoteKeys
import com.trianglz.movies.localDataSource.ILocalMoviesDataSource
import com.trianglz.movies.models.mappers.toEntity
import com.trianglz.movies.remoteDataSource.IMoviesRemoteDataSource
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class MoviesRemoteMediator(
    private val remoteDataSource: IMoviesRemoteDataSource, // your existing network calls
    private val localMoviesDataSource: ILocalMoviesDataSource,
    private val database: AppDatabase
) : RemoteMediator<Int, MovieEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        return try {
            // 1) Decide which page to load
            val pageToLoad = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    // Find last loaded entity
                    val lastItem = state.pages
                        .lastOrNull { it.data.isNotEmpty() }
                        ?.data
                        ?.lastOrNull()

                    if (lastItem == null) {
                        // No items in DB yet ⇒ nothing to append
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    // Get its remote keys
                    val remoteKeys = localMoviesDataSource.getRemoteKeysByMovieId(lastItem.id)
                    remoteKeys?.nextPage ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            // 2) Fetch from network
            val response = remoteDataSource.getMovies(page = pageToLoad , pageSize = 20)
            val movies = response?.results
            val isEnd = (response?.page ?: 1) >= (response?.totalPages ?: 1)

            // 3) Save to Room in one transaction
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    localMoviesDataSource.clearAllMovies()
                    localMoviesDataSource.clearAllRemoteKeys()
                }

                // Map DTO → Entity (capturing page)
                val entities = movies?.map { dto ->
                    dto.toEntity(page = pageToLoad)
                }
                entities?.let {  localMoviesDataSource.insertMovies(it)  }

                // Build and insert new RemoteKeys
                val keys = movies?.map { dto ->
                    MovieRemoteKeys(
                        movieId = dto.id,
                        prevPage = if (pageToLoad == 1) null else pageToLoad - 1,
                        nextPage = if (isEnd) null else pageToLoad + 1
                    )
                }
                keys?.let { localMoviesDataSource.insertRemoteKeys(it) }

            }

            MediatorResult.Success(endOfPaginationReached = isEnd)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
//    fun getRefreshKey(state: PagingState<Int, MovieEntity>): Int? {
//        // Standard implementation: use anchor position to find closest page
//        return state.anchorPosition?.let { pos ->
//            state.closestPageToPosition(pos)?.prevKey?.plus(1)
//                ?: state.closestPageToPosition(pos)?.nextKey?.minus(1)
//        }
//    }

}
