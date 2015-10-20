describe('should display graph on pressing Szukaj, and allow to select range', function() {
    it('should just work TM', function () {
        browser.get('http://localhost:8000');
        // Need to send \n because otherwise pressing the button
        // works as if there was nothing entered. Manually it works ok
        // though.
        $('input[ng-model="newTag.text"]').sendKeys("opozycja\n");
        $('div.button[ng-click="search.run()"]').click();
        // click the first plot line
        element(by.id('legend-box')).all(by.repeater('plotLine in plotLines')).first()
          .element(by.css('input')).click();
        var paths = element(by.css('svg#term-occurences-chart'))
          .all(by.css('path.line[style*=visible]'));
        // exactly one path should be visible
        expect(paths.count()).toEqual(1);
        // It is hard to test the line looks sane, let's
        // at least check that it has some d instructions
        paths.first().getAttribute('d').then(function(d) {
            expect(d.length).toBeGreaterThan(50);
            // now lets pick a range
            var slider = element(by.css('#range-filter-chart g.linesCanvas'));
            browser.actions()
              .mouseMove(slider, {x:20,y:10})
              .mouseDown() 
              .mouseMove(slider, {x:22,y:15})
              .mouseMove(slider, {x:150,y:16})
              .mouseUp()
              .perform();
            paths.first().getAttribute('d').then(function(newD) {
                // Check that the path is somewhat changed, and that it is still non-empty.
                // The path [newD] is different, because it gets encoded using a different
                // scaleX
                expect(newD).not.toEqual(d);
                expect(newD.length).toBeGreaterThan(50);
                browser.sleep(500);
            });
        });
    });
});
