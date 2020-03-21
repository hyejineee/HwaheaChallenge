package com.hyejineee.hwahae.datasource.ServerMessageCode

enum class BodyMessage(val message: String) {
    NO_DATA("ProductNotFound - 데이터가 비어있습니다!"),
    SKIN_TYPE_ERROR(
        "ValidateSkinTypeParameter - skin_type은 ['oily', 'dry', 'sensitive'] 요청만 가능합니다 !"
    ),
    PAGE_ERROR("PageUnderRequest - 페이지는 1부터 시작합니다!");
}
