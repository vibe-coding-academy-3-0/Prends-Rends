package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PermMedia
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LoanType
import com.example.i18n.AppStrings
import com.example.media.MediaType
import com.example.ui.components.AudioPlayerCard
import com.example.ui.components.LanguageSelector
import com.example.ui.components.LoanStatusBadge
import com.example.ui.components.LoanTypeBadge
import com.example.ui.components.MediaGalleryCarousel
import com.example.ui.components.ThemeSelector
import com.example.viewmodel.LoanViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailLoanScreen(
    loanId: Long,
    viewModel: LoanViewModel,
    onBack: () -> Unit,
    onEditLoan: (Long) -> Unit
) {
    val context = LocalContext.current
    val currentLang by viewModel.currentLanguage.collectAsState()
    val currentThemeMode by viewModel.currentThemeMode.collectAsState()
    val loanState by viewModel.getLoanByIdFlow(loanId).collectAsState(initial = null)
    var showDeleteDialog by remember { mutableStateOf(false) }

    val loan = loanState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(AppStrings.detailTitle(currentLang), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        ThemeSelector(
                            currentThemeMode = currentThemeMode,
                            onThemeModeSelected = { viewModel.setThemeMode(it) }
                        )
                        LanguageSelector(
                            currentLanguage = currentLang,
                            onLanguageSelected = { viewModel.setLanguage(it) }
                        )
                    }
                    if (loan != null) {
                        IconButton(onClick = { onEditLoan(loan.id) }) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { innerPadding ->
        if (loan == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Prêt non trouvé ou supprimé.")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Main Info Header Card
                Card(
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LoanTypeBadge(type = loan.type)
                            LoanStatusBadge(loan = loan)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = loan.title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        val valCat = loan.valueOrCategory
                        if (!valCat.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = valCat,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Créé le", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(formatDate(loan.createdDate), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            }

                            if (loan.dueDate != null) {
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("Échéance de retour", fontSize = 11.sp, color = if (loan.isOverdue) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(12.dp), tint = if (loan.isOverdue) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(formatDateAndTime(loan.dueDate), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if (loan.isOverdue) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface)
                                    }
                                }
                            }
                        }
                    }
                }

                // Contact Section & Quick Shortcuts (Call, SMS, WhatsApp)
                Card(
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (loan.type == LoanType.LENT) "Prêté à" else "Emprunté à",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = loan.contactName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                val phone = loan.contactPhone
                                if (!phone.isNullOrBlank()) {
                                    Text(text = phone, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }

                        val phone = loan.contactPhone
                        if (!phone.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Call
                                OutlinedButton(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                                        context.startActivity(intent)
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("action_call_contact")
                                ) {
                                    Icon(imageVector = Icons.Default.Call, contentDescription = "Appeler", modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(AppStrings.callBtn(currentLang), fontSize = 11.sp)
                                }

                                // SMS
                                OutlinedButton(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$phone")).apply {
                                            val msg = if (loan.type == LoanType.LENT) {
                                                "Hello ${loan.contactName}, je te contacte au sujet de \"${loan.title}\" !"
                                            } else {
                                                "Hello ${loan.contactName}, concernant \"${loan.title}\" que tu m'as prêté !"
                                            }
                                            putExtra("sms_body", msg)
                                        }
                                        context.startActivity(intent)
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("action_sms_contact")
                                ) {
                                    Icon(imageVector = Icons.AutoMirrored.Filled.Message, contentDescription = "SMS", modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("SMS", fontSize = 11.sp)
                                }

                                // WhatsApp
                                OutlinedButton(
                                    onClick = {
                                        val cleanPhone = phone.replace("+", "").replace(" ", "")
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$cleanPhone"))
                                        try {
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "WhatsApp non installé", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("action_whatsapp_contact")
                                ) {
                                    Icon(imageVector = Icons.AutoMirrored.Filled.Chat, contentDescription = "WhatsApp", modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("WhatsApp", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }

                // Media Gallery Attachments (Photos / Videos)
                val allMedia = loan.mediaList.ifEmpty {
                    if (!loan.photoPath.isNullOrBlank()) {
                        listOf(com.example.media.MediaItem(filePath = loan.photoPath, type = MediaType.PHOTO))
                    } else emptyList()
                }

                if (allMedia.isNotEmpty()) {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.PermMedia, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(AppStrings.mediaHeader(currentLang), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            MediaGalleryCarousel(
                                mediaList = allMedia,
                                isEditable = false
                            )
                        }
                    }
                }

                // Voice Note Audio Player Section
                val audioPath = loan.audioPath
                if (!audioPath.isNullOrBlank() && File(audioPath).exists()) {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(AppStrings.voiceNoteHeader(currentLang), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(12.dp))
                            AudioPlayerCard(audioPath = audioPath, playerHelper = viewModel.playerHelper)
                        }
                    }
                }

                // Notes Section
                val notes = loan.notes
                if (!notes.isNullOrBlank()) {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(AppStrings.notesLabel(currentLang), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = notes, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Big Toggle Status Button
                Button(
                    onClick = { viewModel.toggleReturnedStatus(loan) },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (loan.isReturned) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("toggle_returned_detail_button")
                ) {
                    Icon(
                        imageVector = if (loan.isReturned) Icons.AutoMirrored.Filled.Undo else Icons.Default.CheckCircle,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (loan.isReturned) AppStrings.markActiveBtn(currentLang) else AppStrings.markReturnedBtn(currentLang),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog && loan != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(AppStrings.deleteConfirmTitle(currentLang)) },
            text = { Text(AppStrings.deleteConfirmDesc(currentLang, loan.title)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteLoan(loan) {
                            showDeleteDialog = false
                            onBack()
                        }
                    }
                ) {
                    Text("Supprimer", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(AppStrings.cancelBtn(currentLang))
                }
            }
        )
    }
}

private fun formatDate(timestampMillis: Long): String {
    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH)
    return sdf.format(Date(timestampMillis))
}

private fun formatDateAndTime(timestampMillis: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy à HH:mm", Locale.FRENCH)
    return sdf.format(Date(timestampMillis))
}
