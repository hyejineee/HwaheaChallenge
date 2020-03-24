package com.hyejineee.hwahae

class Action(val type:ActionType, val data: Any)
enum class ActionType{
    FILTERING, NEXT_PAGE, SEARCH, DEFAULT_PRODUCTS
}