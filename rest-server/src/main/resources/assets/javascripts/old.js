 function buttonGenVizPressed(){
                console.log("button Pressed");
                var ngram = document.getElementById("inputNgram").value;
                var dateFrom = document.getElementById("inputDateFrom").value;
                var dateTo = document.getElementById("inputDateTo").value;
                var xRes = document.getElementById("inputXRes").value;
                //startVisualization(ngram, dateFrom, dateTo, xRes);

                ngram = "Ala ma Kota";
                startJsonApiBasedVisualization(ngram, dateFrom, dateTo, xRes)
            }

            /* Handle "Draw Lines" CheckBox */
            function checkBoxDrawLinesPressed(chckBox){
                for(partylines in dataSets){
                    document.getElementById("idPathPoint" + partylines).style.visibility =
                        chckBox.checked ? "visible" : "hidden";
                }
            }

            /** Handles checkbox for chart legend pressed */
            function checkBoxShowChartLegendPressed(chkBox){
                var chartLegendGroup = document.getElementById("legendOnChart");
                chartLegendGroup.style.visibility =
                    chkBox.checked ? "visible" : "hidden";
            }

            /* Puts chosen ngram to textField. Invoked after selecting ngram from
                any of the select fields
            */
            function ngramSelected(ngramWord){
                alert(ngramWord);
                document.getElementById("inputNgram").value = ngramWord;
            }

            /* for manipulating*/
            function partyLegendCheckBoxClicked(checkBox){

                var divId = checkBox.parentNode.id; //divId = "legendDiv" + "nr"
                var clickedPartyId = divId.substring(9);

                var lineGroupId = document.getElementById("lineGroupId" + clickedPartyId);
                // lineGroupId.style.visibility =
                //     checkBox.checked ? "visible" : "hidden";

                 lineGroupId.style.display =
                    checkBox.checked ? "inline" : "none";


                // console.log("legen checkbox clicked!");
                // console.log(checkBox);
                // console.log(clickedPartyId);
            }


  /* Fills the form with necessary data*/
            function loadForms(){
                // fill 1grams select form
                var field1Grams = document.getElementById("selectField1Grams");
                for(i = 0, x = oneGrams.length;  i < x ; i++){
                    field1Grams.options[field1Grams.options.length] = new Option(oneGrams[i], i);
                }

                // fill 2grams select form
                var field2Grams = document.getElementById("selectField2Grams");
                for(i = 0, x = twoGrams.length;  i < x ; i++){
                    field2Grams.options[field2Grams.options.length] = new Option(twoGrams[i], i);
                }

                // fill 3grams select form
                var field3Grams = document.getElementById("selectField3Grams");
                for(i = 0, x = threeGrams.length;  i < x ; i++){
                    field3Grams.options[field3Grams.options.length] = new Option(threeGrams[i], i);
                }
            }
