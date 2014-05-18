function buttonGenVizPressed() {
    showSpinner();

    var term=$("#inputNgram").val().toLowerCase().replace(/[ ]+$/,"")
        .replace(/^[ ]+/,"");
    visualize(term);

    function showSpinner() {
        var generateButton = $('#generate');
        var generateSpinner = $('#generate-spinner');
        generateButton.hide();
        generateSpinner.show();
    }
}
