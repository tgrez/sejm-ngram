describe('should display graph on pressing Szukaj, and allow to select range', function() {
    it('should just work TM', function () {
        browser.get('http://localhost:8000');
        // Need to send \n because otherwise pressing the button
        // works as if there was nothing entered. Manually it works ok
        // though.
        $('input[ng-model="newTag.text"]').sendKeys("opozycja\n");
        $('div.button[ng-click="search.run()"]').click();
        // click the first plot line
        var menuItem = element(by.id('legend-box')).all(by.repeater('plotLine in plotLines')).first();
        var colorInMenu = menuItem.element(by.css('.party-indicator')).getCssValue('background-color');
        menuItem.element(by.css('input')).click();
        var paths = element(by.css('svg#term-occurences-chart'))
          .all(by.css('path.line[style*=visible]'));
        // exactly one path should be visible
        expect(paths.count()).toEqual(1);
        // It is hard to test the line looks sane, let's
        // at least check that it has some d instructions
        // and the color matches the color from the menu
        var path = paths.first();
        path.getAttribute('d').then(function(d) {
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
            path.getAttribute('d').then(function(newD) {
                // Check that the path is somewhat changed, and that it is still non-empty.
                // The path [newD] is different, because it gets encoded using a different
                // scaleX
                expect(newD).not.toEqual(d);
                expect(newD.length).toBeGreaterThan(50);
                expect(path.getCssValue('stroke').then(normalizeColor)).toEqual(colorInMenu.then(normalizeColor));
                browser.sleep(500);
            });
        });
    });
});

// Put the functions in scope for eval, to parse the color
// objects. The problem is that stroke returns a color without alpha,
// while background-color returns a color with the alpha component, so
// need to normalize to compare.
function rgb(r,g,b) {return {red:r,green:g,blue:b,alpha:1}}
function rgba(r,g,b,a) {return {red:r,green:g,blue:b,alpha:a}}
function normalizeColor(colorStr) {return eval(colorStr);}
