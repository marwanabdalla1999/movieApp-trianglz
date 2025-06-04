package com.trianglz.movies.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.trianglz.cache.movies.MovieRemoteKeysDao
import com.trianglz.cache.movies.MoviesDao
import com.trianglz.cache.movies.entities.MovieEntity
import com.trianglz.cache.movies.entities.MovieRemoteKeys
import com.trianglz.corenetwork.NetworkConstants.Paging.PAGE_SIZE
import com.trianglz.movies.models.mappers.toEntity
import com.trianglz.movies.remoteDataSource.IMoviesRemoteDataSource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MoviesMediator @Inject constructor(
    private val remoteDataSource: IMoviesRemoteDataSource,
    private val moviesDao: MoviesDao,
    private val remoteKeysDao: MovieRemoteKeysDao,
    private val withTransaction: suspend (suspend () -> Unit) -> Unit
) : RemoteMediator<Int, MovieEntity>() {


    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        return try {
            val pageToLoad = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem =
                        state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()

                    if (lastItem == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    val remoteKeys = remoteKeysDao.remoteKeysByMovieId(lastItem.id)
                    remoteKeys?.nextPage
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val response = remoteDataSource.getMovies(page = pageToLoad, pageSize = PAGE_SIZE)

            val movies = response?.results
            val isEnd = (response?.page ?: 1) >= (response?.totalPages ?: 1)

           withTransaction {
                if (loadType == LoadType.REFRESH) {
                    moviesDao.clearAllMovies()
                    remoteKeysDao.clearAllRemoteKeys()
                }

                val entities = movies?.map { dto ->
                    dto.toEntity()
                }
                entities?.let { moviesDao.insertAll(it) }

                val keys = movies?.map { dto ->
                    MovieRemoteKeys(
                        movieId = dto.id,
                        prevPage = if (pageToLoad == 1) null else pageToLoad - 1,
                        nextPage = if (isEnd) null else pageToLoad + 1
                    )
                }
                keys?.let { remoteKeysDao.insertAll(it) }

            }

            MediatorResult.Success(endOfPaginationReached = isEnd)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}
