/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package net.waterfox.android.compose.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mozilla.components.lib.state.ext.observeAsComposableState
import net.waterfox.android.R
import net.waterfox.android.components.components
import net.waterfox.android.compose.inComposePreview
import net.waterfox.android.theme.WaterfoxTheme
import net.waterfox.android.theme.Theme
import net.waterfox.android.wallpapers.Wallpaper

/**
 * Homepage header.
 *
 * @param headerText The header string.
 * @param description The content description for the "Show all" button.
 * @param onShowAllClick Invoked when "Show all" button is clicked.
 */
@Composable
fun HomeSectionHeader(
    headerText: String,
    description: String = "",
    onShowAllClick: (() -> Unit)? = null,
) {
    if (inComposePreview) {
        HomeSectionHeaderContent(
            headerText = headerText,
            description = description,
            onShowAllClick = onShowAllClick,
        )
    } else {
        val wallpaperState = components.appStore
            .observeAsComposableState { state -> state.wallpaperState }.value

        // TODO: [Waterfox] Do we want to bring in wallpaper related changes?
        val wallpaperAdaptedTextColor = null // wallpaperState?.currentWallpaper?.textColor?.let { Color(it) }

        val isWallpaperDefault =
            (wallpaperState?.currentWallpaper ?: Wallpaper.Default) == Wallpaper.Default

        HomeSectionHeaderContent(
            headerText = headerText,
            textColor = wallpaperAdaptedTextColor ?: WaterfoxTheme.colors.textPrimary,
            description = description,
            showAllTextColor = if (isWallpaperDefault) {
                WaterfoxTheme.colors.textAccent
            } else {
                wallpaperAdaptedTextColor ?: WaterfoxTheme.colors.textAccent
            },
            onShowAllClick = onShowAllClick,
        )
    }
}

/**
 * Homepage header content.
 *
 * @param headerText The header string.
 * @param description The content description for the "Show all" button.
 * @param showAllTextColor [Color] for the "Show all" button.
 * @param onShowAllClick Invoked when "Show all" button is clicked.
 */
@Composable
private fun HomeSectionHeaderContent(
    headerText: String,
    textColor: Color = WaterfoxTheme.colors.textPrimary,
    description: String = "",
    showAllTextColor: Color = WaterfoxTheme.colors.textAccent,
    onShowAllClick: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = headerText,
            modifier = Modifier
                .weight(1f)
                .wrapContentHeight(align = Alignment.Top),
            color = textColor,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            style = WaterfoxTheme.typography.headline6,
        )

        onShowAllClick?.let {
            ClickableText(
                text = AnnotatedString(text = stringResource(id = R.string.recent_tabs_show_all)),
                modifier = Modifier.padding(start = 16.dp)
                    .semantics {
                        contentDescription = description
                    },
                style = TextStyle(
                    color = showAllTextColor,
                    fontSize = 14.sp,
                ),
                onClick = { onShowAllClick() },
            )
        }
    }
}

@Composable
@Preview
private fun HomeSectionsHeaderPreview() {
    WaterfoxTheme(theme = Theme.getTheme()) {
        HomeSectionHeader(
            headerText = stringResource(R.string.recently_saved_title),
            description = stringResource(R.string.recently_saved_show_all_content_description_2),
        )
    }
}
