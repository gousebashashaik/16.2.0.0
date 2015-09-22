describe("Tui Underscore Extensions", function(){
    var tuiUnderscore;

    beforeEach(function(){
        tuiUnderscore = new dojo.require("tui/utils/TuiUnderscore");
    });

    it("should be loaded", function(){
        expect(tuiUnderscore).not.toBeNull();
    });

    it("should extend Underscore or LoDash", function(){
       expect(typeof _.mock).toBe("function");
       expect(typeof _.unmock).toBe("function");
       expect(typeof _.unmockAll).toBe("function");
       expect(typeof _.valid).toBe("function");
       expect(typeof _.formatDate).toBe("function");
       expect(typeof _.formatTime).toBe("function");
       expect(typeof _.isPositive).toBe("function");
       // etc...
    });

    it("_.mock should properly mock a function", function(){
        var obj = {key: "value", f: function() {return "original value";}};
        _.mock(obj, "f", function() {return "mocked value";});
        expect(obj.f()).toBe("mocked value");
    });

    it("_.unmockAll should properly unmock all functions in an object", function(){
        var obj = {key: "value", f: function() {return "original value";}};
        _.mock(obj, "f", function() {return "mocked value";});
        _.unmockAll(obj);
        expect(obj.f()).toBe("original value");
    });

    it("_.valid should validate a function's parameters", function(){
        var invalidArgs = function(){
            return _.valid(asdf);
        };
        var nullArgs = function(){
            return _.valid(null);
        };
        var combinedArgs = function(){
            return _.valid(asdf, "test");
        };
        var validArgs = function(){
            return _.valid("valid1", function(){});
        };
        expect(invalidArgs).toThrow();
        expect(nullArgs).toThrow();
        expect(combinedArgs).toThrow();
        expect(validArgs).not.toThrow();
    });

    it("_.formatDate should re-format a date string", function(){
        var inDate = "10-07-2015", outDate ="Fri 10 Jul 2015";
        var nonMatchingFormat = function() {
            return _.formatDate(inDate, "ddMMYYY");
        };
        expect(_.formatDate(inDate)).toBe(outDate);
        expect(nonMatchingFormat).toThrow();
    });

    it("_.formatTime should re-format a time string", function(){
        var inTime = "1915", outTime ="19:15";
        var nonMatchingFormat = function() {
            return _.formatTime(inTime, "hhM");
        };
        expect(_.formatTime(inTime)).toBe(outTime);
        expect(nonMatchingFormat).toThrow();
    });

    it("_.isPositive should test if a number is greater than 0", function(){
        var num = 2, str = '2';
        expect(_.isPositive(num)).toBe(true);
        expect(_.isPositive(str)).toBe(false);
    });

    it("_.negate should convert a number to it's negative equiavalent", function(){
        var num = 2;
        expect(_.negate(num)).toBe(-2);
    });

    it("_.when should run a supplied function if condition is met", function(){
        function testFn() {
          return true;
        }

        var testA = _.when(true, testFn);
        var testB = _.when(false, testFn);

        expect(testA).toBe(true);
        expect(testB).toBeNull();
    });

    it("_.inc should increment a number by one", function(){
      var num = 2;

      expect(_.inc(num)).toBe(3);
      expect(_.inc(num)).not.toBe(2);
    });

    it("_.dec should reduce a number by one", function(){
      var num = 5;

      expect(_.dec(num)).toBe(4);
      expect(_.dec(num)).not.toBe(5);
    });

    it("_.numberBefore should, given a string containing a number and a suffix return only the number (as a string)", function(){
      var test = "25px";
      expect(_.numberBefore(test, "px")).toBe('25');
      expect(_.numberBefore(test, "px")).not.toBe(25);
    });

    it("_.pixelValue should, given a pixel-value string (eg 32px) return only the Number (as a number)", function(){
      var test = "32px";
      expect(_.pixelValue(test)).toBe(32);
      expect(_.pixelValue(test)).not.toBe('32');
    });

    it("_.pixels should, given a number as a string return a string containing the number with 'px' appended", function(){
      var test = '48';
      expect(_.pixels(test)).toBe('48px');
      expect(_.pixelValue(test)).not.toBe('48');
    });

    it("_.isPositive should test if a number is positive", function(){
      var test = 12;
      expect(_.isPositive(test)).toBe(true);
      expect(_.isPositive(test * -1)).not.toBe(true);
    });

    it("_.isNegative should test if a number is negative", function(){
      var test = -12;
      expect(_.isNegative(test)).toBe(true);
      expect(_.isNegative(test * -1)).not.toBe(true);
    });

    it("_.isNull should test if a variable is null or undefined", function(){
      var undef,
          def = 'aa',
          nu = null;

      expect(_.isNull(undef)).toBe(true);
      expect(_.isNull(def)).not.toBe(true);
      expect(_.isNull(nu)).toBe(true);
    });

    it("_.capitalize should capitalise the first letter of a string", function(){
      var lc = 'laurent',
          uc = 'Laurent';

      expect(_.capitalize(lc)).toBe(uc);
      expect(_.capitalize(lc)).not.toBe(lc);
    });

    it("_.repeat should return an array of length n with value x repeated", function (){
      var str = 'string',
          len = 5;
      expect(_.repeat(str, len).length).toBe(5);
      expect(_.repeat(str, len)[0]).toBe('string');
      expect(_.repeat(str, len)[1]).toBe('string');
      expect(_.repeat(str, len)[2]).toBe('string');
      expect(_.repeat(str, len)[3]).toBe('string');
      expect(_.repeat(str, len)[4]).toBe('string');
    });

    it("_.wrapWords should wrap given word with <strong/> tag", function (){
      var string = "Spa and Holidays - Hotel Sensatori Spa",
          wrapped = "<strong>Spa</strong> and Holidays - Hotel Sensatori <strong>Spa</strong>",
          word = "spa";

      expect(_.wrapWords(string, word)).toEqual(wrapped);
    });

    it("_.wrapWords should wrap given word with given prefix and suffix", function (){
      var string = "Spa and Holidays - Hotel Sensatori Spa",
          wrapped = "|Spa* and Holidays - Hotel Sensatori |Spa*",
          word = "spa",
          pref="|",
          suff = "*";

      expect(_.wrapWords(string, word, pref, suff)).toBe(wrapped);
    });

    it("_.toDash should convert strings from camelCase to hyphenated", function(){
      var camel = "camelCaseString",
          hyphenated = "camel-case-string";
      expect(_.toDash(camel)).toBe(hyphenated);
    });

});