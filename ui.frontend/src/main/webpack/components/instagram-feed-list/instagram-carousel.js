instagramCarousel = {

    SIDECAR_SELECTOR : "cpm-instagram-modal__media-image",
    slideIndex: 1,

    init : function () {
        this.slideIndex = 1;
        this.showSlides(this.slideIndex);
    },

    plusSlides: function (n) {
        this.showSlides(this.slideIndex += n);
    },

    showSlides: function (n) {
        let i;
        let x = document.getElementsByClassName(this.SIDECAR_SELECTOR);
        if (n > x.length) {this.slideIndex = 1}
        if (n < 1) {this.slideIndex = x.length}

        for (i = 0; i < x.length; i++) {

            x[i].classList.add("hide");
            x[i].classList.remove("show");
        }

        x[this.slideIndex-1].classList.remove("hide");
        x[this.slideIndex-1].classList.add("show");
    }

};
