window.onload = function nrIdeas() {
    //alert(document.getElementById("ideaNr").textContent);


    $({ someValue: 0 }).animate({ someValue: Math.floor(document.getElementById("likeNr").textContent) }, {
        duration: 2000,
        easing: 'swing', // can be anything
        step: function () { // called on every step
            // Update the element's text with rounded-up value:
            $('.count1').text(commaSeparateNumber(Math.round(this.someValue)));
        }
    });
    $({ someValue: 0 }).animate({ someValue: Math.floor(document.getElementById("comNr").textContent) }, {
        duration: 2000,
        easing: 'swing', // can be anything
        step: function () { // called on every step
            // Update the element's text with rounded-up value:
            $('.count2').text(commaSeparateNumber(Math.round(this.someValue)));
        }
    });
    $({ someValue: 0 }).animate({ someValue: Math.floor(document.getElementById("matchNr").textContent) }, {
        duration: 2000,
        easing: 'swing', // can be anything
        step: function () { // called on every step
            // Update the element's text with rounded-up value:
            $('.count3').text(commaSeparateNumber(Math.round(this.someValue)));
        }
    });

    function commaSeparateNumber(val) {
        while (/(\d+)(\d{3})/.test(val.toString())) {
            val = val.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,");
        }
        return val;
    }
}