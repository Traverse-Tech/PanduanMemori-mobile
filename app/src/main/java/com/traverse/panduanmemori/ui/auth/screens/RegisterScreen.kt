package com.traverse.panduanmemori.ui.auth.screens

import android.Manifest
import android.location.Geocoder
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
//import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
//import com.google.accompanist.permissions.ExperimentalPermissionsApi
//import com.google.accompanist.permissions.PermissionState
//import com.google.accompanist.permissions.isGranted
//import com.google.accompanist.permissions.rememberPermissionState
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.MarkerOptions
import com.traverse.panduanmemori.R
import com.traverse.panduanmemori.data.models.Gender
import com.traverse.panduanmemori.data.models.UserRole
import com.traverse.panduanmemori.data.repositories.ApiState
import com.traverse.panduanmemori.ui.auth.AuthViewModel
import com.traverse.panduanmemori.ui.auth.AuthenticatedState
import com.traverse.panduanmemori.ui.auth.LOGIN_SCREEN
import com.traverse.panduanmemori.ui.auth.PATIENT_PROFILE_SCREEN
import com.traverse.panduanmemori.ui.components.elements.*
import com.traverse.panduanmemori.ui.components.themes.AppColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit
) {
    val userRole = viewModel.getUserRole()
    val userRoleDescription = if (userRole == UserRole.PATIENT) "Pasien Demensia" else "Caregiver"

    var fullName by remember { mutableStateOf("") }
    var identifier by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf(Gender.MAN) }
    var selectedLocation by remember { mutableStateOf("") }

    val registerState by viewModel.registerState.collectAsState()
    val authenticatedState by viewModel.authenticatedState.collectAsState()

    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
//    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    var toastMessage by remember { mutableStateOf<String?>(null) }
    var toastDescription by remember { mutableStateOf<String?>(null) }
    var toastVisible by remember { mutableStateOf(false) }

    LaunchedEffect(registerState) {
        when (registerState) {
            is ApiState.Success -> {
                if (authenticatedState == AuthenticatedState.Unassigned)
                    navController.navigate(PATIENT_PROFILE_SCREEN)
                else
                    onRegisterSuccess()
            }
            is ApiState.Error -> {
                val errorState = registerState as ApiState.Error
                toastMessage = errorState.message
                toastDescription = errorState.description
                toastVisible = true
                viewModel.setRegisterState(ApiState.Idle)
            }
            else -> {}
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
            Column(modifier = Modifier.padding(16.dp)) {
//                if (locationPermissionState.status.isGranted) {
//                    GoogleMapView(onLocationSelected = { location ->
//                        selectedLocation = location
//                    })
//                } else {
//                    locationPermissionState.launchPermissionRequest()
//                }
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .zIndex(20f)
            ) {
                AppButton(
                    onClick = { navController.popBackStack() },
                    icon = Icons.Filled.Close,
                    iconPosition = ButtonIconPosition.ONLY,
                    modifier = Modifier.align(Alignment.TopEnd),
                    type = if (userRole == UserRole.PATIENT) ButtonType.CYAN else ButtonType.PRIMARY,
                    size = ButtonSize.SMALL,
                    variant = ButtonVariant.SECONDARY
                )
            }

            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxHeight(),
                contentPadding = PaddingValues(bottom = 120.dp)
            ) {
                item {
                    AppTag(
                        text = userRoleDescription,
                        icon = ImageVector.vectorResource(id = if (userRole == UserRole.PATIENT) R.drawable.ic_elder else R.drawable.ic_health_care),
                        type = TagType.OUTLINED,
                        variant = if (userRole == UserRole.CAREGIVER) TagVariant.INFO else TagVariant.PRIMARY
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Selamat Datang!",
                        style = MaterialTheme.typography.h1
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Isi data akun untuk ${userRoleDescription.lowercase()}",
                        style = MaterialTheme.typography.body1
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))

                    AppTextInput(
                        value = fullName,
                        onValueChange = { fullName = it },
                        title = "Nama Lengkap",
                        placeholder = "Nana Sutarna",
                        leftIcon = Icons.Filled.Person,
                        leftIconColor = if (userRole == UserRole.CAREGIVER) AppColors.Secondary.`200` else AppColors.Primary.`200`
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AppTextInput(
                        value = identifier,
                        onValueChange = { identifier = it },
                        title = "NIK / Email",
                        placeholder = "Isi antara NIK atau Email",
                        leftIcon = Icons.Filled.Email,
                        leftIconColor = if (userRole == UserRole.CAREGIVER) AppColors.Secondary.`200` else AppColors.Primary.`200`
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AppTextInput(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        title = "No HP",
                        placeholder = "812919291",
                        leftIcon = Icons.Filled.Phone,
                        leftIconColor = if (userRole == UserRole.CAREGIVER) AppColors.Secondary.`200` else AppColors.Primary.`200`,
                        type = KeyboardType.Phone
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AppTextInput(
                        value = password,
                        onValueChange = { password = it },
                        title = "Password",
                        placeholder = "Isi password",
                        leftIcon = Icons.Filled.Lock,
                        leftIconColor = if (userRole == UserRole.CAREGIVER) AppColors.Secondary.`200` else AppColors.Primary.`200`,
                        type = KeyboardType.Password
                    )

                    if (userRole == UserRole.PATIENT) {
                        Spacer(modifier = Modifier.height(12.dp))

                        AppDateInput(
                            value = birthdate,
                            onValueChange = { birthdate = it },
                            title = "Tanggal Lahir",
                            placeholder = "dd/MM/yyyy",
                            iconColor = if (userRole == UserRole.CAREGIVER) AppColors.Secondary.`200` else AppColors.Primary.`200`,
                            type = DateInputType.BIRTHDATE
                        )
                    }

//                    Spacer(modifier = Modifier.height(12.dp))
//
//                    AppButton(
//                        text = "Select Location",
//                        onClick = {
//                            scope.launch {
//                                bottomSheetState.show()
//                            }
//                        }
//                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (userRole == UserRole.PATIENT) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Jenis Kelamin",
                                style = MaterialTheme.typography.caption,
                                fontWeight = FontWeight.Bold,
                                color = AppColors.Neutral.`70`
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .border(
                                            1.dp,
                                            AppColors.Neutral.`50`,
                                            RoundedCornerShape(
                                                topStart = 16.dp,
                                                bottomStart = 16.dp
                                            )
                                        )
                                        .background(
                                            if (gender == Gender.MAN) AppColors.Neutral.`10` else AppColors.Primary.Main,
                                            RoundedCornerShape(
                                                topStart = 16.dp,
                                                bottomStart = 16.dp
                                            )
                                        )
                                        .clickable {
                                            gender = Gender.MAN
                                        }
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Laki-laki",
                                        style = MaterialTheme.typography.body1,
                                        color = if (gender == Gender.MAN) Color.Black else AppColors.Neutral.`10`
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .border(
                                            1.dp,
                                            AppColors.Neutral.`50`,
                                            RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
                                        )
                                        .background(
                                            if (gender == Gender.WOMAN) AppColors.Neutral.`10` else AppColors.Primary.Main,
                                            RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
                                        )
                                        .padding(8.dp)
                                        .clickable {
                                            gender = Gender.WOMAN
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Perempuan",
                                        style = MaterialTheme.typography.body1,
                                        color = if (gender == Gender.WOMAN) Color.Black else AppColors.Neutral.`10`
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .shadow(2.dp, RoundedCornerShape(8.dp))
                    .background(
                        AppColors.Neutral.`10`,
                        RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                    )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    AppButton(
                        onClick = {
                            viewModel.register(
                                fullName,
                                identifier,
                                phoneNumber,
                                password,
                                birthdate,
                                gender
                            )
                        },
                        text = "Registrasi",
                        size = ButtonSize.LARGE,
                        type = if (userRole == UserRole.PATIENT) ButtonType.CYAN else ButtonType.PRIMARY,
                        modifier = Modifier.fillMaxWidth(),
                        disabled = if (userRole == UserRole.PATIENT) {
                            fullName.isEmpty() || identifier.isEmpty() || phoneNumber.isEmpty() || password.isEmpty() || birthdate.isEmpty()
                        } else {
                            fullName.isEmpty() || identifier.isEmpty() || phoneNumber.isEmpty() || password.isEmpty()
                        },
                        isLoading = registerState == ApiState.Loading
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Sudah punya akun?",
                                style = MaterialTheme.typography.body1
                            )
                            AppButton(
                                text = "Masuk di sini",
                                onClick = { navController.navigate(LOGIN_SCREEN) },
                                variant = ButtonVariant.TEXT_ONLY,
                                type = if (userRole == UserRole.CAREGIVER) ButtonType.PRIMARY else ButtonType.CYAN,
                                size = ButtonSize.SMALL,
                                textDecoration = TextDecoration.Underline
                            )
                        }
                    }
                }
            }
        }
    }
}

//@Composable
//fun GoogleMapView(
//    onLocationSelected: (String) -> Unit
//) {
//    AndroidView(
//        modifier = Modifier.fillMaxSize(),
//        factory = { context ->
//            val mapFragment = SupportMapFragment.newInstance()
//
//            (context as FragmentActivity).supportFragmentManager.beginTransaction()
////                .replace(R.id.map_container, mapFragment) // Make sure you replace the fragment if needed
//                .commit()
//
//            // Wait until the map is ready before accessing it
//            mapFragment.getMapAsync { googleMap ->
//                googleMap.setOnMapClickListener { latLng ->
//                    googleMap.clear() // Clear previous markers
//                    googleMap.addMarker(MarkerOptions().position(latLng).title("Selected Location"))
//
//                    // Use Geocoder to fetch address from LatLng
//                    val geocoder = Geocoder(context)
//                    val address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
//                    if (address != null) {
//                        if (address.isNotEmpty()) {
//                            onLocationSelected(address[0].getAddressLine(0) ?: "Unknown Location")
//                        }
//                    }
//                }
//            }
//
//            // Null check for mapFragment.view to ensure it's not null
//            mapFragment.view ?: throw IllegalStateException("Map Fragment's view is null")
//        }
//    )
//}
