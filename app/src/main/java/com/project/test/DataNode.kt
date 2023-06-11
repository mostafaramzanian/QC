package com.project.test

data class DataNode(
    val id:Int, val name: String, val parent:String, var children: MutableList<DataNode> = mutableListOf(),
    var space: Int = 0, var space1: Int = 0,
    val charNull: String =" ",
    val star: String ="* ")
