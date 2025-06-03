package com.trianglz.movies.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.trianglz.movies.models.MovieDomainModel
import com.trianglz.movies.models.mappers.toDomain
import com.trianglz.movies.remoteDataSource.IMoviesRemoteDataSource

class SearchMoviesPagingSource(
    private val remoteMoviesDataSource: IMoviesRemoteDataSource,
    private val query: String
) : PagingSource<Int, MovieDomainModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDomainModel> {
        return try {
            val page = params.key ?: 1
            val response = remoteMoviesDataSource.searchForMovies(query = query, page = page)
            LoadResult.Page(
                data = response?.results?.map { it.toDomain() }?:emptyList(),
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response?.results?.isEmpty() == true) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieDomainModel>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }
}
