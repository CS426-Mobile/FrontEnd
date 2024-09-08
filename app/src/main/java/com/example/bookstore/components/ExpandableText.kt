package com.example.bookstore.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun ExpandableText(
    text: String,
    minimizedMaxLines: Int = 4 // Số dòng tối đa khi thu gọn
) {
    var isExpanded by remember { mutableStateOf(false) } // Trạng thái mở rộng hoặc thu gọn

    Column(modifier = Modifier.padding(8.dp)) {
        // Văn bản hiển thị
        Text(
            text = text,
            maxLines = if (isExpanded) Int.MAX_VALUE else minimizedMaxLines, // Hiển thị toàn bộ hoặc giới hạn
            style = MaterialTheme.typography.body2,
            overflow = TextOverflow.Ellipsis // Cắt đoạn văn khi vượt quá số dòng
        )

        // Chỉ hiển thị nút "Read more" hoặc "Show less" khi văn bản vượt quá số dòng tối đa
        if (text.length > 200) { // Điều chỉnh độ dài theo ý muốn
            Text(
                text = if (isExpanded) "Show less" else "Read more",
                color = Color.Gray,
                modifier = Modifier
                    .clickable { isExpanded = !isExpanded }
                    .padding(top = 4.dp),
                style = MaterialTheme.typography.caption
            )
        }
    }
}