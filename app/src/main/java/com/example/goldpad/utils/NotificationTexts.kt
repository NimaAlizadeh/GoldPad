package com.example.goldpad.utils

enum class NotificationTexts(val template: String) {
    PHASE_1("درخواست شما برای %s %d گرم طلا از سمت ادمین به مقدار %,.0f هزار تومان مورد بررسی قرار گرفته است. آیا تایید میکنید که این معامله انجام شود؟"),
    PHASE_2("معامله شما با موفقیت ثبت شد."),
    PHASE_0("درخواست شما رد شده است.")
}
