var jQuery = require("jquery");

jQuery(function ($) {
    "use strict";
    function pullIgPost(){
        const SELECTORS = {
            IG_COMPONENT : ".cmp-instagram-post",
        };
        const token = "EAAC8ZCVnhe18BAEGZB32oXHABFuTpdOKTnIyTAVN3koYkI3XMMBRQATsIoLu01KIw5MOQk9bYGIAceq74qgKX5qzPxAVD9y75b8Dx0K3K6J1CXDrPxKsI8VYfR9HshxUh1JxFvsA6SfVjoRWH5KIMcZBuZBmxAo9hF2aZBqOJZAoO3N5sQgBQKDxzBCRCO1QqHDdU3cidgrA1h2tTbDGho";
        $(SELECTORS.IG_COMPONENT).each((_, item) => {
            if($(item).data()){
                const url = $(item).data("url");
                const endpoint = "https://graph.facebook.com/v10.0/instagram_oembed";

                $.get(endpoint, {access_token: token, debug: "all", format: "json", pretty:0, suppress_http_code: 1,
                                  transport: "cors", url: url} )
                    .done(function(data) {
                        //console.log(data.author_name);
                        if(data){
                            $(item).html(data.html);
                        }
                });
            }
        });
    }

    $( document ).ready(function() {
        pullIgPost();
    });

});