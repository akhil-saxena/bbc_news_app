package com.wmhack.navdrawer

data class APIResponse(val data: Data)


data class Data(
    val event_type: String,
    val event_name: String,
    val img_url: String,
    val lp_url: String,
    val adTitle: String,
    val adSubtitle: String,
    val adDescription: String,
    val ctaText: String,
    val backgroundColor: String,
    val textDefault: String,
    val textHover: String,
    val textFocus: String,
    val buttonDefault: String,
    val buttonHover: String,
    val buttonFocus: String
)
