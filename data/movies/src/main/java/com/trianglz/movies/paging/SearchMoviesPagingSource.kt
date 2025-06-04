package com.trianglz.movies.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.trianglz.movies.models.MovieDomainModel
import com.trianglz.movies.models.mappers.toDomain
import com.trianglz.movies.remoteDataSource.IMoviesRemoteDataSource


/**
 * A [PagingSource] that loads paginated movie search results from a remote data source.
 *
 * This is used to support infinite scroll or pagination when searching for movies by a query string.
 *
 * @property remoteMoviesDataSource The remote data source that provides the search API.
 * @property query The search query to filter movies.
 */
class SearchMoviesPagingSource(
    private val remoteMoviesDataSource: IMoviesRemoteDataSource,
    private val query: String
) : PagingSource<Int, MovieDomainModel>() {


    /**
     * Loads a single page of search results from the API.
     *
     * @param params Parameters for the load request, including the page key and load size.
     * @return [LoadResult.Page] on success or [LoadResult.Error] on failure.
     */
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


    /**
     * Computes the key to use for the initial load (e.g., after refresh).
     * It tries to find the closest page to the anchor position and infer a good restart key.
     *
     * @param state The current paging state.
     * @return The key for the page to refresh.
     */
    override fun getRefreshKey(state: PagingState<Int, MovieDomainModel>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }
}
