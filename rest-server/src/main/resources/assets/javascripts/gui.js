function buttonGenVizPressed() {
    var diacriticsRemover = new DiacriticsRemover();

    showSpinner();

    var term=$("#inputNgram").val().toLowerCase().replace(/[ ]+$/,"")
        .replace(/^[ ]+/,"");
    term = diacriticsRemover.removeDiacritics(term);

    visualize(term);

    function showSpinner() {
        var generateButton = $('#generate');
        var generateSpinner = $('#generate-spinner');
        generateButton.hide();
        generateSpinner.show();
    }
}
