"use strict";

use(function ($) {
    var info = {};
    info.title = currentPage.getTitle() || currentPage.getName();

    return info;
});
