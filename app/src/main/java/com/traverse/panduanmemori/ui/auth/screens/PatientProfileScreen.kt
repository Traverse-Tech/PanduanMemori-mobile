package com.traverse.panduanmemori.ui.auth.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.traverse.panduanmemori.ui.auth.AuthViewModel
import com.traverse.panduanmemori.ui.components.themes.AppColors
import com.traverse.panduanmemori.R
import com.traverse.panduanmemori.data.repositories.ApiState
import com.traverse.panduanmemori.ui.components.elements.*
import com.traverse.panduanmemori.utils.StringUtil
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PatientProfileScreen(
    viewModel: AuthViewModel,
    onAssignPatientSuccess: () -> Unit
) {
    var identifier by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val searchPatientState by viewModel.searchPatientState.collectAsState()
    val assignPatientState by viewModel.assignPatientState.collectAsState()

    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    var toastMessage by remember { mutableStateOf<String?>(null) }
    var toastDescription by remember { mutableStateOf<String?>(null) }
    var toastVisible by remember { mutableStateOf(false) }

    LaunchedEffect(searchPatientState, assignPatientState) {
        when (searchPatientState) {
            is ApiState.Success -> {
                bottomSheetState.show()
            }
            is ApiState.Error -> {
                val errorState = searchPatientState as ApiState.Error
                toastMessage = errorState.message
                toastDescription = errorState.description
                toastVisible = true
                viewModel.setSearchPatientState(ApiState.Idle)
            }
            else -> {}
        }

        when (assignPatientState) {
            is ApiState.Success -> {
                onAssignPatientSuccess()
            }

            is ApiState.Error -> {
                val errorState = assignPatientState as ApiState.Error
                toastMessage = errorState.message
                toastDescription = errorState.description
                toastVisible = true
                viewModel.setAssignPatientState(ApiState.Idle)
            }
        }
    }

    AppToast(
        message = toastMessage ?: "",
        description = toastDescription,
        variant = ToastVariant.DANGER,
        isVisible = toastVisible,
        onDismiss = { toastVisible = false }
    )

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.wrapContentSize(Alignment.Center)) {
                    Column(modifier = Modifier.wrapContentSize(Alignment.Center)) {
                        Text(
                            text = "Profil Pasien",
                            style = MaterialTheme.typography.subtitle1,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Berikut adalah data pasien yang akan kamu jaga, konfirmasi apabila sudah sesuai",
                            style = MaterialTheme.typography.body1,
                            color = AppColors.Neutral.`90`,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, AppColors.Neutral.`40`, RoundedCornerShape(8.dp))
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            val initialPatientName = viewModel.selectedPatient.value?.name?.firstOrNull()?.toString()?.uppercase() ?: ""
                            val randomColor = StringUtil.getRandomColorFromString(initialPatientName)
                            val textColor = if (randomColor.red + randomColor.green + randomColor.blue > 1.5f) AppColors.Neutral.`110` else AppColors.Neutral.`10`

                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(color = randomColor, shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = initialPatientName,
                                    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                                    color = textColor
                                )
                            }

                            Column {
                                Text(
                                    text = viewModel.selectedPatient.value?.name ?: "",
                                    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
                                )

                                Text(
                                    text = "${viewModel.selectedPatient.value?.age ?: "0"} tahun",
                                    style = MaterialTheme.typography.body1
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Column {
                        AppButton(
                            text = "Konfirmasi",
                            onClick = { viewModel.updateAssignedPatient(viewModel.selectedPatient.value?.id ?: "") },
                            size = ButtonSize.LARGE,
                            type = ButtonType.PRIMARY,
                            isLoading = searchPatientState == ApiState.Loading || assignPatientState == ApiState.Loading,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        AppButton(
                            text = "Batal",
                            onClick = {
                                scope.launch {
                                    bottomSheetState.hide()
                                }
                            },
                            size = ButtonSize.LARGE,
                            variant = ButtonVariant.SECONDARY,
                            type = ButtonType.PRIMARY,
                            isLoading = searchPatientState == ApiState.Loading  || assignPatientState == ApiState.Loading,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Profil Pasien",
                        style = MaterialTheme.typography.h1
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Masukkan NIK/email beserta password dari pasien yang akan kamu jaga",
                        style = MaterialTheme.typography.body1
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(AppColors.Secondary.`100`)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_info_circle) ,
                            contentDescription = "Information",
                            modifier = Modifier.size(24.dp),
                            tint = AppColors.Info.Main
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text ="Daftarkan terlebih dahulu pasien di perangkat pasien sebelum melanjutkan",
                            style = MaterialTheme.typography.caption,
                            color = AppColors.Secondary.Main
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Column {
                    AppTextInput(
                        value = identifier,
                        onValueChange = { identifier = it },
                        title = "NIK / Email",
                        placeholder = "Isi antara NIK atau Email pasien",
                        leftIcon = Icons.Filled.Email,
                        leftIconColor = AppColors.Secondary.`200`
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AppTextInput(
                        value = password,
                        onValueChange = { password = it },
                        title = "Password",
                        placeholder = "Isi password dari akun pasien",
                        leftIcon = Icons.Filled.Lock,
                        leftIconColor = AppColors.Secondary.`200`,
                        type = KeyboardType.Password
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                AppButton(
                    onClick = {
                        viewModel.searchPatientByCredential(identifier, password)
                    },
                    text = "Cari Pasien",
                    size = ButtonSize.LARGE,
                    type = ButtonType.PRIMARY,
                    modifier = Modifier.fillMaxWidth(),
                    disabled = identifier.isEmpty() || password.isEmpty(),
                    isLoading = searchPatientState == ApiState.Loading  || assignPatientState == ApiState.Loading
                )
            }
        }
    }
}
