package com.application.newsly

object ColorPicker {
    val color= arrayOf("#FFA07A","#E9967A","#CD5C5C","#F08080","#40E0D0","#9FE2BF")
    var colorIndex=1
    fun getColor():String{
        return color[colorIndex++ % color.size]
    }
}