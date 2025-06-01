package com.trianglz.ui.commonUi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core_ui.R
import com.core_ui.models.githubRepositoriesModels.AppRepositoriesModel
import com.trianglz.ui.uiModels.AppMoviesModel

@Composable
fun MovieCard(repo: AppMoviesModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row (verticalAlignment = Alignment.CenterVertically){
                AsyncImageView(imageUrl = repo.repositoryOwnerAvatarUrl,modifier = Modifier.padding(end = 10.dp))
                Text(repo.repositoryOwnerName, style = MaterialTheme.typography.titleMedium)
            }
            Text(repo.repositoryName, style = MaterialTheme.typography.titleMedium)
            Text("⭐ ${repo.starsCount}", style = MaterialTheme.typography.bodyMedium)
            Text(repo.description, style = MaterialTheme.typography.bodySmall)
            repo.language?.let {
                Text(stringResource(R.string.language, it), style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RepoItemPreview() {
    RepoItem(
        AppRepositoriesModel(
            repositoryName = "JetBrains/compose-multiplatform",
            repositoryOwnerName = "JetBrains",
            repositoryOwnerAvatarUrl = " ",
            description = "A modern declarative UI framework",
            language = "Kotlin",
            starsCount = 999
        )
    )
}
