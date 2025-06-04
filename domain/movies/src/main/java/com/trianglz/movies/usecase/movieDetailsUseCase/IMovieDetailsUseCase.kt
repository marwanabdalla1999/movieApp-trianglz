package com.trianglz.movies.usecase.movieDetailsUseCase

import androidx.paging.PagingData
import com.trianglz.movies.models.MovieDomainModel
import kotlinx.coroutines.flow.Flow

interface IMovieDetailsUseCase {

    suspend operator fun invoke(movieId:Int): Flow<MovieDomainModel>

}