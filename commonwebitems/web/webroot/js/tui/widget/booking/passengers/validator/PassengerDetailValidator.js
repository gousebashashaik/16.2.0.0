define("tui/widget/booking/passengers/validator/PassengerDetailValidator", [
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/date",
	"dojo/query",
  "tui/widget/booking/passengers/PassengerUtils"
	
    ], function(declare, lang, date, query,PassengerUtils ){
	/**
	 * @module
	 * @description   
	 */
	return declare("tui.widget.booking.passengers.validator.PassengerDetailValidator",[],  {
		
		

		 checkDOBDate : function(value, constraints){
      this.ageValid = true;
			var isValid =  (new RegExp("^(?:" + this.regExpGen(constraints) + ")"+(this.required?"":"?")+"$")).test(value) &&
				(!this.required || !this._isEmpty(value)) &&
				(this._isEmpty(value) || this.parse(value, constraints) !== undefined);
			var dobDate = PassengerUtils.getDateInCorrectFormat(value);
			this.invalidMessage= "The date of birth you entered is invalid";
		    if (isValid && dobDate !== null) {
		    	var calculatedAge =  ~~((this.returnDate - dobDate) / (31557600000));
		    	if(this.returnDate.getMonth() ===  dobDate.getMonth() &&
		    			this.returnDate.getDate() ===  dobDate.getDate() && (this.age % 4 !==0)){
		    		calculatedAge++;
		    	}
		    	if(this.returnDate.getMonth() ===  dobDate.getMonth() &&
		    			this.returnDate.getDate() ===  dobDate.getDate() && calculatedAge != this.returnDate.getFullYear()-dobDate.getFullYear()){
		    		calculatedAge--;
		    	}

                /* Infant age checking */
                if (this.age == 0) {
                    this.futureDate = false;

                    var dateString = value.split("/")[2] + "/" + (parseInt(value.split("/")[1], 10)) + "/" + value.split("/")[0];
                    var now = new Date();
                    var target = new Date(dateString);

                    if (target.getFullYear() > this.returnDate.getFullYear()) {
                        this.futureDate = true;
                    } else if (target.getFullYear() == this.returnDate.getFullYear()) {
                        if (target.getMonth() > this.returnDate.getMonth()) {
                            this.futureDate = true;
                        } else if (target.getMonth() == this.returnDate.getMonth()) {
                            if (target.getDate() > this.returnDate.getDate()) {
                                this.futureDate = true;
                            } else {
                                this.futureDate = false;
                            }
                        }
                    } else {
                        this.futureDate = false;
                    }
                    console.log("this.futureDate: ", this.futureDate);
                    if (this.futureDate) {
                        this.ageValid = false;
                        this.futureDate = true;
                        this.invalidMessage = "Please choose Infant not born yet option above";
                        return false;
                    }
                }
                /* Infant age checking ends here */
		    	if(calculatedAge === this.age){
            this.ageValid = true;
		    		return true;
		    	}else{
            this.ageValid = false;
          }
		    	this.invalidMessage= "The date of birth does not match the age entered earlier";
		    } 
		    return false; 
		},
    checkDOBRange : function(value, constraints){
      this.ageValid = true;
      var isValid =  (new RegExp("^(?:" + this.regExpGen(constraints) + ")"+(this.required?"":"?")+"$")).test(value) &&
          (!this.required || !this._isEmpty(value)) &&
          (this._isEmpty(value) || this.parse(value, constraints) !== undefined);
      var dobDate = PassengerUtils.getDateInCorrectFormat(value);
      this.invalidMessage= "The date of birth you entered is invalid";
      if (isValid && dobDate !== null) {
        var calculatedAge =  ~~((this.returnDate - dobDate) / (31557600000));
        if(this.returnDate.getMonth() ===  dobDate.getMonth() &&
            this.returnDate.getDate() ===  dobDate.getDate()&& (this.age % 4 !==0)){
          calculatedAge++;
        }
        if(this.returnDate.getMonth() ===  dobDate.getMonth() &&
    			this.returnDate.getDate() ===  dobDate.getDate() && calculatedAge != this.returnDate.getFullYear()-dobDate.getFullYear()){
    		calculatedAge--;
    	}
        if(calculatedAge >= this.minAge && calculatedAge < this.maxAge){
          return true;
        }else {
          this.ageValid = false;
        }
        this.invalidMessage= "The date of birth does not match the range";
      }
      return false;
    },

		checkConfirmationEmail : function (value, constraints) {
			var isValid =  (new RegExp("^(?:" + this.regExpGen(constraints) + ")"+(this.required?"":"?")+"$")).test(value) &&
					(!this.required || !this._isEmpty(value)) &&
					(this._isEmpty(value) || this.parse(value, constraints) !== undefined);
			var emailDijit = dijit.byId(query("input[name=email]")[0]);
			if(isValid && dojo.trim(value) ===emailDijit.value) {
				return true;
			}else {
				return false;
			}
		}
		
		
	});
});

