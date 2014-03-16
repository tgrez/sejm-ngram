function buttonGenVizPressed() {
    var term=$("#inputNgram").val().toLowerCase().replace(/[ ]+$/,"")
        .replace(/^[ ]+/,"");
    visualize(term);
    }
