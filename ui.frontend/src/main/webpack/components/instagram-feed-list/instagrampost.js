var jQuery = require("jquery");

jQuery(function ($) {
    "use strict";

    let visible = false;

    /**
     * Handle clicking of the Sign In button
     */
    $('body').on('click', '[data-ig-modal-url]', showModal);


    /**
     * Handle clicking "off-modal" to hide it
     */
    $(document).on('click', (hideModal));

    /**
     * Handle esc "off-modal" to hide it
     */
    $(document).on('keyup',function(evt) {
        if (evt.keyCode == 27) {
            hideModal(evt);
        }
    });

    function showModal(e) {
        e.preventDefault();

        const xfUrl = $(this).data('ig-modal-url');
        const typeName = $(this).data('type-name');
        const displayUrl = $(this).data('display-url');
        const sidecarToChildren = $(this).data('sidecar-to-children');
        const mediaSection = typeName == 'GraphImage' ? createGraphImage(displayUrl) : createSidecar(displayUrl, sidecarToChildren);
        const caption = $(this).data('media-to-caption') || {};

        //if (visible || !xfUrl) { return; }
        if (visible) { return; }
        const showModalEvt = new Event('wknd-ig-modal-show');
        const body = document.querySelector('body');

        const modal = $('<div id="wknd-ig-modal"/>')
            .append(createHeaderModal())
            .append(createBodyWrapper(mediaSection,
                createModalBody($(this).data('owner'), caption)));

        $(modal).ready(function (){
            $('#wknd-modal-close-btn').on('click', hideModal)
        })
        $('body').append(modal);

        modal.fadeIn(300, function() { visible = true; });
        visible = true;
        // dispatch event to indicate that the modal has been shown
        // used by sign-in-form.js to dynamically update a successful sign-in redirect to the current page
        body.dispatchEvent(showModalEvt);

        /*$.get(xfUrl, function (data) {
            const modal = $('<div id="wknd-ig-modal"/>');

            $('body').apui.frontend/src/main/webpack/components/instagram-feed-list/instagrampost.jspend(modal.append(data));
            modal.fadeIn(300, function() { visible = true; });
            visible = true;
            // dispatch event to indicate that the modal has been shown
            // used by sign-in-form.js to dynamically update a successful sign-in redirect to the current page
            body.dispatchEvent(showModalEvt);
        });*/

        return false;
    }

    function createModalBody(owner, caption){
        const modalBody  = $('<div class="cpm-instagram-modal__body" />');
        const ownerSection = $('<div class="cpm-instagram-modal__profile" />')
            .append($('<img class="profile-picture" />').attr("src", owner.profile_pic_url))
            .append($('<div class="profile-username" />').text(owner.username));

        let captionSection;
        const edges = caption.edges;
        if(edges && edges.length > 0){
            captionSection = $('<div class="cpm-instagram-modal__body-caption" />')
                .text(edges[0].node.text);
        }
        if(captionSection){
            return $(modalBody).append(ownerSection).append(captionSection);
        }
        return $(modalBody).append(ownerSection);
    }

    function createCaption(edges){
        if(edges && edges.length > 0){
            return $('<div class="cpm-instagram-modal_caption"/>').text(edges[0].node.text);
        }
        return null;
    }

    function createBodyWrapper(media, body){
        const wrapper = $('<div class="cpm-instagram-modal__body-wrapper"/>')
            .append(media).append(body);
        return wrapper;
    }

    function createGraphImage(displayUrl) {
        const container = $('<div class="cpm-instagram-modal__media"/>');
        const image = $('<img class="cpm-instagram-modal__media-image">')
            .attr("src", displayUrl);
        return container.append(image);
    }

    function createSidecar(displayUrl, sidecarMedia) {
        const container = $('<div class="cpm-instagram-modal__media"/>');
        const image = $('<img class="cpm-instagram-modal__media-image">')
            .attr("src", displayUrl);
        return container.append(image);
    }

    function createHeaderModal(){
        return $('<div class="cpm-instagram-modal__header" />')
            .append($('<div class="cpm-instagram-modal__close" />')
                .append($('<span id="wknd-modal-close-btn" class="close">x</span>')));
    }

    function hideModal(e) {
        const modal = $('#wknd-ig-modal');
        // if the target of the click isn't the modal nor a descendant of the modal
        if (visible && modal && !modal.is(e.target) && (modal.has(e.target).length === 0 ||
            (modal.has(e.target).length === 1 && $(e.target).attr('id') === 'wknd-modal-close-btn'))) {

            e.preventDefault();

            modal.fadeOut(200, function(){
                modal.remove();
                visible = false;
            });

            return false;
        }

    }

});