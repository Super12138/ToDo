package cn.super12138.todo.ui.pages.settings.components.licence

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.ui.compose.LibraryColors
import com.mikepenz.aboutlibraries.ui.compose.LibraryDefaults
import com.mikepenz.aboutlibraries.ui.compose.LibraryPadding
import com.mikepenz.aboutlibraries.ui.compose.m3.component.LibraryChip
import com.mikepenz.aboutlibraries.ui.compose.m3.libraryColors
import com.mikepenz.aboutlibraries.ui.compose.util.author

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LicenceItem(
    modifier: Modifier = Modifier,
    library: Library,
    showAuthor: Boolean = true,
    showVersion: Boolean = true,
    showLicenseBadges: Boolean = true,
    colors: LibraryColors = LibraryDefaults.libraryColors(),
    padding: LibraryPadding = LibraryDefaults.libraryPadding(),
    libraryPadding: LibraryPadding = LibraryDefaults.libraryPadding(),
    typography: Typography = MaterialTheme.typography,
    shape: Shape = MaterialTheme.shapes.large,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable { onClick.invoke() }
            .padding(libraryPadding.contentPadding)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = library.name,
                modifier = Modifier
                    .padding(padding.namePadding)
                    .weight(1f),
                style = typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            val version = library.artifactVersion
            if (version != null && showVersion) {
                Text(
                    version,
                    modifier = Modifier.padding(padding.versionPadding.contentPadding),
                    style = typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
        val author = library.author
        if (showAuthor && author.isNotBlank()) {
            Text(
                text = author,
                style = typography.bodyMedium
            )
        }
        if (showLicenseBadges && library.licenses.isNotEmpty()) {
            FlowRow {
                library.licenses.forEach {
                    LibraryChip(
                        modifier = Modifier.padding(padding.licensePadding.containerPadding),
                        minHeight = LibraryDefaults.libraryDimensions().chipMinHeight,
                        containerColor = colors.licenseChipColors.containerColor,
                        contentColor = colors.licenseChipColors.contentColor,
                        shape = LibraryDefaults.libraryShapes().chipShape,
                    ) {
                        Text(
                            modifier = Modifier.padding(padding.licensePadding.contentPadding),
                            maxLines = 1,
                            text = it.name,
                            // style = MaterialTheme.typography.labelSmall,
                            textAlign = TextAlign.Center,
                            overflow = LibraryDefaults.libraryTextStyles().defaultOverflow,
                        )
                    }
                }
            }
        }
    }
}