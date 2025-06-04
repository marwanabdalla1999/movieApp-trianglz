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
import com.trianglz.movies.models.responses.MovieDto
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
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        return try {
            val page = getPageToLoad(loadType) ?: return MediatorResult.Success(true)

            val response = remoteDataSource.getMovies(page = page, pageSize = PAGE_SIZE)
            val movies = response?.results.orEmpty()
            val isEnd = (response?.page ?: 1) >= (response?.totalPages ?: 1)

            persistToDb(loadType, page, movies, isEnd)

            MediatorResult.Success(endOfPaginationReached = isEnd)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getPageToLoad(
        loadType: LoadType
    ): Int? {
        return when (loadType) {
            LoadType.REFRESH -> 1

            LoadType.PREPEND -> null

            LoadType.APPEND -> {
                val remoteKeys = remoteKeysDao.getLastRemoteKey()
                remoteKeys?.nextPage
            }
        }
    }

    private suspend fun persistToDb(
        loadType: LoadType,
        page: Int,
        movies: List<MovieDto>,
        isEnd: Boolean
    ) = withTransaction {
        if (loadType == LoadType.REFRESH) {
            moviesDao.clearAllMovies()
            remoteKeysDao.clearAllRemoteKeys()
        }

        val entities = movies.map { it.toEntity() }
        moviesDao.insertAll(entities)

        val keys = movies.map {
            MovieRemoteKeys(
                movieId = it.id,
                prevPage = if (page == 1) null else page - 1,
                nextPage = if (isEnd) null else page + 1
            )
        }
        remoteKeysDao.insertAll(keys)
    }
}

