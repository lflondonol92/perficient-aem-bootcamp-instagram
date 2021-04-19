var jQuery = require("jquery");

jQuery(function ($) {
    "use strict";
    /*function pullIgPost(){
        const SELECTORS = {
            IG_COMPONENT : ".cmp-instagram-post",
        };
        const token = "EAAC8ZCVnhe18BAPaip5EAcQn5XZAkOZB4IbwZCjBSQlXxm0fmsPi1yZCCVDAnRQqVou66BlO8Hhanhb2ZAzKWyzK3TvnaR4CyThqQScKHA70ZAsgPoOd5lNeGSHOnpTSDNZBqTXG7CgFk92f0TjZAOxzfs63gZAnErZAgVETWL2mvZBoCZApgV049uKlW1WahaoZC9Y0EYdxl2yHCf20wcXeZBoBQZCKmaPz0SXduycobFt5W873keP5PaVpzASSfDXOotuMm3AZD";
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
    }*/

    $( document ).ready(function() {
        console.log('ready...');
    });

});