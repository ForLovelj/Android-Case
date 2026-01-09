package com.clow.customviewcase.data

/**
 * Created by clow
 * Des:
 * Date: 2026/1/9.
 */
data class TaskMapNode(
    val id: Int,
    val title: String,
    var isCompleted: Boolean, // 是否已完成
    var isCurrent: Boolean = false, // 是否是当前学习的节点
    var x: Float = 0f, // 节点在屏幕上的X坐标
    var y: Float = 0f // 节点在屏幕上的Y坐标
)
