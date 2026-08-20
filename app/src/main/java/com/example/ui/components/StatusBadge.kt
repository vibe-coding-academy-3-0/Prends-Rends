package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LoanItem
import com.example.data.LoanType
import com.example.ui.theme.StatusBlue
import com.example.ui.theme.StatusBlueBorder
import com.example.ui.theme.StatusBlueContainer
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusGreenBorder
import com.example.ui.theme.StatusGreenContainer
import com.example.ui.theme.StatusOrange
import com.example.ui.theme.StatusOrangeBorder
import com.example.ui.theme.StatusOrangeContainer
import com.example.ui.theme.StatusRed
import com.example.ui.theme.StatusRedBorder
import com.example.ui.theme.StatusRedContainer

@Composable
fun LoanStatusBadge(loan: LoanItem, modifier: Modifier = Modifier) {
    val (backgroundColor, textColor, borderColor, icon, text) = when {
        loan.isReturned -> Quintuple(
            StatusGreenContainer,
            StatusGreen,
            StatusGreenBorder,
            Icons.Default.CheckCircle,
            "Rendu"
        )
        loan.isOverdue -> Quintuple(
            StatusRedContainer,
            StatusRed,
            StatusRedBorder,
            Icons.Default.Warning,
            "En retard"
        )
        else -> Quintuple(
            StatusOrangeContainer,
            StatusOrange,
            StatusOrangeBorder,
            Icons.Default.HourglassTop,
            "En cours"
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = textColor,
            modifier = Modifier.size(13.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun LoanTypeBadge(type: LoanType, modifier: Modifier = Modifier) {
    val isLent = type == LoanType.LENT
    val backgroundColor = if (isLent) StatusGreenContainer else StatusBlueContainer
    val textColor = if (isLent) StatusGreen else StatusBlue
    val borderColor = if (isLent) StatusGreenBorder else StatusBlueBorder
    val text = if (isLent) "Prêté" else "Emprunté"
    val icon = if (isLent) Icons.Default.NorthEast else Icons.Default.SouthWest

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = textColor,
            modifier = Modifier.size(13.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private data class Quintuple<A, B, C, D, E>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E
)

