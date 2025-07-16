package us.docbee.docbeeapp.presentation.components.inputs.countrycodefield

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import docbee.composeapp.generated.resources.Res
import docbee.composeapp.generated.resources.ic_search
import docbee.composeapp.generated.resources.modal_country_code_search_not_found
import docbee.composeapp.generated.resources.modal_country_code_search_not_found_description
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import us.docbee.docbeeapp.domain.models.Country
import us.docbee.docbeeapp.presentation.components.inputs.InputSearchField
import us.docbee.docbeeapp.presentation.theme.Black100
import us.docbee.docbeeapp.presentation.theme.Gray
import us.docbee.docbeeapp.presentation.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountrySelectorPicker(
    isVisible: Boolean,
    searchValue: String,
    title: String,
    placeholder: String,
    countries: List<Country>,
    onSearchValueChange: (String) -> Unit,
    onCountrySelect: (Country) -> Unit,
    onDismiss: () -> Unit = { }
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                    onDismiss()
                }
            },
            dragHandle = null,
            sheetState = sheetState,
            shape = RoundedCornerShape(0.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
                    .background(color = White)
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(top = 24.dp)
                    .padding(horizontal = 32.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Black100,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(16.dp))
                InputSearchField(
                    value = searchValue,
                    placeholder = placeholder,
                    onValueChange = onSearchValueChange
                )
                if (countries.isNotEmpty()) {
                    CountryList(
                        modifier = Modifier.weight(1f),
                        countries = countries,
                        onClickItem = { country ->
                            scope.launch {
                                sheetState.hide()
                                onCountrySelect(country)
                            }
                        }
                    )
                } else {
                    EmptyCountryList(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun CountryList(
    modifier: Modifier = Modifier,
    countries: List<Country>,
    onClickItem: (Country) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(countries, key = { it.isoCode }) { country ->
            Row(
                modifier = Modifier.fillMaxWidth()
                    .clickable { onClickItem(country) }
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                AsyncImage(
                    model = Res.getUri(country.flagAssetPath),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    modifier = Modifier.weight(1f),
                    text = "${country.name} (${country.isoCode})",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Black100,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    modifier = Modifier.wrapContentWidth(),
                    text = "+${country.callingCode}",
                    style = MaterialTheme.typography.titleSmall,
                    color = Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
            }
        }
    }
}

@Composable
fun EmptyCountryList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            imageVector = vectorResource(Res.drawable.ic_search),
            contentDescription = null,
            tint = Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(Res.string.modal_country_code_search_not_found),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Black100
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.modal_country_code_search_not_found_description),
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            color = Gray
        )
    }
}