package com.hyejineee.hwahae

class Action(val type:ActionType, val data: Any)
enum class ActionType{
    FILTERING, NOT_FILTERING, NEXT_PAGE, SEARCH, REFRESH
}